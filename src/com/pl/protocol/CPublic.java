package com.pl.protocol;

public class CPublic {

	// 数据类型
	public static final int DATA_TYPE_UNSIGN = 0;// unsign 无符号
	public static final int DATA_TYPE_SIGNED = 1;// signed 有符号
	public static final int DATA_TYPE_STRING = 2;// String 字符串
	public static final int DATA_TYPE_HEXSTR = 3;// Hex String 十六进制格式字符串

	// 显示类型
	public static final int SHOW_TYPE_DERECT = 0;// direct show
	public static final int SHOW_TYPE_COMBO = 1;// combo box

	// 协议类型
	public static final byte AP_TYPE_A = 0x01;
	public static final byte AP_TYPE_B = 0x02;
	public static final byte AP_TYPE_C = 0x03;

	public static final byte VP_TYPE_A = 0x01;

	public static final int DVC_PACKET_ID_START = 0x8000;

	public static final byte VP_FLAG_CMD = (byte) 0x80;
	public static final byte MCP_TYPE_A = 0x01;

	// 协议基本长度
	public static final int MOBILE_PROT_LEN = 13;

}
