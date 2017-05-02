package com.pl.protocol;

import com.pl.common.Cfun;

public class CProtocol {

	private int m_ProtocolType;

	public CProtocol(int iProtocolType) {
		m_ProtocolType = iProtocolType;
	}

	/**
	 * 解包时使用，根据数据类型，将数据序列转为String中间过程值Value 例如
	 * //e.g.:0X65,0X66,0x31,0x32,0X70->"AB123F"
	 * 
	 * @param DataArray
	 *            String 要转换的数据值
	 * @param iDataType
	 *            int iDataType值
	 * @param iLen
	 *            int 长度
	 * @param iRate
	 *            int 倍数
	 * @return String 转换后的界面值
	 **/
	public static String DeArray2Value(String DataArray, int iDataType,
			int iLen, int iRate) {
		String sMidValue = "";
		long iValue = 0, iMax = 0x1;
		double dbValue = 0;
		byte[] byteArray = Cfun.Str2ByteA(DataArray);

		switch (iDataType) {
		case CPublic.DATA_TYPE_UNSIGN:

			for (int i = 0; i < iLen; i++) {
				iValue = iValue << 8;
				iValue += byteArray[iLen - i - 1] & 0xff;
			}

			dbValue = (iValue * 1.0) / iRate;
			sMidValue = String.format("%.4f", dbValue);
			sMidValue = subZeroAndDot(sMidValue);
			break;
		case CPublic.DATA_TYPE_SIGNED:
			for (int i = 0; i < iLen; i++) {
				iMax *= 0x100;
				iValue = iValue << 8;
				iValue += byteArray[iLen - i - 1] & 0xff;
			}
			if (iValue >= (iMax / 2)) {
				iValue = iValue - iMax;
			}
			dbValue = (iValue * 1.0) / iRate;
			sMidValue = String.format("%.4f", dbValue);
			sMidValue = subZeroAndDot(sMidValue);
			break;
		case CPublic.DATA_TYPE_STRING:// '[31,31,31,00]' -> "111"
			// byteArray '[31,31,31,00]' -> [31,31,31,00]
			int i = 0;
			for (i = 0; i < byteArray.length; ++i)// 找到 00
			{
				if (byteArray[i] == 0)
					break;
			}
			byte[] temp2 = new byte[i];
			System.arraycopy(byteArray, 0, temp2, 0, temp2.length);// [31,31,31,00]
																	// ->
																	// [31,31,31]
			sMidValue = Cfun.getStr(temp2);// [31,31,31] -> "111"
			break;
		default:
			sMidValue = Cfun.Str2x16Str(DataArray);
			break;

		}
		return sMidValue;
	}

	/**
	 * 解包时使用，根据显示类型，，将中间过程值Value转为界面显示值 例如 //"01"显示为界面上的"打开"，"02"显示为界面上的"关闭"
	 * 
	 * @param sMidValue
	 *            String 要转换的数据值
	 * @param iShowType
	 *            int edittype值
	 * @param sSection
	 *            String 选项，这个不知道如何使用
	 * @return String 转换后的界面值
	 **/
	public static String DeValue2Show(String sMidValue, int iShowType,
			String sSection) {
		String sShowValue = "";
		sShowValue = sMidValue;
		switch (iShowType) {
		case CPublic.SHOW_TYPE_DERECT:
			break;
		case CPublic.SHOW_TYPE_COMBO:
			break;
		default:
			break;
		}
		return sShowValue;
	}

	/**
	 * 组包时使用，根据显示类型，将显示值转为中间过程值 例如 //e.g.:"打开"->0X01
	 * 
	 * @param sShowValue
	 *            String 要转换的界面值
	 * @param iShowType
	 *            int edittype值
	 * @param sSection
	 *            String 选项，这个不知道如何使用
	 * @return String 转换后的值
	 * 
	 **/
	public static String EnShow2Value(String sShowValue, int iShowType,
			String sSection) {
		String sMidValue = "";
		sMidValue = sShowValue;
		switch (iShowType) {
		case CPublic.SHOW_TYPE_DERECT:
			break;
		case CPublic.SHOW_TYPE_COMBO:
			break;
		default:
			break;
		}
		return sMidValue;
	}

