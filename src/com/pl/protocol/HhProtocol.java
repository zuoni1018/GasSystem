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

	// ��������
	private String password = "01020304050607080000000000000000";

	// private String password = "701f72a20f59f4140000000000000000";

	private void HhProtocol() {

	}

	/**
	 * ��������Ӧ�������򳤶�
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
	 * ��ͷ
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
	 * ���
	 * 
	 * @param data
	 * @param x
	 * @return
	 */
	public String encode(CqueueData data) {
		String packet = "";
		String all = "";
		String frameStart = "68"; // ֡��ʼ��
		String syncSymbol = "68"; // ͬ����
		String frameEnd = "16"; // ֡������

		String cmd = data.getCmdType();

		if (cmd.equals("04")) { // �ӽ��洫����������
			all += "a100a1e1";
			all += "";
		} else {
			all += "a110a1e1";
			all += data.getTargetAddr(); // ����Ŀ���ַ-->5BCDͨѶ��
		}
		if (data.isSet() == true) {
			all += getDataLength1(cmd); // �����򳤶�(����)
		} else {
			if (cmd.equals("23")) {
				all += "07"; // ��ѯ ����������򳤶�
			} else {
				all += "02";// ��ѯ �����򳤶�(��ѯ)
			}
		}
		all += syncSymbol; // ͬ����
		String cmdData = "";
		if (data.isSet() == true) {
			if (cmd.isEmpty()) {
				return "noSet"; // �������ò���
			} else {
				cmdData += cmd; // ��������-->����
			}
		} else {
			if (cmd.isEmpty()) {
				return "noQuery"; // ���ǲ�ѯ����
			} else {
				cmdData += cmd; // ��������-->��ѯ
			}
		}
		int dataLen = Cfun.parseToInt(getDataLength1(cmd), 0, 16) * 2;
		if (data.isSet() == true) {
			if (data.getDataBCD().length() == (dataLen - 2)) {
				cmdData += data.getDataBCD(); // ����5BCD
			} else {
				return "lenError"; // ����趨���ݳ��Ȳ��ԣ��򷵻�"lenError"
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
		else if (cmd.equals("23")) { // �����
			cmdData += "11" + data.getDataBCD().substring(0, 10);
		} else {
			cmdData += "ff"; // ��ѯĬ������Ϊ"FF" // ����/�ط�ʹ�ò�ѯ"FF"
		}
		String checkSum = Cfun.accSum(Cfun.x16Str2Byte(cmdData));// �ۼӺ�У�飬1�ֽ�
		packet += frameStart; // ֡��ʼ�����̶�Ϊ0x68
		packet += all;
		packet += cmdData;
		if (cmd.equals("23") || cmd.equals("28") || cmd.equals("25")
				|| cmd.equals("35")) { // �����Э�����ۼӺ�У��
			checkSum = Cfun.accSum(Cfun.x16Str2Byte(packet));
		}
		packet += checkSum.toLowerCase();
		packet += frameEnd; // ������־���̶�Ϊ0x16
		if (cmd.equals("01") && (data.getDataBCD().length() >= 11)) {
			if (data.getDataBCD().substring(10, 11).equals(";")) {
				packet += data.getDataBCD().substring(11, 19);
			} else {
				return "dataError"; // ����趨���ݸ�ʽ���ԣ��򷵻�"dataError"
			}
			packet += "26";
		}
		if (cmd.equals("81") && (data.getDataBCD().length() >= 11)) {
			if (data.getDataBCD().substring(10, 11).equals(";")) {
				packet += data.getDataBCD().substring(11, 19);
			} else {
				return "dataError"; // ����趨���ݸ�ʽ���ԣ��򷵻�"dataError"
			}
			packet += "26";
		}
		return packet;
	}

	public String encodeShangHai(int operationType, String meterNo,
			ArrayList<String> params) {
		String message = "";
		if (operationType == GlobalConsts.COPY_OPERATION_COPY) { // ����
			message = "0715" + params.get(0);
		} else if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) { // �޸�ͨѶ��
			message = "0713" + params.get(0);
		} else if (operationType == GlobalConsts.COPY_OPERATION_OPENVALVE) { // ����
			message = "031700";
		} else if (operationType == GlobalConsts.COPY_OPERATION_CLOSEVALVE) { // �ط�
			message = "031701";
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_REGNET) { // ��������
			message = "0938" + params.get(0) + params.get(1) + params.get(2);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_RELAYREGNET) { // �м���������
			message = "0739" + params.get(0);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_CANCELRELAYREGNET) { // ȡ���м���������
			message = "073b" + params.get(0);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_TESTMODE) { // ����ģʽ��������
			message = "093c" + params.get(0) + params.get(1);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_GETPAMARS) { // ���в�����ѯ����
			message = "033e" + params.get(0);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_RESET) { // �ָ�������������
			message = "073f" + params.get(0);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_SETKEY) {// �趨��Կ����
			message = "1731" + params.get(0) + params.get(1);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_SAFEMODE) { // ��ȫģʽ��������
			message = "0832" + params.get(0) + params.get(1);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_SETCOPYDAY) { // ���ó���ʱ���
			message = "0418" + params.get(0) + params.get(1);
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_GETCOPYDAY) { // ��ѯ����ʱ���
			message = "0219";
		}
		String checkSum = Cfun.accSum(Cfun.x16Str2Byte(message));// �ۼӺ�У�飬1�ֽ�
		message = meterNo + message + checkSum;
		return message;
	}

	public String encodeHuiZhou(CqueueData data) {
		String start = "6801";
		String len = "11";
		String message = data.getTargetAddr(); // ͨѶ��
		// ���ܴ���
		if (data.getCmdType().equals("01")) {
			message += "01"; // ����
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
					.getBCC(Cfun.x16Str2Byte(start + "0f" + message));// ����У��
			message = message + check + "8000"; // ���ⲿ�ֽ��м���
			message = parseByte2HexStr(encrypt(message, password));
		} else if (data.getCmdType().equals("02")) {
			message += "0C55"; // ����
			String check = Cfun
					.getBCC(Cfun.x16Str2Byte(start + "09" + message));// ����У��
			message = message + check + "8000000000000000";
			message = parseByte2HexStr(encrypt(message, password));
		} else if (data.getCmdType().equals("03")) {
			message += "0CAA"; // �ط�
			String check = Cfun
					.getBCC(Cfun.x16Str2Byte(start + "09" + message));// ����У��
			message = message + check + "8000000000000000";
			message = parseByte2HexStr(encrypt(message, password));
		} else if (data.getCmdType().equals("05")) { // �޸�ͨѶ���
			message += "08" + data.getDataBCD();
			String check = Cfun
					.getBCC(Cfun.x16Str2Byte(start + "0C" + message));// ����У��
			message = message + check + "80000000";
			message = parseByte2HexStr(encrypt(message, password));
		} else if (data.getCmdType().equals("0b")) { // ���ñ����
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
					.getBCC(Cfun.x16Str2Byte(start + "09" + message));// ����У��
			message = message + check + "8000000000";
			message = parseByte2HexStr(encrypt(message, password));
		} else if (data.getCmdType().equals("81")) {// ��ȡ������
			message += "81";
			String check = Cfun
					.getBCC(Cfun.x16Str2Byte(start + "08" + message));// ����У��
			message = message + check + "800000000000000000";
			message = parseByte2HexStr(encrypt(message, password));
		}

		message = start + len + message + "16";
		return message;
	}

	/**
	 * ����
	 * 
	 * @param content
	 *            ��Ҫ���ܵ�����
	 * @param password
	 *            ��������
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
			// Cipher cipher = Cipher.getInstance("AES");// ����������
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// "�㷨/ģʽ/���뷽ʽ"
			// byte[] byteContent = content.getBytes("utf-8");
			byte[] byteContent = hexStr2ByteArray(content);
			cipher.init(Cipher.ENCRYPT_MODE, key);// ��ʼ��
			byte[] result = cipher.doFinal(byteContent);
			return result; // ����
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
	 * ����
	 * 
	 * @param content
	 *            ����������
	 * @param password
	 *            ������Կ
	 * @return
	 */
	private static byte[] decrypt(byte[] content, String password) {
		try {
			// KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// kgen.init(128, new SecureRandom(password.getBytes()));
			// SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = hexStr2ByteArray(password);
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			// Cipher cipher = Cipher.getInstance("AES");// ����������
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// "�㷨/ģʽ/���뷽ʽ"
			cipher.init(Cipher.DECRYPT_MODE, key);// ��ʼ��
			byte[] result = cipher.doFinal(content);
			return result; // ����
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
	 * ��������ת����16����
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
	 * ��16����ת��Ϊ������
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
	 * 16���Ƶ��ַ�����ʾת���ֽ�����
	 * 
	 * @param hexString
	 *            16���Ƹ�ʽ���ַ���
	 * @return ת������ֽ�����
	 **/
	@SuppressLint("DefaultLocale")
	public static byte[] hexStr2ByteArray(String hexString) {

		hexString = hexString.toLowerCase();
		final byte[] byteArray = new byte[hexString.length() / 2];
		int k = 0;
		for (int i = 0; i < byteArray.length; i++) {
			// ��Ϊ��16���ƣ����ֻ��ռ��4λ��ת�����ֽ���Ҫ����16���Ƶ��ַ�����λ����
			// ��hex ת����byte "&" ����Ϊ�˷�ֹ�������Զ���չ
			// hexת����byte ��ʵֻռ����4λ��Ȼ��Ѹ�λ����������λ
			// Ȼ��|������ ����λ ���ܵõ� ���� 16������ת����һ��byte.
			//
			byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
			byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
			byteArray[i] = (byte) (high << 4 | low);
			k += 2;
		}
		return byteArray;
	}

	/**
	 * �����ͨ��XML
	 * 
	 * @param data
	 * @param x
	 * @return
	 */
	public String encode(CXmlAnalyze x, CqueueData data) {
		xml = x;
		String packet = "";
		String all = "";
		String frameStart = "68"; // ֡��ʼ��
		String syncSymbol = "68"; // ͬ����
		String frameEnd = "16"; // ֡������
		// ArrayList<String> packArray = new ArrayList<String>();

		CXmlData xmlData = xml.GetAllAttr(data.getMname());
		// CXmlData dataAddr = xml.GetTheAttr("targetAddr", a.GetMname());
		// CXmlData dataCmd = xml.GetTheAttr("cmd", a.GetMname());
		if (xmlData.GetAttr("cmdSet").equals("04")) {
			// if (data.getCmdType("cmdSet").equals("04")) { //�ӽ��洫����������
			all += "a100a1e1";
			all += "";
		} else {
			all += "a110a1e1";
			all += data.getTargetAddr(); // ����Ŀ���ַ-->5BCDͨѶ��
		}
		if (data.isSet() == true) {
			all += xmlData.GetAttr("dataLength1"); // �����򳤶�(����)
		} else {
			all += "02";// ��ѯ �����򳤶�(��ѯ)
		}
		all += syncSymbol; // ͬ����
		String cmdData = "";
		if (data.isSet() == true) {
			if (xmlData.GetAttr("cmdSet").isEmpty()) {
				return "noSet"; // �������ò���
			} else {
				cmdData += xmlData.GetAttr("cmdSet"); // ��������-->����
			}
		} else {
			if (xmlData.GetAttr("cmdQuery").isEmpty()) {
				return "noQuery"; // ���ǲ�ѯ����
			} else {
				cmdData += xmlData.GetAttr("cmdQuery"); // ��������-->��ѯ
			}
		}
		int dataLen = Cfun.parseToInt(xmlData.GetAttr("dataLength1"), 0, 16) * 2;
		if (data.isSet() == true) {
			if (data.getDataBCD().length() == (dataLen - 2)) {
				cmdData += data.getDataBCD(); // ����5BCD
			} else {
				return "lenError"; // ����趨���ݳ��Ȳ��ԣ��򷵻�"lenError"
			}
		} else {
			cmdData += "ff"; // ��ѯĬ������Ϊ"FF" // ����/�ط�ʹ�ò�ѯ"FF"
		}
		String checkSum = Cfun.accSum(Cfun.x16Str2Byte(cmdData));// �ۼӺ�У�飬1�ֽ�
		packet += frameStart; // ֡��ʼ�����̶�Ϊ0x68
		packet += all;
		packet += cmdData;
		packet += checkSum.toLowerCase();
		packet += frameEnd; // ������־���̶�Ϊ0x16
		// packArray.add(packet);
		return packet;
	}

	/**
	 * У����Ƿ���ȷ
	 * 
	 * @param unpack
	 * @return 1HEXУ����
	 */
	private String apbUnpack(String unpack) {
		int len;

		len = unpack.length();
		// ���ַ����в���ָ���ַ�
		while (len >= 16 * 2) {
			// ���ҵ�һ��"68"��λ��
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
			// ȡ�����򳤶�
			int dataLen = Integer.parseInt(unpack.substring(20, 22), 16);
			// �ж�ͬ����
			String syncSymbol = unpack.substring(22, 24);
			if (syncSymbol.equals("68")) {
				;
			} else {
				break;
			}
			String cmdDataAcc = unpack.substring(24, 24 + dataLen * 2 + 2);
			// У����Ƿ���ȷ
			byte[] s_buf = Cfun.x16Str2Byte(cmdDataAcc);

			byte acc[] = new byte[1];
			acc[0] = s_buf[s_buf.length - 1]; // ȡУ���

			if ((Cfun.accSum(s_buf, s_buf.length - 1)).equals(Cfun
					.Byte2x16Str(acc))) {
				return Cfun.Byte2x16Str(acc).toLowerCase(); // ����СдУ���
			} else {
				return null; // ʧ�ܷ��ؿ��ַ�
			}
		}
		return null;
	}

	/**
	 * ���
	 * 
	 * @param unpack
	 * @return
	 */
	public CqueueData decode(String unpack, String cmdType) {
		if (unpack.length() < 6) {
			return null;
		}
		String accSum = "";
		String str1 = "a101"; // ϵͳ��ʶ�͵�ַ���ñ�ʶ
		String str2 = "e1a1";
		String str3 = "b2a1";
		// CXmlData level1 = xml.GetAllAttr("*");
		CqueueData datas = new CqueueData();
		if (unpack.substring(2, 6).equals(str1)
				|| unpack.substring(2, 6).equals("a110")) { // �жϿ������ʶ
			accSum = apbUnpack(unpack); // У���,����У����
		} else {
			return null;
		}

		// if (accSum == null)
		// return null;

		datas.setSystemId(unpack.substring(2, 4)); // ϵͳ��ʶ
		datas.setAddressEn(unpack.substring(4, 6)); // ��ַ���ñ�ʶ

		// �ж���e1������b2��
		if (str2.equals(unpack.substring(6, 10))) {
			datas.setTargetType(unpack.substring(6, 8));
			datas.setSourceType(unpack.substring(8, 10));
		} else if (str3.equals(unpack.substring(6, 10))) {
			datas.setTargetType(unpack.substring(6, 8));
			datas.setSourceType(unpack.substring(8, 10));
		} else {
			return null;
		}

		datas.setSourceAddr(unpack.substring(10, 20)); // ȡ��5BCDͨѶ��
		int len = Integer.parseInt(unpack.substring(20, 22), 16) * 2;
		datas.setDataLength(len); // �����򳤶�
		if (unpack.substring(22, 24).equals("68")) {
			datas.setSyncSymbol(unpack.substring(22, 24)); // ͬ����
		} else {
			return null;
		}

		// String cmdType = unpack.substring(24, 26);// �����������
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
		if (cmdType.equals("ff") || cmdType.equals("01")) {// ��������
			if (unpack.length() > 33) {
				int sumUseGas = Integer.parseInt(unpack.substring(26, 34), 10); // �ۼ�������
				datas.setSumUseGas(sumUseGas);// �ۼ�������
				data1 = unpack.substring(26, 34) + ";";
			}
			if (unpack.length() > 39) {
				datas.setMeterState(unpack.substring(34, 38));// ���״̬
				data1 += unpack.substring(34, 38) + ";"
						+ unpack.substring(38, 40);
			}
			if (unpack.length() > 87) { // �ɳ��򲻻᷵����������
				int dBm = Integer.parseInt(unpack.substring(44, 46), 16); // �����ź�ǿ��
				datas.setSignalStrength(dBm); // �ź�ǿ��
				int elec = Integer.parseInt(unpack.substring(46, 48), 16); // ����
				datas.setPowerState(elec);// �����,�ٷֱ�
				data1 += ";|-" + Integer.toString(155 - dBm) + ";"
						+ Integer.toString(elec) + ";"
						+ unpack.substring(48, 50) + ";"
						+ unpack.substring(50, 58) + ";"
						+ unpack.substring(58, 68) + ";"
						+ unpack.substring(68, 88);
			}
		} else if (cmdType.equals("10")) { // ��ߺţ�����ţ�ģ���
			data1 = unpack.substring(26, 36) + ";" + unpack.substring(36, 46)
					+ ";" + unpack.substring(46, 56);
		} else if (cmdType.equals("15")) { // ���߷��Ͳ���ָ��
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 38)
					+ ";" + unpack.substring(38, 40);
		} else if (cmdType.equals("20")) { // ��ȡ�ۼƶ�����ָ��20
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 38)
					+ ";" + unpack.substring(38, 40) + ";"
					+ unpack.substring(40, 50);
		} else if (cmdType.equals("22")) { // ��ѯ����ʱ��ָ��22
			data1 = unpack.substring(26, 28) + ";" + unpack.substring(28, 30)
					+ ";" + unpack.substring(30, 32) + ";"
					+ unpack.substring(32, 34);
		} else if (cmdType.equals("30")) { // ���·ݳ�ȡ�ۼƶ�����ָ��30
			data1 = unpack.substring(26, 30) + ";" + unpack.substring(30, 32)
					+ ";" + unpack.substring(32, 40) + ";"
					+ unpack.substring(40, 50) + ";" + unpack.substring(40, 60);
		} else {
			data1 += unpack.substring(26, 26 + (len - 2));
		}
		datas.setDataBCD(data1); // �������

		return datas;
	}

	public CqueueData decodeHuiZhou(String unpack) {
		// ����
		CqueueData datas;
		try {
			byte[] decryptResult = decrypt(
					parseHexStr2Byte(unpack.substring(6, unpack.length() - 4)),
					password); // ȥ��ͷβ֡
			String revsString = parseByte2HexStr(decryptResult);
			datas = new CqueueData();
			datas.setSourceAddr(revsString.substring(0, 10));// ͨѶ��
			String cmdType = revsString.substring(10, 12);
			datas.setCmdType(cmdType);// ��������
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
				datas.setSumUseGas(Double.parseDouble(sumUseGas));// �ۼ�������
				datas.setMeterState(revsString.substring(34, 38));// ���״̬
			} else {
				datas.setDataBCD(revsString.substring(12));
			}
		} catch (NumberFormatException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			datas = null;
		}
		return datas;
	}

	public CqueueData decodeShangHai(String msg) {
		CqueueData datas = new CqueueData();
		datas.setSourceAddr(msg.substring(0, 10)); // ͨѶ��
		datas.setCmdType(msg.substring(10, 14)); // ����+������
		datas.setDataBCD(msg.substring(14, msg.length() - 2)); // ������
		return datas;
	}

	/**
	 * ���
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
		String str1 = "a101"; // ϵͳ��ʶ�͵�ַ���ñ�ʶ
		String str2 = "e1a1";
		String str3 = "b2a1";
		// CXmlData level1 = xml.GetAllAttr("*");
		CqueueData datas = new CqueueData();
		if (unpack.substring(2, 6).equals(str1)
				|| unpack.substring(2, 6).equals("a110")) { // �жϿ������ʶ
			accSum = apbUnpack(unpack); // У���,����У����
		} else {
			return null;
		}

		if (accSum == null)
			return null;

		datas.setSystemId(unpack.substring(2, 4)); // ϵͳ��ʶ
		datas.setAddressEn(unpack.substring(4, 6)); // ��ַ���ñ�ʶ

		// �ж���e1������b2��
		if (str2.equals(unpack.substring(6, 10))) {
			datas.setTargetType(unpack.substring(6, 8));
			datas.setSourceType(unpack.substring(8, 10));
		} else if (str3.equals(unpack.substring(6, 10))) {
			datas.setTargetType(unpack.substring(6, 8));
			datas.setSourceType(unpack.substring(8, 10));
		} else {
			return null;
		}

		datas.setSourceAddr(unpack.substring(10, 20)); // ȡ��5BCDͨѶ��
		int len = Integer.parseInt(unpack.substring(20, 22), 16) * 2;
		datas.setDataLength(len); // �����򳤶�
		if (unpack.substring(22, 24).equals("68")) {
			datas.setSyncSymbol(unpack.substring(22, 24)); // ͬ����
		} else {
			return null;
		}
		/*
		 * String cmdQ=""; String cmdS=""; for (int i = 0; level1.ChangeTo(i +
		 * 1); i++) { if
		 * ((level1.GetAttr(".").substring(1)).equals(packet.substring(22, 24)))
		 * { cmdQ = level1.GetAttr("cmdQuery"); // ����������� cmdS =
		 * level1.GetAttr("cmdSet"); // ����������� } } if(packet.substring(22,
		 * 24).equals(cmdQ)||packet.substring(22, 24).equals(cmdS)){
		 * d.setCmdType(packet.substring(22, 24)); // ����������� }
		 */
		String cmdType = unpack.substring(24, 26);// �����������
		datas.setCmdType(cmdType);
		String data1 = "";
		if (cmdType.equals("ff") || cmdType.equals("01")) {// ��������
			int sumUseGas = Integer.parseInt(unpack.substring(26, 34), 10); // �ۼ�������
			datas.setSumUseGas(sumUseGas);// �ۼ�������
			datas.setMeterState(unpack.substring(34, 38));// ���״̬
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 38)
					+ ";" + unpack.substring(38, 40);
			int dBm = Integer.parseInt(unpack.substring(44, 46), 16); // �����ź�ǿ��
			datas.setSignalStrength(dBm); // �ź�ǿ��
			int elec = Integer.parseInt(unpack.substring(46, 48), 16); // ����
			datas.setPowerState(elec);// �����,�ٷֱ�
			data1 += ";|-" + Integer.toString(155 - dBm) + ";"
					+ Integer.toString(elec) + ";" + unpack.substring(48, 50)
					+ ";" + unpack.substring(50, 58) + ";"
					+ unpack.substring(58, 68) + ";" + unpack.substring(68, 88);
		} else if (cmdType.equals("10")) { // ��ߺţ�����ţ�ģ���
			data1 = unpack.substring(26, 36) + ";" + unpack.substring(36, 46)
					+ ";" + unpack.substring(46, 56);
		} else if (cmdType.equals("15")) { // ���߷��Ͳ���ָ��
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 38)
					+ ";" + unpack.substring(38, 40);
		} else if (cmdType.equals("20")) { // ��ȡ�ۼƶ�����ָ��20
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 38)
					+ ";" + unpack.substring(38, 40) + ";"
					+ unpack.substring(40, 50);
		} else if (cmdType.equals("22")) { // ��ѯ����ʱ��ָ��22
			data1 = unpack.substring(26, 28) + ";" + unpack.substring(28, 30)
					+ ";" + unpack.substring(30, 32) + ";"
					+ unpack.substring(32, 34);
		} else if (cmdType.equals("30")) { // ���·ݳ�ȡ�ۼƶ�����ָ��30
			data1 = unpack.substring(26, 30) + ";" + unpack.substring(30, 32)
					+ ";" + unpack.substring(32, 40) + ";"
					+ unpack.substring(40, 50) + ";" + unpack.substring(40, 60);
		} else {
			data1 += unpack.substring(26, 26 + (len - 2));
		}
		datas.setDataBCD(data1); // �������

		return datas;
	}

	/**
	 * ��������Ӧ�������򳤶�
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
	 * ���
	 * 
	 * @param data
	 * @param x
	 * @return
	 */
	public String encodeIC(CqueueData data) {
		String packet = "";
		String all = "";
		String frameStart = "68"; // ֡��ʼ��
		String syncSymbol = "68"; // ͬ����
		String frameEnd = "16"; // ֡������
		String cmd = data.getCmdType();

		if (cmd.equals("01")) { // �ӽ��洫����������
			all += "a200a1e1";
			all += "";
		} else {
			all += "a210a1e1";
			all += data.getTargetAddr(); // ����Ŀ���ַ-->5BCDͨѶ��
		}
		if (data.isSet() == true) {
			all += getDataLengthIC(cmd); // �����򳤶�(����)
		} else {
			all += "02";// ��ѯ �����򳤶�(��ѯ)
		}
		all += syncSymbol; // ͬ����
		String cmdData = "";
		if (data.isSet() == true) {
			if (cmd.isEmpty()) {
				return "noSet"; // �������ò���
			} else {
				cmdData += cmd; // ��������-->����
			}
		} else {
			if (cmd.isEmpty()) {
				return "noQuery"; // ���ǲ�ѯ����
			} else {
				cmdData += cmd; // ��������-->��ѯ
			}
		}
		int dataLen = Cfun.parseToInt(getDataLengthIC(cmd), 0, 16) * 2;
		if (data.isSet() == true) {
			if (data.getDataBCD().length() == (dataLen - 2)) {
				cmdData += data.getDataBCD(); // ����5BCD
			} else {
				return "lenError"; // ����趨���ݳ��Ȳ��ԣ��򷵻�"lenError"
			}
		} else {
			// cmdData += "ff"; // ��ѯĬ������Ϊ"FF" // ����/�ط�ʹ�ò�ѯ"FF"
			cmdData += "38";
		}
		// String checkSum = Cfun.accSum(Cfun.x16Str2Byte(cmdData));// �ۼӺ�У�飬1�ֽ�
		String checkSum = "00";
		packet += frameStart; // ֡��ʼ�����̶�Ϊ0x68
		packet += all;
		packet += cmdData;
		packet += checkSum.toLowerCase();
		packet += frameEnd; // ������־���̶�Ϊ0x16
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
	 * ���
	 * 
	 * @param unpack
	 * @return
	 */
	public CqueueData decodeIC(String unpack) {
		// String accSum = "";
		String str1 = "a201"; // ϵͳ��ʶ�͵�ַ���ñ�ʶ
		String str2 = "e1a1";

		CqueueData datas = new CqueueData();
		if (unpack.substring(2, 6).equals(str1)) { // �жϿ������ʶ
			// accSum = apbUnpack(unpack); // У���,����У����
		} else {
			return null;
		}

		// if (accSum == null)
		// return null;

		datas.setSystemId(unpack.substring(2, 4)); // ϵͳ��ʶ
		datas.setAddressEn(unpack.substring(4, 6)); // ��ַ���ñ�ʶ

		// �ж��Ƿ���e1a1���ذ�
		if (str2.equals(unpack.substring(6, 10))) {
			datas.setTargetType(unpack.substring(6, 8));
			datas.setSourceType(unpack.substring(8, 10));
		} else {
			return null;
		}

		datas.setSourceAddr(unpack.substring(10, 20)); // ȡ��5BCDͨѶ��
		int len = Integer.parseInt(unpack.substring(20, 22), 16) * 2;
		datas.setDataLength(len); // �����򳤶�
		if (unpack.substring(22, 24).equals("68")) {
			datas.setSyncSymbol(unpack.substring(22, 24)); // ͬ����
		} else {
			return null;
		}

		String cmdType = unpack.substring(24, 26);// �����������
		datas.setCmdType(cmdType);
		String data1 = "";
		if (cmdType.equals("06")) {// ��ȡ����
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 42)
					+ ";" + unpack.substring(42, 50) + ";"
					+ unpack.substring(50, 54) + ";" + unpack.substring(54, 56)
					+ ";" + unpack.substring(56, 58) + ";"
					+ unpack.substring(58, 60) + ";" + unpack.substring(60, 66)
					+ ";" + unpack.substring(66, 72) + ";"
					+ unpack.substring(72, 78);
			// + ";" + unpack.substring(78, 84) + ";" + unpack.substring(84,
			// 90); 3BCD�������ۼ�����3BCD���������ۼ���
			// data1 += ";|" + unpack.substring(94, 100) + ";"
			// + unpack.substring(100, 110) + ";"
			// + unpack.substring(110, 120) + ";"
			// + unpack.substring(120, 126);
			data1 += ";|" + unpack.substring(82, 88) + ";"
					+ unpack.substring(88, 98) + ";"
					+ unpack.substring(98, 108) + ";"
					+ unpack.substring(108, 114);
		} else if (cmdType.equals("08")) { // ��ѯ���ʲ���
			data1 = unpack.substring(26, 32) + ";" + unpack.substring(32, 38)
					+ ";" + unpack.substring(38, 44) + ";"
					+ unpack.substring(44, 50) + ";" + unpack.substring(50, 56)
					+ ";" + unpack.substring(56, 62);
		} else if (cmdType.equals("18")) { // ��ѯ�������ڲ���
			data1 = unpack.substring(26, 28) + ";" + unpack.substring(28, 30)
					+ ";" + unpack.substring(30, 36) + ";"
					+ unpack.substring(36, 38);
			data1 += ";|" + unpack.substring(38, 48) + ";"
					+ unpack.substring(48, 52) + ";" + unpack.substring(52, 58)
					+ ";" + unpack.substring(58, 64) + ";"
					+ unpack.substring(64, 70) + ";" + unpack.substring(70, 76)
					+ ";" + unpack.substring(76, 82) + ";"
					+ unpack.substring(82, 88) + ";" + unpack.substring(88, 90);
		} else if (cmdType.equals("19")) { // ���÷������ڲ���
			data1 = unpack.substring(26, 28);
			data1 += ";|" + unpack.substring(28, 30);
		} else if (cmdType.equals("20") || cmdType.equals("21")) { // ���߳�ֵ/�˿�Э��
			data1 = unpack.substring(26, 34) + ";" + unpack.substring(34, 42);
		} else {
			data1 += unpack.substring(26, 26 + (len - 2));
		}
		datas.setDataBCD(data1); // �������

		return datas;
	}

	/**
	 * ���
	 * 
	 * @param data
	 * @param x
	 * @return
	 */
	public String encodeIC(CqueueData data, CXmlAnalyze x) {
		xml = x;
		String packet = "";
		String all = "";
		String frameStart = "68"; // ֡��ʼ��
		String syncSymbol = "68"; // ͬ����
		String frameEnd = "16"; // ֡������
		CXmlData xmlData = xml.GetAllAttr(data.getMname());
		// CXmlData dataAddr = xml.GetTheAttr("targetAddr", a.GetMname());
		// CXmlData dataCmd = xml.GetTheAttr("cmd", a.GetMname());
		if (xmlData.GetAttr("set").equals("01")) {
			// if (data.getCmdType("cmdSet").equals("04")) { //�ӽ��洫����������
			all += "a200a1e1";
			all += "";
		} else {
			all += "a210a1e1";
			all += data.getTargetAddr(); // ����Ŀ���ַ-->5BCDͨѶ��
		}
		if (data.isSet() == true) {
			all += xmlData.GetAttr("dataLength1"); // �����򳤶�(����)
		} else {
			all += "02";// ��ѯ �����򳤶�(��ѯ)
		}
		all += syncSymbol; // ͬ����
		String cmdData = "";
		if (data.isSet() == true) {
			if (xmlData.GetAttr("set").isEmpty()) {
				return "noSet"; // �������ò���
			} else {
				cmdData = xmlData.GetAttr("set"); // ��������-->����
			}
		} else {
			if (xmlData.GetAttr("query").isEmpty()) {
				return "noQuery"; // ���ǲ�ѯ����
			} else {
				cmdData = xmlData.GetAttr("query"); // ��������-->��ѯ
			}
		}
		int dataLen = Cfun.parseToInt(xmlData.GetAttr("dataLength1"), 0, 16) * 2;
		if (data.isSet() == true) {
			if (data.getDataBCD().length() == (dataLen - 2)) {
				cmdData += data.getDataBCD(); // ����5BCD
			} else {
				return ""; // ����趨���ݳ��Ȳ��ԣ��򷵻�""
			}
		} else {
			cmdData += "ff"; // ��ѯĬ������Ϊ"FF" // ����/�ط�ʹ�ò�ѯ"FF"
		}
		String checkSum = Cfun.accSum(Cfun.x16Str2Byte(cmdData));// �ۼӺ�У�飬1�ֽ�
		packet += frameStart; // ֡��ʼ�����̶�Ϊ0x68
		packet += all;
		packet += cmdData;
		packet += checkSum;
		packet += frameEnd; // ������־���̶�Ϊ0x16
		// packArray.add(packet);
		return packet;
	}

	/**
	 * ���
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
		if (unpack.substring(2, 6).equals(str1)) { // �жϿ������ʶ
			packet = apbUnpack(unpack); // У�鲢ȡ�øɾ��İ�,��ȥ����ͷ�Ͱ�β�Լ�У��
		} else {
			return null;
		}

		if (packet == null)
			return null;

		datas.setSystemId(packet.substring(0, 2)); // ϵͳ��ʶ
		datas.setAddressEn(packet.substring(2, 4)); // ��ַ���ñ�ʶ

		// �ж���E1������b2��
		if (str2.equals(packet.substring(4, 8))) {
			datas.setTargetType(packet.substring(4, 6));
			datas.setSourceType(packet.substring(6, 8));
		} else {
			return null;
		}

		datas.setSourceAddr(packet.substring(8, 18)); // ȡ��5BCDͨѶ��
		int len = Integer.parseInt(packet.substring(18, 20)) * 2;
		datas.setDataLength(len); // �����򳤶�
		if (packet.substring(20, 22).equals("68")) {
			datas.setSyncSymbol(packet.substring(20, 22)); // ͬ����
		} else {
			return null;
		}
		/*
		 * String cmdQ=""; String cmdS=""; for (int i = 0; level1.ChangeTo(i +
		 * 1); i++) { if
		 * ((level1.GetAttr(".").substring(1)).equals(packet.substring(22, 24)))
		 * { cmdQ = level1.GetAttr("cmdQuery"); // ����������� cmdS =
		 * level1.GetAttr("cmdSet"); // ����������� } } if(packet.substring(22,
		 * 24).equals(cmdQ)||packet.substring(22, 24).equals(cmdS)){
		 * d.setCmdType(packet.substring(22, 24)); // ����������� }
		 */
		datas.setCmdType(packet.substring(22, 24)); // �����������
		datas.setDataBCD(packet.substring(24, 24 + (len - 2))); // �������

		return datas;
	}
}
