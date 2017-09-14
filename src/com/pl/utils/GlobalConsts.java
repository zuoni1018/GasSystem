package com.pl.utils;

public class GlobalConsts {
	public static final String EXTRA_BOOKINFO_OP_TYPE = "bk_opType";
	public static final int TYPE_ADD = 1;
	public static final int TYPE_UPDATE = 2;

	public static final String EXTRA_BOOKINFO_OP_DATA = "bk_opData";

	// ������ҳ��ʾ����
	public static final String EXTRA_COPYRESULT_TYPE = "re_type";
	public static final int RE_TYPE_COPY = 1;
	public static final int RE_TYPE_SHOWUNCOPY = 2;
	public static final int RE_TYPE_SHOWALL = 3;
	public static final int RE_TYPE_SELECTUNCOPY = 4;
	public static final int RE_TYPE_SELECTCOPY = 5;
	public static final int RE_TYPE_SELECTALL = 6;

	// ��������
	public static final int COPY_TYPE_BATCH = 1;// Ⱥ��
	public static final int COPY_TYPE_SINGLE = 2;// ����

	// ��߲���ģʽ
	public static final int COPY_OPERATION_COPY = 1;// ����
	public static final int COPY_OPERATION_OPENVALVE = 2;// ����
	public static final int COPY_OPERATION_CLOSEVALVE = 3;// �ط�
	public static final int COPY_OPERATION_BUY = 4;// Զ�̳�ֵ
	public static final int COPY_OPERATION_PRICE = 5;// ����
	public static final int COPY_OPERATION_COMNUMBER = 6;// �޸�ͨѶ��
	public static final int COPY_OPERATION_PWD = 7;// �޸ı�����
	public static final int COPY_OPERATION_SETBASENUM = 8;// ���ñ����
	public static final int COPY_OPERATION_COPYFROZEN = 9;// ��ȡ������
	public static final int COPY_OPERATION_PHOTOPOINT = 10;// �����Ƶ���޸�
	public static final int COPY_OPERATION_PHOTOCOMNUMBER = 11;// �����Ƶ���޸�

	// �Ϻ���߳���ģʽ
	public static final int COPYSH_OPERATION_REGNET = 38;// ��������
	public static final int COPYSH_OPERATION_RELAYREGNET = 39;// �м���������
	public static final int COPYSH_OPERATION_CANCELRELAYREGNET = 391;// ȡ���м���������
	public static final int COPYSH_OPERATION_TESTMODE = 40;// ����ģʽ��������
	public static final int COPYSH_OPERATION_GETPAMARS = 41;// ���в�����ѯ����
	public static final int COPYSH_OPERATION_RESET = 42;// �ָ�������������
	public static final int COPYSH_OPERATION_SETKEY = 31;// �趨��Կ����
	public static final int COPYSH_OPERATION_SAFEMODE = 32;// ��ȫģʽ��������
	public static final int COPYSH_OPERATION_SETCOPYDAY = 18;// ���ó���ʱ���
	public static final int COPYSH_OPERATION_GETCOPYDAY = 19;// ��ѯ����ʱ���
	public static final int COPYSH_OPERATION_GETLICENCE = 111;// �����֤��

	// ����͸��ģ��
	public static final int COPYSH_OPERATION_SETMJC = 99;// ����͸��ģ��

	// �˲�ѡ�����ģʽ
	public static final int BOOKINFO_TYPE_COPY = 1;// ����
	public static final int BOOKINFO_TYPE_DOWNLOAD = 2;// ����
	public static final int BOOKINFO_TYPE_UPLOAD = 3;// �ϴ�
	public static final int BOOKINFO_TYPE_SELECT = 4;// ��ѯ

	// ��������ģʽ
	public static final String RUNMODE_STANDARD = "1";
	public static final String RUNMODE_LORA = "2";
	public static final String RUNMODE_FSK = "3";
	public static final String RUNMODE_HUIZHOU = "4";
	public static final String RUNMODE_SHANGHAI = "5";
	public static final String RUNMODE_PHOTO = "6";
	public static final String RUNMODE_ZHGT = "7";

	public static final String RUNMODE_HANG_TIAN = "8";

}
