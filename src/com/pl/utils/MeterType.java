package com.pl.utils;

public class MeterType {

	public static String GetMeterTypeName(String MeterTypeNo) {
		String m = "";
		switch (MeterTypeNo) {
		case "04":
			m = "IC������";
			break;
		case "05":
			m = "������";
			break;
		case "06":
			m = "��е��";
			break;
		case "10":
			m = "�����";
			break;
		}
		return m;
	}

	// public static String GetMeterState(int meterState){
	// String m = "";
	// switch (meterState) {
	// case 0: m="����";break;
	// case 1: m="�ط�";break;
	// case 2: m="�͵�";break;
	// default : m="δ֪";
	// }
	// return m;
	// }

	public static String GetCopyPhotoOcrState(int ocrState) {
		String m = "";
		switch (ocrState) {
		case 0:
			m = "ʶ��ʧ��";
			break;
		case 1:
			m = "ʶ��ɹ�";
			break;
		case 2:
			m = "ʶ������";
			break;
		default:
			m = "δ֪";
		}
		return m;
	}

	public static String GetCopyState(int copyState) {
		String m = "";
		switch (copyState) {
		case 0:
			m = "δ��";
			break;
		case 1:
			m = "�ѳ�";
			break;
		default:
			m = "δ֪";
		}
		return m;
	}

	public static String GetPrintFlag(int printFlag) {
		String m = "";
		switch (printFlag) {
		case 0:
			m = "δ��ӡ";
			break;
		case 1:
			m = "�Ѵ�ӡ";
			break;
		default:
			m = "δ֪";
		}
		return m;
	}

	public static String GetIsBalance(int isBalance) {
		String m = "";
		switch (isBalance) {
		case 0:
			m = "δ����";
			break;
		case 1:
			m = "�ѽ���";
			break;
		case 2:
			m = "����ʧ��";
			break;
		default:
			m = "δ֪";
		}
		return m;
	}

	public static String GetCopyWay(String copyWay) {
		String m = "";
		if (copyWay != null) {
			switch (copyWay) {
			case "S":
				m = "���߳���";
				break;
			case "M":
				m = "�˹����볭��";
				break;
			case "G":
				m = "����";
				break;
			case "Y":
				m = "Զ�̳���";
				break;
			}
		}
		return m;
	}

	public static String GetHuiZhouWIMeterStateMsg(int MeterState) {
		// State1
		// BIT0 0 ���ſ���1���Ź�
		// BIT1 0 ���ŵ�λ1���Ų���λ
		// BIT2 0 ����������1һ���͵�
		// BIT3 0 ����������1�����͵�
		// BIT4 0 �޴Ź�����1�дŹ���
		// BIT5 0 ������ߣ�1���ط�
		// BIT6 0 �ֳ���δ���ƹط���
		// 1�ֳ������ƹط�
		// BIT7 0 δ����ѹ������
		// 1����ѹ����

		// State2
		// BIT0 0 ��λ������������
		// 1��λ����������
		// BIT1 0 δ����й©��1й©����
		// BIT2 0 ���������1������
		// BIT3 0 ������������
		// 1�������쳣
		// BIT4 0 ��緢��������
		// 1��緢���쳣
		// BIT5 0 ���������߶�ʱ���ͣ�
		// 1�������߶�ʱ����
		// BIT6 0 ������͵�ط���
		// 1����͵�ط�
		// BIT7 0����

		StringBuilder msg = new StringBuilder();
		String[] state = new String[] { "", "", "", "", "���߹ط�;", "й©�쳣;", "",
				"",

				"", "���Ź�;", "Ƿѹ;", "", "������;", "�Ÿ���;", "���ɻɹ�", "����" };

		for (int i = 0; i < state.length; i++) {
			String tmp = state[i];
			if (tmp.length() > 0) {
				int a = MeterState >> i;
				if (intToBoolean((MeterState >> i) & 1)) {
					msg.append(tmp);
				}
			}
		}

		String tmp2 = msg.toString();
		if (tmp2.equals("")) {
			return "����";
		} else {
			return tmp2;
		}
	}

