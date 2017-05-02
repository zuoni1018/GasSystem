package com.pl.protocol;

public class CPublic {

	// ��������
	public static final int DATA_TYPE_UNSIGN = 0;// unsign �޷���
	public static final int DATA_TYPE_SIGNED = 1;// signed �з���
	public static final int DATA_TYPE_STRING = 2;// String �ַ���
	public static final int DATA_TYPE_HEXSTR = 3;// Hex String ʮ�����Ƹ�ʽ�ַ���

	// ��ʾ����
	public static final int SHOW_TYPE_DERECT = 0;// direct show
	public static final int SHOW_TYPE_COMBO = 1;// combo box

	// Э������
	public static final byte AP_TYPE_A = 0x01;
	public static final byte AP_TYPE_B = 0x02;
	public static final byte AP_TYPE_C = 0x03;

	public static final byte VP_TYPE_A = 0x01;

	public static final int DVC_PACKET_ID_START = 0x8000;

	public static final byte VP_FLAG_CMD = (byte) 0x80;
	public static final byte MCP_TYPE_A = 0x01;

	// Э���������
	public static final int MOBILE_PROT_LEN = 13;

}
