package com.pl.protocol;

import android.annotation.SuppressLint;

import com.pl.common.Cfun;
import com.pl.cxml.CXmlAnalyze;
import com.pl.cxml.CXmlData;
import com.pl.utils.GlobalConsts;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class HhProtocol {

	private CXmlAnalyze xml = null;

	// 惠州密码
	private String password = "01020304050607080000000000000000";

	// private String password = "701f72a20f59f4140000000000000000";

	private void HhProtocol() {

	}

	/**
	 * 获得命令对应的数据域长度
	 * 
	 * @param cmd
	 * @return dataLength
	 */
	private String getDataLength1(String cmd) {
		String dataLength = "";
		if (cmd.isEmpty()) {
			return dataLength = "00";
		}
		int cmdInt = Cfun.parseToInt(cmd, 0, 16);
		switch (cmdInt) {
		case 0x04:
		case 0x05:
		case 0x07:
		case 0x08:
		case 0x09:
		case 0x16:
			dataLength = "06";
			break;
		case 0x06:
		case 0x21:
		case 0x35:
			dataLength = "05";
			break;
		case 0x18:
			dataLength = "04";
			break;
		case 0x23:
			dataLength = "07";
			break;
		default:
			dataLength = "02";
			break;
		}
		return dataLength;
	}

	/**
	 * 包头
	 * 
	 * @param a
	 * @return
	 */
	public String packetHeadMaker(CqueueData a) {
		String head = null;
		byte[] control = new byte[4];

		control[0] = (byte) 0xA1; //
		control[1] = (byte) 0x10; //
		control[2] = (byte) 0xA1; //
		control[3] = (byte) 0xE1; //

		head = Cfun.Byte2x16Str(control);

		return head;
	}

	/**
	 * 组包
	 * 
	 * @param data
	 * @param x
	 * @return
	 */
	public String encode(CqueueData data) {
		String packet = "";
		String all = "";
		String frameStart = "68"; // 帧起始符
		String syncSymbol = "68"; // 同步符
		String frameEnd = "16"; // 帧结束符

		String cmd = data.getCmdType();

		if (cmd.equals("04")) { // 从界面传入命令类型
			all += "a100a1e1";
			all += "";
		} else {
			all += "a110a1e1";
			all += data.getTargetAddr(); // 加入目标地址-->5BCD通讯号
		}
		if (data.isSet() == true) {
			all += getDataLength1(cmd); // 数据域长度(设置)
		} else {
			if (cmd.equals("23")) {
				all += "07"; // 查询 摄像表数据域长度
			} else {
				all += "02";// 查询 数据域长度(查询)
			}
		}
		all += syncSymbol; // 同步符
		String cmdData = "";
		if (data.isSet() == true) {
			if (cmd.isEmpty()) {
				return "noSet"; // 不是设置参数
			} else {
				cmdData += cmd; // 命令类型-->设置
			}
		} else {
			if (cmd.isEmpty()) {
				return "noQuery"; // 不是查询参数
			} else {
				cmdData += cmd; // 命令类型-->查询
			}
		}
		int dataLen = Cfun.parseToInt(getDataLength1(cmd), 0, 16) * 2;
		if (data.isSet() == true) {
			if (data.getDataBCD().length() == (dataLen - 2)) {
				cmdData += data.getDataBCD(); // 数据5BCD
			} else {
				return "lenError"; // 如果设定数据长度不对，则返回"lenError"
			}
		} else if (cmd.equals("01") && (data.getDataBCD().length() >= 11)
				&& data.getDataBCD().substring(10, 11).equals(";")) {
			if (data.getDataBCD().length() >= 19) {
				cmdData += data.getDataBCD().substring(0, 10);
			} else {
				return "lenError";
			}
		}else if (cmd.equals("81") && (data.getDataBCD().length() >= 11)
				&& data.getDataBCD().substring(10, 11).equals(";")) {
			if (data.getDataBCD().length() >= 19) {
				cmdData += data.getDataBCD().substring(0, 10);
			} else {
				return "lenError";
			}
		}  
		else if (cmd.equals("23")) { // 摄像表
			cmdData += "11" + data.getDataBCD().substring(0, 10);
		} else {
			cmdData += "ff"; // 查询默认数据为"FF" // 开阀/关阀使用查询"FF"
		}
		String checkSum = Cfun.accSum(Cfun.x16Str2Byte(cmdData));// 累加和校验，1字节
		packet += frameStart; // 帧起始符，固定为0x68
		packet += all;
		packet += cmdData;
		if (cmd.equals("23") || cmd.equals("28") || cmd.equals("25")
				|| cmd.equals("35")) { // 摄像表协议用累加和校验
			checkSum = Cfun.accSum(Cfun.x16Str2Byte(packet));
		}
		packet += checkSum.toLowerCase();
		packet += frameEnd; // 结束标志，固定为0x16
		if (cmd.equals("01") && (data.getDataBCD().length() >= 11)) {
			if (data.getDataBCD().substring(10, 11).equals(";")) {
				packet += data.getDataBCD().substring(11, 19);
			} else {
				return "dataError"; // 如果设定数据格式不对，则返回"dataError"
			}
			packet += "26";
		}
		if (cmd.equals("81") && (data.getDataBCD().length() >= 11)) {
			if (data.getDataBCD().substring(10, 11).equals(";")) {
				packet += data.getDataBCD().substring(11, 19);
			} else {
				return "dataError"; // 如果设定数据格式不对，则返回"dataError"
			}
			packet += "26";
		}
		return packet;
	}

	public String encodeShangHai(int operationType, String meterNo,
			ArrayList<String> params) {
		String message = "";
		if (operationType == GlobalConsts.COPY_OPERATION_COPY) { // 抄表
			message = "0715" + params.get(0);
		} else if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) { // 修改通讯号
			message = "0713" + params.get(0);
		} else if (operationType == GlobalConsts.COPY_OPERATION_OPENVALVE) { // 开阀
			message = "031700";
		} else if (operationType == GlobalConsts.COPY_OPERATION_CLOSEVALVE) { // 关阀
			message = "031701";
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_REGNET) { // 入网命令
			message = "0938" + params.get(0) + params.get(1) + params.get(2);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_RELAYREGNET) { // 中继入网命令
			message = "0739" + params.get(0);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_CANCELRELAYREGNET) { // 取消中继入网命令
			message = "073b" + params.get(0);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_TESTMODE) { // 测试模式设置命令
			message = "093c" + params.get(0) + params.get(1);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_GETPAMARS) { // 运行参数查询命令
			message = "033e" + params.get(0);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_RESET) { // 恢复出厂设置命令
			message = "073f" + params.get(0);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_SETKEY) {// 设定密钥命令
			message = "1731" + params.get(0) + params.get(1);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_SAFEMODE) { // 安全模式设置命令
			message = "0832" + params.get(0) + params.get(1);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_SETCOPYDAY) { // 设置抄表时间段
			message = "0418" + params.get(0) + params.get(1);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_GETCOPYDAY) { // 查询抄表时间段
			message = "0219";
		}
		String checkSum = Cfun.accSum(Cfun.x16Str2Byte(message));// 累加和校验，1字节
		message = meterNo + message + checkSum;
		return message;
	}

	public String encodeHuiZhou(CqueueData data) {
		String start = "6801";
		String len = "11";
		String message = data.getTargetAddr(); // 通讯号
		// 功能代码
		if (data.getCmdType().equals("01")) {
			message += "01"; // 抄表
			SimpleDateFormat copydf = new SimpleDateFormat("yyyyMMddHHmmss");
			String timeString = copydf.format(new Date());
			String dataHexString = Cfun.ToHEXString(Integer.parseInt(timeString
					.substring(0, 2)))
					+ Cfun.ToHEXString(Integer.parseInt(timeString.substring(2,
							4)))
					+ Cfun.ToHEXString(Integer.parseInt(timeString.substring(4,
							6)))
					+ Cfun.ToHEXString(Integer.parseInt(timeString.substring(6,
							8)))
					+ Cfun.ToHEXString(Integer.parseInt(timeString.substring(8,
							10)))
					+ Cfun.ToHEXString(Integer.parseInt(timeString.substring(
							10, 12)))
					+ Cfun.ToHEXString(Integer.parseInt(timeString.substring(
							12, 14)));
			message = message + dataHexString;
			String check = Cfun
					.getBCC(Cfun.x16Str2Byte(start + "0f" + message));// 异或合校验
			message = message + check + "8000"; // 对这部分进行加密
			message = parseByte2HexStr(encrypt(message, password));
		} else if (data.getCmdType().equals("02")) {
			message += "0C55"; // 开阀
			String check = Cfun
					.getBCC(Cfun.x16Str2Byte(start + "09" + message));// 异或合校验
			message = message + check + "8000000000000000";
			message = parseByte2HexStr(encrypt(message, password));
		} else if (data.getCmdType().equals("03")) {
			message += "0CAA"; // 关阀
			String check = Cfun
					.getBCC(Cfun.x16Str2Byte(start + "09" + message));// 异或合校验
			message = message + check + "8000000000000000";
			message = parseByte2HexStr(encrypt(message, password));
		} else if (data.getCmdType().equals("05")) { // 修改通讯编号
			message += "08" + data.getDataBCD();
			String check = Cfun
					.getBCC(Cfun.x16Str2Byte(start + "0C" + message));// 异或合校验
			message = message + check + "80000000";
			message = parseByte2HexStr(encrypt(message, password));
		} else if (data.getCmdType().equals("0b")) { // 设置表底数
			String dataBcd = data.getDataBCD();
			String intNumHex = Cfun.ToHEXString(Integer.parseInt(dataBcd
					.substring(0, 6)));
			intNumHex = Cfun.transeferStr(intNumHex, 6);
			String dataHex = Cfun.ToHEXString(Integer.parseInt(dataBcd
					.substring(6, 8)))
					+ intNumHex.substring(4, 6)
					+ intNumHex.substring(2, 4) + intNumHex.substring(0, 2);
			message += "0b" + dataHex;
			String check = Cfun
					.getBCC(Cfun.x16Str2Byte(start + "09" + message));// 异或合校验
			message = message + check + "8000000000";
			message = parseByte2HexStr(encrypt(message, password));
		} else if (data.getCmdType().equals("81")) {// 抄取冻结量
			message += "81";
			String check = Cfun
					.getBCC(Cfun.x16Str2Byte(start + "08" + message));// 异或合校验
			message = message + check + "800000000000000000";
			message = parseByte2HexStr(encrypt(message, password));
		}

		message = start + len + message + "16";
		return message;
	}

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 * @throws InvalidKeySpecException
	 */
	private static byte[] encrypt(String content, String password) {
		try {
			// KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// kgen.init(128, new SecureRandom(hexStr2ByteArray(password)));
			// SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = hexStr2ByteArray(password);
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			// Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// "算法/模式/补码方式"
			// byte[] byteContent = content.getBytes("utf-8");
			byte[] byteContent = hexStr2ByteArray(content);
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	private static byte[] decrypt(byte[] content, String password) {
		try {
			// KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// kgen.init(128, new SecureRandom(password.getBytes()));
			// SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = hexStr2ByteArray(password);
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			// Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// "算法/模式/补码方式"
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 16进制的字符串表示转成字节数组
	 * 
	 * @param hexString
	 *            16进制格式的字符串
	 * @return 转换后的字节数组
	 **/
	@SuppressLint("DefaultLocale")
	public static byte[] hexStr2ByteArray(String hexString) {

		hexString = hexString.toLowerCase();
		final byte[] byteArray = new byte[hexString.length() / 2];
		int k = 0;
		for (int i = 0; i < byteArray.length; i++) {
			// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
			// 将hex 转换成byte "&" 操作为了防止负数的自动扩展
			// hex转换成byte 其实只占用了4位，然后把高位进行右移四位
			// 然后“|”操作 低四位 就能得到 两个 16进制数转换成一个byte.
			//
			byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
			byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
			byteArray[i] = (byte) (high << 4 | low);
			k += 2;
		}
		return byteArray;
	}

	/**
	 * 组包，通过XML
	 * 
	 * @param data
	 * @param x
	 * @return
	 */
	public String encode(CXmlAnalyze x, CqueueData data) {
		xml = x;
		String packet = "";
		String all = "";
		String frameStart = "68"; // 帧起始符
		String syncSymbol = "68"; // 同步符
		String frameEnd = "16"; // 帧结束符
		// ArrayList<String> packArray = new ArrayList<String>();

		CXmlData xmlData = xml.GetAllAttr(data.getMname());
		// CXmlData dataAddr = xml.GetTheAttr("targetAddr", a.GetMname());
		// CXmlData dataCmd = xml.GetTheAttr("cmd", a.GetMname());
		if (xmlData.GetAttr("cmdSet").equals("04")) {
			// if (data.getCmdType("cmdSet").equals("04")) { //从界面传入命令类型
			all += "a100a1e1";
			all += "";
		} else {
			all += "a110a1e1";
			all += data.getTargetAddr(); // 加入目标地址-->5BCD通讯号
		}
		if (data.isSet() == true) {
			all += xmlData.GetAttr("dataLength1"); // 数据域长度(设置)
		} else {
			all += "02";// 查询 数据域长度(查询)
		}
		all += syncSymbol; // 同步符
		String cmdData = "";
		if (data.isSet() == true) {
			if (xmlData.GetAttr("cmdSet").isEmpty()) {
				return "noSet"; // 不是设置参数
			} else {
				cmdData += xmlData.GetAttr("cmdSet"); // 命令类型-->设置
			}
		} else {
			if (xmlData.GetAttr("cmdQuery").isEmpty()) {
				return "noQuery"; // 不是查询参数
			} else {
				cmdData += xmlData.GetAttr("cmdQuery"); // 命令类型-->查询
			}
		}
		int dataLen = Cfun.parseToInt(xmlData.GetAttr("dataLength1"), 0, 16) * 2;
		if (data.isSet() == true) {
			if (data.getDataBCD().length() == (dataLen - 2)) {
				cmdData += data.getDataBCD(); // 数据5BCD
			} else {
				return "lenError"; // 如果设定数据长度不对，则返回"lenError"
			}
		} else {
			cmdData += "ff"; // 查询默认数据为"FF" // 开阀/关阀使用查询"FF"
		}
		String checkSum = Cfun.accSum(Cfun.x16Str2Byte(cmdData));// 累加和校验，1字节
		packet += frameStart; // 帧起始符，固定为0x68
		packet += all;
		packet += cmdData;
		packet += checkSum.toLowerCase();
		packet += frameEnd; // 结束标志，固定为0x16
		// packArray.add(packet);
		return packet;
	}

	/**
	 * 校验包是否正确
	 * 
	 * @param unpack
	 * @return 1HEX校验码
	 */
	private String apbUnpack(String unpack) {
		int len;

		len = unpack.length();
		// 在字符串中查找指定字符
		while (len >= 16 * 2) {
			// 查找第一个"68"的位置
			if ((unpack.indexOf("68")) == -1) {
				break;
			}

			if (unpack.substring(unpack.length() - 2, unpack.length()).equals(
					"16")
					|| unpack.substring(unpack.length() - 2, unpack.length())
							.equals("26")) {
				;
			} else {
				break;
			}
			// 取数据域长度
			int dataLen = Integer.parseInt(unpack.substring(20, 22), 16);
			// 判断同步符
			String syncSymbol = unpack.substring(22, 24);
			if (syncSymbol.equals("68")) {
				;
			} else {
				break;
			}
			String cmdDataAcc = unpack.substring(24, 24 + dataLen * 2 + 2);
			// 校验包是否正确
			byte[] s_buf = Cfun.x16Str2Byte(cmdDataAcc);

			byte acc[] = new byte[1];
			acc[0] = s_buf[s_buf.length - 1]; // 取校验和

			if ((Cfun.accSum(s_buf, s_buf.length - 1)).equals(Cfun
					.Byte2x16Str(acc))) {
				return Cfun.Byte2x16Str(acc).toLowerCase(); // 返回小写校验和
			} else {
				return null; // 失败返回空字符
			}
		}
		return null;
	}

	/**
	 * 解包
	 * 
	 * @param unpack
	 * @return
	 */
	public CqueueData decode(String unpack, String cmdType) {
		if (unpack.length() < 6) {
			return null;
		}
		String accSum = "";
		String str1 = "a101"; // 系统标识和地址启用标识
		String str2 = "e1a1";
		String str3 = "b2a1";
		// CXmlData level1 = xml.GetAllAttr("*");
		CqueueData datas = new CqueueData();
		if (unpack.substring(2, 6).equals(str1)
				|| unpack.substring(2, 6).equals("a110")) { // 判断控制域标识
			accSum = apbUnpack(unpack); // 校验包,返回校验码
		} else {
			return null;
		}

		// if (accSum == null)
		// return null;

		datas.setSystemId(unpack.substring(2, 4)); // 系统标识
		datas.setAddressEn(unpack.substring(4, 6)); // 地址启用标识

		// 判断是e1包还是b2包
		if (str2.equals(unpack.substring(6, 10))) {
			datas.setTargetType(unpack.substring(6, 8));
			datas.setSourceType(unpack.substring(8, 10));
		} else if (str3.equals(unpack.substring(6, 10))) {
			datas.setTargetType(unpack.substring(6, 8));
			datas.setSourceType(unpack.substring(8, 10));
		} else {
			return null;
		}

		datas.setSourceAddr(unpack.substring(10, 20)); // 取得5BCD通讯号
		int len = Integer.parseInt(unpack.substring(20, 22), 16) * 2;
		datas.setDataLength(len); // 数据域长度
		if (unpack.substring(22, 24).equals("68")) {
			datas.setSyncSymbol(unpack.substring(22, 24)); // 同步符
		} else {
			return null;
		}

		// String cmdType = unpack.substring(24, 26);// 获得命令类型
		if (cmdType.equals("00")) {
			cmdType = "01";
		}
		if(cmdType.equals("81")){
			cmdType = "01";
		}
		if(cmdType.equals("82")){
			cmdType = "02";
		}
		if(cmdType.equals("83")){
			cmdType = "03";
		}
		datas.setCmdType(cmdType);
		String data1 = "";
		if (cmdType.equals("ff") || cmdType.equals("01")) {// 抄表数据
			if (unpack.length() > 33) {
				int sumUseGas = Integer.parseInt(unpack.substring(26, 34), 10); // 累计用气量
				datas.setSumUseGas(sumUseGas);// 累计用气量
				data1 = unpack.substring(26, 34) + ";";
			}
			if (unpack.length() > 39) {
				datas.setMeterState(unpack.substring(34, 38));// 表计状态
				data1 += unpack.substring(34, 38) + ";"
						+ unpack.substring(38, 40);
			}
			if (unpack.length() > 87) { // 旧程序不会返回以下数据
				int dBm = Integer.parseInt(unpack.substring(44, 46), 16); // 无线信号强度
				datas.setSignalStrength(dBm); // 信号强度
				int elec = Integer.parseInt(unpack.substring(46, 48), 16); // 电量
				datas.setPowerState(elec);// 表电量,百分比
				data1 += ";|-" + Integer.toString(155 - dBm) + ";"
						+ Integer.toString(elec) + ";"
						+ unpack.substring(48, 50) + ";"
						+ unpack.substring(50, 58) + ";"
						+ unpack.substring(58, 68) + ";"
						+ unpack.substring(68, 88);
			}
		} else if (cmdType.equals("10")) { // 表具号，基表号，模块号
			data1 = unpack.substring(26, 36) + ";" + unpack.substring(36, 46)
					+ ";" + unpack.substring(46, 56);
		} else if (cmdType.equals("15")) { // 无线发送测试指令
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 38)
					+ ";" + unpack.substring(38, 40);
		} else if (cmdType.equals("20")) { // 抄取累计冻结量指令20
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 38)
					+ ";" + unpack.substring(38, 40) + ";"
					+ unpack.substring(40, 50);
		} else if (cmdType.equals("22")) { // 查询抄表时间指令22
			data1 = unpack.substring(26, 28) + ";" + unpack.substring(28, 30)
					+ ";" + unpack.substring(30, 32) + ";"
					+ unpack.substring(32, 34);
		} else if (cmdType.equals("30")) { // 按月份抄取累计冻结量指令30
			data1 = unpack.substring(26, 30) + ";" + unpack.substring(30, 32)
					+ ";" + unpack.substring(32, 40) + ";"
					+ unpack.substring(40, 50) + ";" + unpack.substring(40, 60);
		} else {
			data1 += unpack.substring(26, 26 + (len - 2));
		}
		datas.setDataBCD(data1); // 获得数据

		return datas;
	}

	public CqueueData decodeHuiZhou(String unpack) {
		// 解密
		CqueueData datas;
		try {
			byte[] decryptResult = decrypt(
					parseHexStr2Byte(unpack.substring(6, unpack.length() - 4)),
					password); // 去掉头尾帧
			String revsString = parseByte2HexStr(decryptResult);
			datas = new CqueueData();
			datas.setSourceAddr(revsString.substring(0, 10));// 通讯号
			String cmdType = revsString.substring(10, 12);
			datas.setCmdType(cmdType);// 功能类型
			if (cmdType.equals("01")) {
				String s = revsString.substring(18, 20)
						+ revsString.substring(16, 18)
						+ revsString.substring(14, 16);
				String sumUseGas = Integer.parseInt(s, 16) + "";

				// sumUseGas +=
				// Cfun.patchHexString(Integer.parseInt(revsString.substring(16,
				// 18),16) + "",2);
				// sumUseGas +=
				// Cfun.patchHexString(Integer.parseInt(revsString.substring(14,
				// 16),16) + "",2);
				sumUseGas += Cfun
						.patchHexString(
								Integer.parseInt(revsString.substring(12, 14),
										16) + "", 2);
				if (sumUseGas.length() > 2) {
					sumUseGas = sumUseGas.substring(0, sumUseGas.length() - 2)
							+ "." + sumUseGas.substring(sumUseGas.length() - 2);
				} else {
					sumUseGas = "0." + sumUseGas;
				}
				datas.setSumUseGas(Double.parseDouble(sumUseGas));// 累计用气量
				datas.setMeterState(revsString.substring(34, 38));// 表计状态
			} else {
				datas.setDataBCD(revsString.substring(12));
			}
		} catch (NumberFormatException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			datas = null;
		}
		return datas;
	}

	public CqueueData decodeShangHai(String msg) {
		CqueueData datas = new CqueueData();
		datas.setSourceAddr(msg.substring(0, 10)); // 通讯号
		datas.setCmdType(msg.substring(10, 14)); // 长度+命令码
		datas.setDataBCD(msg.substring(14, msg.length() - 2)); // 数据区
		return datas;
	}

	/**
	 * 解包
	 * 
	 * @param unpack
	 * @return
	 */
	public CqueueData decode(CXmlAnalyze x, String unpack) {
		if (unpack.length() < 6) {
			return null;
		}
		xml = x;
		String accSum = "";
		String str1 = "a101"; // 系统标识和地址启用标识
		String str2 = "e1a1";
		String str3 = "b2a1";
		// CXmlData level1 = xml.GetAllAttr("*");
		CqueueData datas = new CqueueData();
		if (unpack.substring(2, 6).equals(str1)
				|| unpack.substring(2, 6).equals("a110")) { // 判断控制域标识
			accSum = apbUnpack(unpack); // 校验包,返回校验码
		} else {
			return null;
		}

		if (accSum == null)
			return null;

		datas.setSystemId(unpack.substring(2, 4)); // 系统标识
		datas.setAddressEn(unpack.substring(4, 6)); // 地址启用标识

		// 判断是e1包还是b2包
		if (str2.equals(unpack.substring(6, 10))) {
			datas.setTargetType(unpack.substring(6, 8));
			datas.setSourceType(unpack.substring(8, 10));
		} else if (str3.equals(unpack.substring(6, 10))) {
			datas.setTargetType(unpack.substring(6, 8));
			datas.setSourceType(unpack.substring(8, 10));
		} else {
			return null;
		}

		datas.setSourceAddr(unpack.substring(10, 20)); // 取得5BCD通讯号
		int len = Integer.parseInt(unpack.substring(20, 22), 16) * 2;
		datas.setDataLength(len); // 数据域长度
		if (unpack.substring(22, 24).equals("68")) {
			datas.setSyncSymbol(unpack.substring(22, 24)); // 同步符
		} else {
			return null;
		}
		/*
		 * String cmdQ=""; String cmdS=""; for (int i = 0; level1.ChangeTo(i +
		 * 1); i++) { if
		 * ((level1.GetAttr(".").substring(1)).equals(packet.substring(22, 24)))
		 * { cmdQ = level1.GetAttr("cmdQuery"); // 获得命令类型 cmdS =
		 * level1.GetAttr("cmdSet"); // 获得命令类型 } } if(packet.substring(22,
		 * 24).equals(cmdQ)||packet.substring(22, 24).equals(cmdS)){
		 * d.setCmdType(packet.substring(22, 24)); // 获得命令类型 }
		 */
		String cmdType = unpack.substring(24, 26);// 获得命令类型
		datas.setCmdType(cmdType);
		String data1 = "";
		if (cmdType.equals("ff") || cmdType.equals("01")) {// 抄表数据
			int sumUseGas = Integer.parseInt(unpack.substring(26, 34), 10); // 累计用气量
			datas.setSumUseGas(sumUseGas);// 累计用气量
			datas.setMeterState(unpack.substring(34, 38));// 表计状态
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 38)
					+ ";" + unpack.substring(38, 40);
			int dBm = Integer.parseInt(unpack.substring(44, 46), 16); // 无线信号强度
			datas.setSignalStrength(dBm); // 信号强度
			int elec = Integer.parseInt(unpack.substring(46, 48), 16); // 电量
			datas.setPowerState(elec);// 表电量,百分比
			data1 += ";|-" + Integer.toString(155 - dBm) + ";"
					+ Integer.toString(elec) + ";" + unpack.substring(48, 50)
					+ ";" + unpack.substring(50, 58) + ";"
					+ unpack.substring(58, 68) + ";" + unpack.substring(68, 88);
		} else if (cmdType.equals("10")) { // 表具号，基表号，模块号
			data1 = unpack.substring(26, 36) + ";" + unpack.substring(36, 46)
					+ ";" + unpack.substring(46, 56);
		} else if (cmdType.equals("15")) { // 无线发送测试指令
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 38)
					+ ";" + unpack.substring(38, 40);
		} else if (cmdType.equals("20")) { // 抄取累计冻结量指令20
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 38)
					+ ";" + unpack.substring(38, 40) + ";"
					+ unpack.substring(40, 50);
		} else if (cmdType.equals("22")) { // 查询抄表时间指令22
			data1 = unpack.substring(26, 28) + ";" + unpack.substring(28, 30)
					+ ";" + unpack.substring(30, 32) + ";"
					+ unpack.substring(32, 34);
		} else if (cmdType.equals("30")) { // 按月份抄取累计冻结量指令30
			data1 = unpack.substring(26, 30) + ";" + unpack.substring(30, 32)
					+ ";" + unpack.substring(32, 40) + ";"
					+ unpack.substring(40, 50) + ";" + unpack.substring(40, 60);
		} else {
			data1 += unpack.substring(26, 26 + (len - 2));
		}
		datas.setDataBCD(data1); // 获得数据

		return datas;
	}

	/**
	 * 获得命令对应的数据域长度
	 * 
	 * @param cmd
	 * @return dataLength
	 */
	private String getDataLengthIC(String cmd) {
		String dataLength = "";
		if (cmd.isEmpty()) {
			return dataLength = "00";
		}
		int cmdInt = Cfun.parseToInt(cmd, 0, 16);
		switch (cmdInt) {
		case 0x01:
		case 0x02:
		case 0x16:
			dataLength = "06";
			break;
		case 0x03:
		case 0x0B:
		case 0x0D:
			dataLength = "05";
			break;
		case 0x09:
			dataLength = "13";
			break;
		case 0x11:
		case 0x13:
		case 0x19:
			dataLength = "03";
			break;
		case 0x14:
			dataLength = "20";
			break;
		case 0x20:
		case 0x21:
			dataLength = "07";
			break;
		default:
			dataLength = "02";
			break;
		}
		return dataLength;
	}

	/**
	 * 组包
	 * 
	 * @param data
	 * @param x
	 * @return
	 */
	public String encodeIC(CqueueData data) {
		String packet = "";
		String all = "";
		String frameStart = "68"; // 帧起始符
		String syncSymbol = "68"; // 同步符
		String frameEnd = "16"; // 帧结束符
		String cmd = data.getCmdType();

		if (cmd.equals("01")) { // 从界面传入命令类型
			all += "a200a1e1";
			all += "";
		} else {
			all += "a210a1e1";
			all += data.getTargetAddr(); // 加入目标地址-->5BCD通讯号
		}
		if (data.isSet() == true) {
			all += getDataLengthIC(cmd); // 数据域长度(设置)
		} else {
			all += "02";// 查询 数据域长度(查询)
		}
		all += syncSymbol; // 同步符
		String cmdData = "";
		if (data.isSet() == true) {
			if (cmd.isEmpty()) {
				return "noSet"; // 不是设置参数
			} else {
				cmdData += cmd; // 命令类型-->设置
			}
		} else {
			if (cmd.isEmpty()) {
				return "noQuery"; // 不是查询参数
			} else {
				cmdData += cmd; // 命令类型-->查询
			}
		}
		int dataLen = Cfun.parseToInt(getDataLengthIC(cmd), 0, 16) * 2;
		if (data.isSet() == true) {
			if (data.getDataBCD().length() == (dataLen - 2)) {
				cmdData += data.getDataBCD(); // 数据5BCD
			} else {
				return "lenError"; // 如果设定数据长度不对，则返回"lenError"
			}
		} else {
			// cmdData += "ff"; // 查询默认数据为"FF" // 开阀/关阀使用查询"FF"
			cmdData += "38";
		}
		// String checkSum = Cfun.accSum(Cfun.x16Str2Byte(cmdData));// 累加和校验，1字节
		String checkSum = "00";
		packet += frameStart; // 帧起始符，固定为0x68
		packet += all;
		packet += cmdData;
		packet += checkSum.toLowerCase();
		packet += frameEnd; // 结束标志，固定为0x16
		if (data.getCmdType() == null) {
			;
		} else if (data.getCmdType().equals("06")) {
			packet += data.getBcdDate();
			packet += Cfun.accSum(Cfun.x16Str2Byte(data.getBcdDate()));
			packet += "26";
		}
		// packArray.add(packet);
		return packet;
	}

	/**
	 * 解包
	 * 
	 * @param unpack
	 * @return
	 */
	public CqueueData decodeIC(String unpack) {
		// String accSum = "";
		String str1 = "a201"; // 系统标识和地址启用标识
		String str2 = "e1a1";

		CqueueData datas = new CqueueData();
		if (unpack.substring(2, 6).equals(str1)) { // 判断控制域标识
			// accSum = apbUnpack(unpack); // 校验包,返回校验码
		} else {
			return null;
		}

		// if (accSum == null)
		// return null;

		datas.setSystemId(unpack.substring(2, 4)); // 系统标识
		datas.setAddressEn(unpack.substring(4, 6)); // 地址启用标识

		// 判断是否是e1a1返回包
		if (str2.equals(unpack.substring(6, 10))) {
			datas.setTargetType(unpack.substring(6, 8));
			datas.setSourceType(unpack.substring(8, 10));
		} else {
			return null;
		}

		datas.setSourceAddr(unpack.substring(10, 20)); // 取得5BCD通讯号
		int len = Integer.parseInt(unpack.substring(20, 22), 16) * 2;
		datas.setDataLength(len); // 数据域长度
		if (unpack.substring(22, 24).equals("68")) {
			datas.setSyncSymbol(unpack.substring(22, 24)); // 同步符
		} else {
			return null;
		}

		String cmdType = unpack.substring(24, 26);// 获得命令类型
		datas.setCmdType(cmdType);
		String data1 = "";
		if (cmdType.equals("06")) {// 抄取数据
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 42)
					+ ";" + unpack.substring(42, 50) + ";"
					+ unpack.substring(50, 54) + ";" + unpack.substring(54, 56)
					+ ";" + unpack.substring(56, 58) + ";"
					+ unpack.substring(58, 60) + ";" + unpack.substring(60, 66)
					+ ";" + unpack.substring(66, 72) + ";"
					+ unpack.substring(72, 78);
			// + ";" + unpack.substring(78, 84) + ";" + unpack.substring(84,
			// 90); 3BCD上上月累计量、3BCD上上上月累计量
			// data1 += ";|" + unpack.substring(94, 100) + ";"
			// + unpack.substring(100, 110) + ";"
			// + unpack.substring(110, 120) + ";"
			// + unpack.substring(120, 126);
			data1 += ";|" + unpack.substring(82, 88) + ";"
					+ unpack.substring(88, 98) + ";"
					+ unpack.substring(98, 108) + ";"
					+ unpack.substring(108, 114);
		} else if (cmdType.equals("08")) { // 查询费率参数
			data1 = unpack.substring(26, 32) + ";" + unpack.substring(32, 38)
					+ ";" + unpack.substring(38, 44) + ";"
					+ unpack.substring(44, 50) + ";" + unpack.substring(50, 56)
					+ ";" + unpack.substring(56, 62);
		} else if (cmdType.equals("18")) { // 查询费率周期参数
			data1 = unpack.substring(26, 28) + ";" + unpack.substring(28, 30)
					+ ";" + unpack.substring(30, 36) + ";"
					+ unpack.substring(36, 38);
			data1 += ";|" + unpack.substring(38, 48) + ";"
					+ unpack.substring(48, 52) + ";" + unpack.substring(52, 58)
					+ ";" + unpack.substring(58, 64) + ";"
					+ unpack.substring(64, 70) + ";" + unpack.substring(70, 76)
					+ ";" + unpack.substring(76, 82) + ";"
					+ unpack.substring(82, 88) + ";" + unpack.substring(88, 90);
		} else if (cmdType.equals("19")) { // 设置费率周期参数
			data1 = unpack.substring(26, 28);
			data1 += ";|" + unpack.substring(28, 30);
		} else if (cmdType.equals("20") || cmdType.equals("21")) { // 无线充值/退款协议
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 42);
		} else {
			data1 += unpack.substring(26, 26 + (len - 2));
		}
		datas.setDataBCD(data1); // 获得数据

		return datas;
	}

	/**
	 * 组包
	 * 
	 * @param data
	 * @param x
	 * @return
	 */
	public String encodeIC(CqueueData data, CXmlAnalyze x) {
		xml = x;
		String packet = "";
		String all = "";
		String frameStart = "68"; // 帧起始符
		String syncSymbol = "68"; // 同步符
		String frameEnd = "16"; // 帧结束符
		CXmlData xmlData = xml.GetAllAttr(data.getMname());
		// CXmlData dataAddr = xml.GetTheAttr("targetAddr", a.GetMname());
		// CXmlData dataCmd = xml.GetTheAttr("cmd", a.GetMname());
		if (xmlData.GetAttr("set").equals("01")) {
			// if (data.getCmdType("cmdSet").equals("04")) { //从界面传入命令类型
			all += "a200a1e1";
			all += "";
		} else {
			all += "a210a1e1";
			all += data.getTargetAddr(); // 加入目标地址-->5BCD通讯号
		}
		if (data.isSet() == true) {
			all += xmlData.GetAttr("dataLength1"); // 数据域长度(设置)
		} else {
			all += "02";// 查询 数据域长度(查询)
		}
		all += syncSymbol; // 同步符
		String cmdData = "";
		if (data.isSet() == true) {
			if (xmlData.GetAttr("set").isEmpty()) {
				return "noSet"; // 不是设置参数
			} else {
				cmdData = xmlData.GetAttr("set"); // 命令类型-->设置
			}
		} else {
			if (xmlData.GetAttr("query").isEmpty()) {
				return "noQuery"; // 不是查询参数
			} else {
				cmdData = xmlData.GetAttr("query"); // 命令类型-->查询
			}
		}
		int dataLen = Cfun.parseToInt(xmlData.GetAttr("dataLength1"), 0, 16) * 2;
		if (data.isSet() == true) {
			if (data.getDataBCD().length() == (dataLen - 2)) {
				cmdData += data.getDataBCD(); // 数据5BCD
			} else {
				return ""; // 如果设定数据长度不对，则返回""
			}
		} else {
			cmdData += "ff"; // 查询默认数据为"FF" // 开阀/关阀使用查询"FF"
		}
		String checkSum = Cfun.accSum(Cfun.x16Str2Byte(cmdData));// 累加和校验，1字节
		packet += frameStart; // 帧起始符，固定为0x68
		packet += all;
		packet += cmdData;
		packet += checkSum;
		packet += frameEnd; // 结束标志，固定为0x16
		// packArray.add(packet);
		return packet;
	}

	/**
	 * 解包
	 * 
	 * @param unpack
	 * @return
	 */
	public CqueueData decodeIC(String unpack, CXmlAnalyze x) {
		xml = x;
		String packet = "";
		String str1 = "a201";
		String str2 = "e1a1";
		// CXmlData level1 = xml.GetAllAttr("*");
		CqueueData datas = new CqueueData();
		if (unpack.substring(2, 6).equals(str1)) { // 判断控制域标识
			packet = apbUnpack(unpack); // 校验并取得干净的包,是去掉包头和包尾以及校验
		} else {
			return null;
		}

		if (packet == null)
			return null;

		datas.setSystemId(packet.substring(0, 2)); // 系统标识
		datas.setAddressEn(packet.substring(2, 4)); // 地址启用标识

		// 判断是E1包还是b2包
		if (str2.equals(packet.substring(4, 8))) {
			datas.setTargetType(packet.substring(4, 6));
			datas.setSourceType(packet.substring(6, 8));
		} else {
			return null;
		}

		datas.setSourceAddr(packet.substring(8, 18)); // 取得5BCD通讯号
		int len = Integer.parseInt(packet.substring(18, 20)) * 2;
		datas.setDataLength(len); // 数据域长度
		if (packet.substring(20, 22).equals("68")) {
			datas.setSyncSymbol(packet.substring(20, 22)); // 同步符
		} else {
			return null;
		}
		/*
		 * String cmdQ=""; String cmdS=""; for (int i = 0; level1.ChangeTo(i +
		 * 1); i++) { if
		 * ((level1.GetAttr(".").substring(1)).equals(packet.substring(22, 24)))
		 * { cmdQ = level1.GetAttr("cmdQuery"); // 获得命令类型 cmdS =
		 * level1.GetAttr("cmdSet"); // 获得命令类型 } } if(packet.substring(22,
		 * 24).equals(cmdQ)||packet.substring(22, 24).equals(cmdS)){
		 * d.setCmdType(packet.substring(22, 24)); // 获得命令类型 }
		 */
		datas.setCmdType(packet.substring(22, 24)); // 获得命令类型
		datas.setDataBCD(packet.substring(24, 24 + (len - 2))); // 获得数据

		return datas;
	}
}