	/**
	 * 组包时使用，根据数据类型，将中间过程值转为数据序列 例如 //e.g.:"AB123F"->0X65,0X66,0x31,0x32,0X70
	 * 
	 * @param sMidValue
	 *            String 要转换的数据值
	 * @param iDataType
	 *            int edittype值
	 * @param iLen
	 *            int 长度
	 * @param iRate
	 *            int 倍数
	 * @return String 转换后的界面值
	 **/
	public static String EnValue2Array(String sMidValue, int iDataType,
			int iLen, int iRate) {
		String DataArray = "";
		byte[] byteArray = new byte[iLen];// iLen
		long iValue = 0;
		double dbValue = 0;
		switch (iDataType) {
		case CPublic.DATA_TYPE_UNSIGN:// unsign digital ,low byte first
										// //e.g.:"3221.451" * 100
										// ->0x61,0xea,0x04,0x00
		case CPublic.DATA_TYPE_SIGNED:// signed digital ,low byte first
										// //e.g.:"-3221.451" * 100
										// ->0x9f,0x15,0xfb,0xff
			if (sMidValue == null)
				sMidValue = "0";
			dbValue = Float.parseFloat(sMidValue) * iRate;
			iValue = (long) dbValue;
			for (int i = 0; i < iLen; i++) {
				// DataArray += (byte) (iValue & 0xff); //byteArray[i]
				byteArray[i] = (byte) (iValue & 0xff);
				iValue = iValue >> 8;
			}
			DataArray = Cfun.getStr(byteArray);
			break;
		case CPublic.DATA_TYPE_STRING:// "111" -> [31,31,31,00]
			if (sMidValue == null)
				sMidValue = "";
			byte[] temp = Cfun.Str2ByteA(sMidValue);// "111" -> [31,31,31]
			System.arraycopy(temp, 0, byteArray, 0, temp.length);// [31,31,31]
																	// ->
																	// [31,31,31,00]
			DataArray = Cfun.getStr(byteArray);// byte[] -> String
			break;
		default:
			if (sMidValue == null)
				sMidValue = "";
			sMidValue += "00000000000000000000000000";
			DataArray = Cfun.x16Str2Str(sMidValue).substring(0, iLen);
		}
		return DataArray;
	}