	public static String GetWIMeterStateMsg(int MeterState) {
		// State1
		// BIT0 0 ���ſ���1���Ź�
		// BIT1 0 ���ŵ�λ1���Ų���λ
		// BIT2 0 ����������1һ���͵�
		// BIT3 0 ����������1�����͵�
		// BIT4 0 �޴Ź�����1�дŹ���
		// BIT5 0 ������ߣ�1���ط�
		// BIT6 0 �ֳ���δ���ƹط���
		// 1�ֳ������ƹط�
		// BIT7 0 δ����ѹ������
		// 1����ѹ����

		// State2
		// BIT0 0 ��λ������������
		// 1��λ����������
		// BIT1 0 δ����й©��1й©����
		// BIT2 0 ���������1������
		// BIT3 0 ������������
		// 1�������쳣
		// BIT4 0 ��緢��������
		// 1��緢���쳣
		// BIT5 0 ���������߶�ʱ���ͣ�
		// 1�������߶�ʱ����
		// BIT6 0 ������͵�ط���
		// 1����͵�ط�
		// BIT7 0����

		StringBuilder msg = new StringBuilder();
		String[] state = new String[] { "��λ����������;", "й©����;", "������;",
				"�������쳣;", "��緢���쳣;", "�������߶�ʱ����;", "����͵�ط�;", "",

				"���Ź�;", "���Ų���λ;", "һ���͵�;", "�����͵�;", "�дŹ���;", "���ط�;",
				"�ֳ������ƹط�", "����ѹ����" };

		for (int i = 0; i < state.length; i++) {
			String tmp = state[i];
			if (tmp.length() > 0) {
				int a = MeterState >> i;
				if (intToBoolean((MeterState >> i) & 1)) {
					msg.append(tmp);
				}
			}
		}

		String tmp2 = msg.toString();
		if (tmp2.equals("")) {
			return "����";
		} else {
			return tmp2;
		}
	}

	public static String GetICMeterStateMsg(int MeterState) {
		// ״̬�ֽ�1: //״̬�ֽ�2: //״̬�ֽ�3:
		// BIT0 ;ǿ�ű�־ //BIT0 ;�����͵��־ //BIT0 ;������־
		// BIT1 ;��Ƭ���� //BIT1 ;һ���͵��־ //BIT1 ;����
		// BIT2 ;������־ //BIT2 ;���������־ //BIT2 ;����
		// BIT3 ;������־ //BIT3 ;���������־ //BIT3 ;����
		// BIT4 ;�ط���־ //BIT4 ;�����־ //BIT4 ;��ѹ��־
		// BIT5 ;�ֳֻ��ط� //BIT5 ;������־ //BIT5 ;����
		// BIT6 ;���� //BIT6 ;��ת��־ //BIT6 ;����
		// BIT7 ;���� //BIT7 ;��������־ //BIT7 ;����

		StringBuilder msg = new StringBuilder();
		String[] state = new String[] { "", "", "", "", "��ѹ;", "", "", "",

		"�����͵�;", "һ���͵�;", "��������;", "��������;", "���;", "����;", "��ת;", "�������;",

		"�Ź���;", "��Ƭ����;", "����;", "����;", "�ط�;", "�ֳֻ��ط�;", "", "" };

		for (int i = 0; i < state.length; i++) {
			String tmp = state[i];
			if (tmp.length() > 0) {
				int a = MeterState >> i;
				if (intToBoolean((MeterState >> i) & 1)) {
					msg.append(tmp);
				}
			}
		}

		String tmp2 = msg.toString();
		if (tmp2.indexOf("�����͵�") > 0) {
			tmp2.replace("һ���͵�;", "");
		}

		if (tmp2.equals("")) {
			return "����";
		} else {
			return tmp2;
		}
	}

	// ��� i Ϊ 1 �򷵻�true�������Ķ���false
	public static boolean intToBoolean(int i) {
		boolean foo = false;
		if (i == 1) {
			foo = true;
		} else {
			foo = false;
		}
		return foo;
	}
}
