package com.pl.common;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class Cfun {
	private Cfun() {
	}

	public static char[] getChars(byte[] bytes) {
		Charset cs = Charset.forName("ASCII");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);

		return cb.array();
	}

	// char转byte

	public static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("ASCII");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		return bb.array();
	}

	public static String getStr(byte[] b) {
		try {
			return new String(b, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] Str2ByteA(String s) {
		try {
			return s.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public static String StrAddByte(String a, byte b) {
		byte[] by = Str2ByteA(a);
		ByteBuffer bb = ByteBuffer.allocate(by.length + 1);
		bb.put(by);
		bb.put(b);
		return getStr(bb.array());
	}

	public static String StrAddByte(String a, byte[] _bys) {
		byte[] bys = Str2ByteA(a);
		ByteBuffer bb = ByteBuffer.allocate(bys.length + _bys.length);
		bb.put(bys);
		bb.put(_bys);
		return getStr(bb.array());
	}

	public static byte[] x16Str2Byte(String s) {// 16进制字符串到byte
		if (s == null)
			return null;
		int length = s.length() / 2;
		ByteBuffer bb = ByteBuffer.allocate(length);
		for (int i = 0; i < length; ++i) {
			bb.put((byte) Integer.parseInt(s.substring(i << 1, (i << 1) + 2),
					16));
		}
		return bb.array();
	}

	public static String x16Str2Str(String s) {// 16进制字符串到byte字符串
		return getStr(x16Str2Byte(s));
	}

	/**
	 * byte到16进制字符串
	 * 
	 * @param bys
	 * @return
	 */
	public static String Byte2x16Str(byte[] bys) {
		String str = "";
		String temp = "";
		for (int i = 0; i < bys.length; ++i) {
			temp = String.format("%x", bys[i]);
			if (temp.length() % 2 == 1) {
				temp = "0" + temp;
			}
			str += temp;
		}
		return str.toUpperCase();
	}

	public static String Str2x16Str(String s) {// byte字符串到16进制字符串
		return Byte2x16Str(Str2ByteA(s));//
	}

	public static byte[] Reve(byte[] bys) {// 反转,（会改变原值 [] []
		for (int i = 0, j = bys.length - 1; i < j; ++i, --j) {
			bys[i] = (byte) (bys[i] ^ bys[j]);
			bys[j] = (byte) (bys[i] ^ bys[j]);
			bys[i] = (byte) (bys[i] ^ bys[j]);
		}
		return bys;
	}

	/**
	 * 反转,（不会改变原值,'[1,2]' ->'[2,1]'
	 * 
	 * @param s
	 *            String 需要反转的字符串 '[ ]'
	 * @return String 反转后的字符串 '[ ]'
	 */
	public static String Reve(String s) {// 反转,（不会改变原值
		return getStr(Reve(Str2ByteA(s)));
	}

	/**
	 * 反转,（不会改变原值, 0102 ->0201
	 * 
	 * @param s
	 *            String 需要反转的字符串 '[ ]'
	 * @return String 反转后的字符串 '[ ]'
	 */
	public static String x16Reve(String s) {// 反转,（不会改变原值
		return Byte2x16Str(Reve(x16Str2Byte(s)));
	}

	// 添加
	/**
	 * 将一个字符串转换为int
	 * 
	 * @param s
	 *            String 要转换的字符串
	 * @param defaultInt
	 *            int 如果出现异常,默认返回的数字
	 * @param radix
	 *            int 要转换的字符串是什么进制的,如16 8 10.
	 * @return int 转换后的数字
	 */
	public static int parseToInt(String s, int defaultInt, int radix) {
		int i = 0;
		try {
			i = Integer.parseInt(s, radix);
		} catch (NumberFormatException ex) {
			i = defaultInt;
		}
		return i;
	}

	/**
	 * 十进制转换为十六进制字符串
	 * 
	 * @param algorism
	 *            int 十进制的数字
	 * @return String 对应的十六进制字符串
	 */
	public static String algorismToHEXString(int algorism) {
		String result = "";
		result = Integer.toHexString(algorism);

		result = patchHexString(result, 4);

		return result.toUpperCase();
	}

	/**
	 * HEX字符串前补0，主要用于长度位数不足。
	 * 
	 * @param str
	 *            String 需要补充长度的十六进制字符串
	 * @param maxLength
	 *            int 补充后十六进制字符串的长度
	 * @return 补充结果
	 */
	static public String patchHexString(String str, int maxLength) {
		String temp = "";
		for (int i = 0; i < maxLength - str.length(); i++) {
			temp = "0" + temp;
		}
		str = (temp + str).substring(0, maxLength);
		return str;
	}

	/**
	 * 将十进制转换为指定长度的十六进制字符串
	 * 
	 * @param algorism
	 *            int 十进制数字
	 * @param maxLength
	 *            int 转换后的十六进制字符串长度
	 * @return String 转换后的十六进制字符串
	 */
	public static String ToHEXString(int algorism) {
		String result = "";
		result = Integer.toHexString(algorism);

		if (result.length() % 2 == 1) {
			result = "0" + result;
		}
		return result;
	}

	/**
	 * 十六进制串转化为byte数组
	 * 
	 * @return the array of byte
	 */
	public static final byte[] hex2byte(String hex)
			throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
			String swap = "" + arr[i++] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();
		}
		return b;
	}

	/**
	 * 倒序
	 */
	public static String Convert(String s) {// 十六进制字符串倒序
		return Byte2x16Str(Reve(hex2byte(s)));
	}

	/**
	 * （异或和）BCC 校验算法 (java)
	 */
	public static String getBCC(byte[] data) {

		String ret = "";
		byte BCC[] = new byte[1];
		for (int i = 0; i < data.length; i++) {
			BCC[0] = (byte) (BCC[0] ^ data[i]);
		}
		String hex = Integer.toHexString(BCC[0] & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		ret += hex.toUpperCase();
		return ret;
	}

	/**
	 * 异或和校验
	 * 
	 * @param str
	 */
	public static String xorSum(String str) {
		// str为参与校验的字符串
		// 检验和的概念一般体现在8bit长度的字符数组
		// 和校验是异或运算，需要先强制把字符转换成整形数据
		String check = "";
		char ch = str.charAt(0);
		int x = ch;
		int y;
		for (int i = 1; i < str.length(); i++) {
			y = str.charAt(i);
			x = x ^ y;
		}
		// x即为校验和，下面将其转换成十六进制形式
		check = Integer.toHexString(x);
		check += check.toUpperCase();
		return check;
	}

	/**
	 * 累加和校验
	 * 
	 * @param data
	 * @return
	 */
	public static String accSum(byte[] data) {
		String ret = "";
		byte acc[] = new byte[1];
		for (int i = 0; i < data.length; i++) {
			acc[0] = (byte) (acc[0] + data[i]);
		}
		String hex = Integer.toHexString(acc[0] & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		ret += hex.toUpperCase();
		return ret;
	}

	public static String accSum(byte[] data, int len) {
		String ret = "";
		byte acc[] = new byte[1];
		for (int i = 0; i < len; i++) {
			acc[0] = (byte) (acc[0] + data[i]);
		}
		String hex = Integer.toHexString(acc[0] & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		ret += hex.toUpperCase();
		return ret;
	}

	public static String transeferStr(String orgin, int len) {
		int poslength = len;
		int length = orgin.length();
		int remaining = poslength - length;
		if (remaining > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < remaining; i++) {
				sb.append(0);
			}
			sb.append(orgin);
			return sb.toString();
		}
		return orgin.toString();
	}
}