	/**
	 * 使用java正则表达式去掉多余的.与0
	 * 
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}

	/*******************************************************************************
	 ** 函数名称:calculateCRC 功能描述:CRC计算 输　入: ptr:指向需要CRC计算的数组首地址 len:需要CRC计算的长度
	 ** 
	 ** 输　出: CRC计算结果
	 ** 
	 ** 全局变量: 调用模块:
	 *******************************************************************************/
	private static int[] crcTable = { 0x0000, 0x1021, 0x2042, 0x3063, 0x4084,
			0x50a5, 0x60c6, 0x70e7, 0x8108, 0x9129, 0xa14a, 0xb16b, 0xc18c,
			0xd1ad, 0xe1ce, 0xf1ef, 0x1231, 0x0210, 0x3273, 0x2252, 0x52b5,
			0x4294, 0x72f7, 0x62d6, 0x9339, 0x8318, 0xb37b, 0xa35a, 0xd3bd,
			0xc39c, 0xf3ff, 0xe3de, 0x2462, 0x3443, 0x0420, 0x1401, 0x64e6,
			0x74c7, 0x44a4, 0x5485, 0xa56a, 0xb54b, 0x8528, 0x9509, 0xe5ee,
			0xf5cf, 0xc5ac, 0xd58d, 0x3653, 0x2672, 0x1611, 0x0630, 0x76d7,
			0x66f6, 0x5695, 0x46b4, 0xb75b, 0xa77a, 0x9719, 0x8738, 0xf7df,
			0xe7fe, 0xd79d, 0xc7bc, 0x48c4, 0x58e5, 0x6886, 0x78a7, 0x0840,
			0x1861, 0x2802, 0x3823, 0xc9cc, 0xd9ed, 0xe98e, 0xf9af, 0x8948,
			0x9969, 0xa90a, 0xb92b, 0x5af5, 0x4ad4, 0x7ab7, 0x6a96, 0x1a71,
			0x0a50, 0x3a33, 0x2a12, 0xdbfd, 0xcbdc, 0xfbbf, 0xeb9e, 0x9b79,
			0x8b58, 0xbb3b, 0xab1a, 0x6ca6, 0x7c87, 0x4ce4, 0x5cc5, 0x2c22,
			0x3c03, 0x0c60, 0x1c41, 0xedae, 0xfd8f, 0xcdec, 0xddcd, 0xad2a,
			0xbd0b, 0x8d68, 0x9d49, 0x7e97, 0x6eb6, 0x5ed5, 0x4ef4, 0x3e13,
			0x2e32, 0x1e51, 0x0e70, 0xff9f, 0xefbe, 0xdfdd, 0xcffc, 0xbf1b,
			0xaf3a, 0x9f59, 0x8f78, 0x9188, 0x81a9, 0xb1ca, 0xa1eb, 0xd10c,
			0xc12d, 0xf14e, 0xe16f, 0x1080, 0x00a1, 0x30c2, 0x20e3, 0x5004,
			0x4025, 0x7046, 0x6067, 0x83b9, 0x9398, 0xa3fb, 0xb3da, 0xc33d,
			0xd31c, 0xe37f, 0xf35e, 0x02b1, 0x1290, 0x22f3, 0x32d2, 0x4235,
			0x5214, 0x6277, 0x7256, 0xb5ea, 0xa5cb, 0x95a8, 0x8589, 0xf56e,
			0xe54f, 0xd52c, 0xc50d, 0x34e2, 0x24c3, 0x14a0, 0x0481, 0x7466,
			0x6447, 0x5424, 0x4405, 0xa7db, 0xb7fa, 0x8799, 0x97b8, 0xe75f,
			0xf77e, 0xc71d, 0xd73c, 0x26d3, 0x36f2, 0x0691, 0x16b0, 0x6657,
			0x7676, 0x4615, 0x5634, 0xd94c, 0xc96d, 0xf90e, 0xe92f, 0x99c8,
			0x89e9, 0xb98a, 0xa9ab, 0x5844, 0x4865, 0x7806, 0x6827, 0x18c0,
			0x08e1, 0x3882, 0x28a3, 0xcb7d, 0xdb5c, 0xeb3f, 0xfb1e, 0x8bf9,
			0x9bd8, 0xabbb, 0xbb9a, 0x4a75, 0x5a54, 0x6a37, 0x7a16, 0x0af1,
			0x1ad0, 0x2ab3, 0x3a92, 0xfd2e, 0xed0f, 0xdd6c, 0xcd4d, 0xbdaa,
			0xad8b, 0x9de8, 0x8dc9, 0x7c26, 0x6c07, 0x5c64, 0x4c45, 0x3ca2,
			0x2c83, 0x1ce0, 0x0cc1, 0xef1f, 0xff3e, 0xcf5d, 0xdf7c, 0xaf9b,
			0xbfba, 0x8fd9, 0x9ff8, 0x6e17, 0x7e36, 0x4e55, 0x5e74, 0x2e93,
			0x3eb2, 0x0ed1, 0x1ef0 };

	// 返回值:CRC16校验
	public static int CalCrc16(byte[] sPack, int len) {
		int crc = 0;
		int temp;
		for (int i = 0; i < len; i++) {
			temp = ((crc / 256) ^ (sPack[i] & 0xFF));
			crc <<= 8; // 左移8位
			crc &= 0xFFFF;
			crc ^= crcTable[temp]; // 高8位和当前字节相加后再查表求CRC ，再加上以前的CRC
		}
		return (crc);
	}

}
