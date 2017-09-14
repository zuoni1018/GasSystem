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

	// charתbyte

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

	public static byte[] x16Str2Byte(String s) {// 16�����ַ�����byte
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

	public static String x16Str2Str(String s) {// 16�����ַ�����byte�ַ���
		return getStr(x16Str2Byte(s));
	}

	/**
	 * byte��16�����ַ���
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

	public static String Str2x16Str(String s) {// byte�ַ�����16�����ַ���
		return Byte2x16Str(Str2ByteA(s));//
	}

	public static byte[] Reve(byte[] bys) {// ��ת,����ı�ԭֵ [] []
		for (int i = 0, j = bys.length - 1; i < j; ++i, --j) {
			bys[i] = (byte) (bys[i] ^ bys[j]);
			bys[j] = (byte) (bys[i] ^ bys[j]);
			bys[i] = (byte) (bys[i] ^ bys[j]);
		}
		return bys;
	}

	/**
	 * ��ת,������ı�ԭֵ,'[1,2]' ->'[2,1]'
	 * 
	 * @param s
	 *            String ��Ҫ��ת���ַ��� '[ ]'
	 * @return String ��ת����ַ��� '[ ]'
	 */
	public static String Reve(String s) {// ��ת,������ı�ԭֵ
		return getStr(Reve(Str2ByteA(s)));
	}

	/**
	 * ��ת,������ı�ԭֵ, 0102 ->0201
	 * 
	 * @param s
	 *            String ��Ҫ��ת���ַ��� '[ ]'
	 * @return String ��ת����ַ��� '[ ]'
	 */
	public static String x16Reve(String s) {// ��ת,������ı�ԭֵ
		return Byte2x16Str(Reve(x16Str2Byte(s)));
	}

	// ���
	/**
	 * ��һ���ַ���ת��Ϊint
	 * 
	 * @param s
	 *            String Ҫת�����ַ���
	 * @param defaultInt
	 *            int ��������쳣,Ĭ�Ϸ��ص�����
	 * @param radix
	 *            int Ҫת�����ַ�����ʲô���Ƶ�,��16 8 10.
	 * @return int ת���������
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
	 * ʮ����ת��Ϊʮ�������ַ���
	 * 
	 * @param algorism
	 *            int ʮ���Ƶ�����
	 * @return String ��Ӧ��ʮ�������ַ���
	 */
	public static String algorismToHEXString(int algorism) {
		String result = "";
		result = Integer.toHexString(algorism);

		result = patchHexString(result, 4);

		return result.toUpperCase();
	}

	/**
	 * HEX�ַ���ǰ��0����Ҫ���ڳ���λ�����㡣
	 * 
	 * @param str
	 *            String ��Ҫ���䳤�ȵ�ʮ�������ַ���
	 * @param maxLength
	 *            int �����ʮ�������ַ����ĳ���
	 * @return ������
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
	 * ��ʮ����ת��Ϊָ�����ȵ�ʮ�������ַ���
	 * 
	 * @param algorism
	 *            int ʮ��������
	 * @param maxLength
	 *            int ת�����ʮ�������ַ�������
	 * @return String ת�����ʮ�������ַ���
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
	 * ʮ�����ƴ�ת��Ϊbyte����
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
	 * ����
	 */
	public static String Convert(String s) {// ʮ�������ַ�������
		return Byte2x16Str(Reve(hex2byte(s)));
	}

	/**
	 * �����ͣ�BCC У���㷨 (java)
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
	 * ����У��
	 * 
	 * @param str
	 */
	public static String xorSum(String str) {
		// strΪ����У����ַ���
		// ����͵ĸ���һ��������8bit���ȵ��ַ�����
		// ��У����������㣬��Ҫ��ǿ�ư��ַ�ת������������
		String check = "";
		char ch = str.charAt(0);
		int x = ch;
		int y;
		for (int i = 1; i < str.length(); i++) {
			y = str.charAt(i);
			x = x ^ y;
		}
		// x��ΪУ��ͣ����潫��ת����ʮ��������ʽ
		check = Integer.toHexString(x);
		check += check.toUpperCase();
		return check;
	}

	/**
	 * �ۼӺ�У��
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
