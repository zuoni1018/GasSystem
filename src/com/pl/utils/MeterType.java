package com.pl.utils;

public class MeterType {

	public static String GetMeterTypeName(String MeterTypeNo) {
		String m = "";
		switch (MeterTypeNo) {
		case "04":
			m = "IC卡无线";
			break;
		case "05":
			m = "纯无线";
			break;
		case "06":
			m = "机械表";
			break;
		case "10":
			m = "摄像表";
			break;
		}
		return m;
	}

	// public static String GetMeterState(int meterState){
	// String m = "";
	// switch (meterState) {
	// case 0: m="正常";break;
	// case 1: m="关阀";break;
	// case 2: m="低电";break;
	// default : m="未知";
	// }
	// return m;
	// }

	public static String GetCopyPhotoOcrState(int ocrState) {
		String m = "";
		switch (ocrState) {
		case 0:
			m = "识别失败";
			break;
		case 1:
			m = "识别成功";
			break;
		case 2:
			m = "识别质疑";
			break;
		default:
			m = "未知";
		}
		return m;
	}

	public static String GetCopyState(int copyState) {
		String m = "";
		switch (copyState) {
		case 0:
			m = "未抄";
			break;
		case 1:
			m = "已抄";
			break;
		default:
			m = "未知";
		}
		return m;
	}

	public static String GetPrintFlag(int printFlag) {
		String m = "";
		switch (printFlag) {
		case 0:
			m = "未打印";
			break;
		case 1:
			m = "已打印";
			break;
		default:
			m = "未知";
		}
		return m;
	}

	public static String GetIsBalance(int isBalance) {
		String m = "";
		switch (isBalance) {
		case 0:
			m = "未结算";
			break;
		case 1:
			m = "已结算";
			break;
		case 2:
			m = "结算失败";
			break;
		default:
			m = "未知";
		}
		return m;
	}

	public static String GetCopyWay(String copyWay) {
		String m = "";
		if (copyWay != null) {
			switch (copyWay) {
			case "S":
				m = "无线抄表";
				break;
			case "M":
				m = "人工输入抄表";
				break;
			case "G":
				m = "估抄";
				break;
			case "Y":
				m = "远程抄表";
				break;
			}
		}
		return m;
	}

	public static String GetHuiZhouWIMeterStateMsg(int MeterState) {
		// State1
		// BIT0 0 阀门开；1阀门关
		// BIT1 0 阀门到位1阀门不到位
		// BIT2 0 电量正常；1一级低电
		// BIT3 0 电量正常；1二级低电
		// BIT4 0 无磁攻击；1有磁攻击
		// BIT5 0 表计在线；1拆表关阀
		// BIT6 0 手抄机未控制关阀；
		// 1手抄机控制关阀
		// BIT7 0 未过电压保护；
		// 1过电压保护

		// State2
		// BIT0 0 单位内流量正常；
		// 1单位内流量超限
		// BIT1 0 未发生泄漏；1泄漏报警
		// BIT2 0 脉冲采样；1光电采样
		// BIT3 0 光电接收正常；
		// 1光电接收异常
		// BIT4 0 光电发射正常；
		// 1光电发射异常
		// BIT5 0 不允许无线定时发送；
		// 1允许无线定时发送
		// BIT6 0 不允许低电关阀；
		// 1允许低电关阀
		// BIT7 0保留

		StringBuilder msg = new StringBuilder();
		String[] state = new String[] { "", "", "", "", "无线关阀;", "泄漏异常;", "",
				"",

				"", "阀门关;", "欠压;", "", "超流量;", "磁干扰;", "单干簧管", "断线" };

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
			return "正常";
		} else {
			return tmp2;
		}
	}

	public static String GetWIMeterStateMsg(int MeterState) {
		// State1
		// BIT0 0 阀门开；1阀门关
		// BIT1 0 阀门到位1阀门不到位
		// BIT2 0 电量正常；1一级低电
		// BIT3 0 电量正常；1二级低电
		// BIT4 0 无磁攻击；1有磁攻击
		// BIT5 0 表计在线；1拆表关阀
		// BIT6 0 手抄机未控制关阀；
		// 1手抄机控制关阀
		// BIT7 0 未过电压保护；
		// 1过电压保护

		// State2
		// BIT0 0 单位内流量正常；
		// 1单位内流量超限
		// BIT1 0 未发生泄漏；1泄漏报警
		// BIT2 0 脉冲采样；1光电采样
		// BIT3 0 光电接收正常；
		// 1光电接收异常
		// BIT4 0 光电发射正常；
		// 1光电发射异常
		// BIT5 0 不允许无线定时发送；
		// 1允许无线定时发送
		// BIT6 0 不允许低电关阀；
		// 1允许低电关阀
		// BIT7 0保留

		StringBuilder msg = new StringBuilder();
		String[] state = new String[] { "单位内流量超限;", "泄漏报警;", "光电采样;",
				"光电接收异常;", "光电发射异常;", "允许无线定时发送;", "允许低电关阀;", "",

				"阀门关;", "阀门不到位;", "一级低电;", "二级低电;", "有磁攻击;", "拆表关阀;",
				"手抄机控制关阀", "过电压保护" };

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
			return "正常";
		} else {
			return tmp2;
		}
	}

	public static String GetICMeterStateMsg(int MeterState) {
		// 状态字节1: //状态字节2: //状态字节3:
		// BIT0 ;强磁标志 //BIT0 ;二级低电标志 //BIT0 ;开户标志
		// BIT1 ;铁片攻击 //BIT1 ;一级低电标志 //BIT1 ;保留
		// BIT2 ;零气标志 //BIT2 ;气量不足标志 //BIT2 ;保留
		// BIT3 ;负气标志 //BIT3 ;次数出错标志 //BIT3 ;保留
		// BIT4 ;关阀标志 //BIT4 ;防拆标志 //BIT4 ;过压标志
		// BIT5 ;手持机关阀 //BIT5 ;过流标志 //BIT5 ;保留
		// BIT6 ;保留 //BIT6 ;堵转标志 //BIT6 ;保留
		// BIT7 ;保留 //BIT7 ;卡密码错标志 //BIT7 ;保留

		StringBuilder msg = new StringBuilder();
		String[] state = new String[] { "", "", "", "", "过压;", "", "", "",

		"二级低电;", "一级低电;", "气量不足;", "次数出错;", "拆表;", "过流;", "堵转;", "卡密码错;",

		"磁攻击;", "铁片攻击;", "零气;", "负气;", "关阀;", "手持机关阀;", "", "" };

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
		if (tmp2.indexOf("二级低电") > 0) {
			tmp2.replace("一级低电;", "");
		}

		if (tmp2.equals("")) {
			return "正常";
		} else {
			return tmp2;
		}
	}

	// 如果 i 为 1 则返回true，其他的都是false
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
