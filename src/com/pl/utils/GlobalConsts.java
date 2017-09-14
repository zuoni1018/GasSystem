package com.pl.utils;

public class GlobalConsts {
	public static final String EXTRA_BOOKINFO_OP_TYPE = "bk_opType";
	public static final int TYPE_ADD = 1;
	public static final int TYPE_UPDATE = 2;

	public static final String EXTRA_BOOKINFO_OP_DATA = "bk_opData";

	// 抄表结果页显示类型
	public static final String EXTRA_COPYRESULT_TYPE = "re_type";
	public static final int RE_TYPE_COPY = 1;
	public static final int RE_TYPE_SHOWUNCOPY = 2;
	public static final int RE_TYPE_SHOWALL = 3;
	public static final int RE_TYPE_SELECTUNCOPY = 4;
	public static final int RE_TYPE_SELECTCOPY = 5;
	public static final int RE_TYPE_SELECTALL = 6;

	// 抄表类型
	public static final int COPY_TYPE_BATCH = 1;// 群抄
	public static final int COPY_TYPE_SINGLE = 2;// 单抄

	// 表具操作模式
	public static final int COPY_OPERATION_COPY = 1;// 抄表
	public static final int COPY_OPERATION_OPENVALVE = 2;// 开阀
	public static final int COPY_OPERATION_CLOSEVALVE = 3;// 关阀
	public static final int COPY_OPERATION_BUY = 4;// 远程充值
	public static final int COPY_OPERATION_PRICE = 5;// 调价
	public static final int COPY_OPERATION_COMNUMBER = 6;// 修改通讯号
	public static final int COPY_OPERATION_PWD = 7;// 修改表密码
	public static final int COPY_OPERATION_SETBASENUM = 8;// 设置表底数
	public static final int COPY_OPERATION_COPYFROZEN = 9;// 抄取冻结量
	public static final int COPY_OPERATION_PHOTOPOINT = 10;// 摄像表频点修改
	public static final int COPY_OPERATION_PHOTOCOMNUMBER = 11;// 摄像表频点修改

	// 上海表具抄表模式
	public static final int COPYSH_OPERATION_REGNET = 38;// 入网命令
	public static final int COPYSH_OPERATION_RELAYREGNET = 39;// 中继入网命令
	public static final int COPYSH_OPERATION_CANCELRELAYREGNET = 391;// 取消中继入网命令
	public static final int COPYSH_OPERATION_TESTMODE = 40;// 测试模式设置命令
	public static final int COPYSH_OPERATION_GETPAMARS = 41;// 运行参数查询命令
	public static final int COPYSH_OPERATION_RESET = 42;// 恢复出厂设置命令
	public static final int COPYSH_OPERATION_SETKEY = 31;// 设定密钥命令
	public static final int COPYSH_OPERATION_SAFEMODE = 32;// 安全模式设置命令
	public static final int COPYSH_OPERATION_SETCOPYDAY = 18;// 设置抄表时间段
	public static final int COPYSH_OPERATION_GETCOPYDAY = 19;// 查询抄表时间段
	public static final int COPYSH_OPERATION_GETLICENCE = 111;// 读许可证号

	// 设置透传模块
	public static final int COPYSH_OPERATION_SETMJC = 99;// 设置透传模块

	// 账册选择操作模式
	public static final int BOOKINFO_TYPE_COPY = 1;// 抄表
	public static final int BOOKINFO_TYPE_DOWNLOAD = 2;// 下载
	public static final int BOOKINFO_TYPE_UPLOAD = 3;// 上传
	public static final int BOOKINFO_TYPE_SELECT = 4;// 查询

	// 抄表运行模式
	public static final String RUNMODE_STANDARD = "1";
	public static final String RUNMODE_LORA = "2";
	public static final String RUNMODE_FSK = "3";
	public static final String RUNMODE_HUIZHOU = "4";
	public static final String RUNMODE_SHANGHAI = "5";
	public static final String RUNMODE_PHOTO = "6";
	public static final String RUNMODE_ZHGT = "7";

	public static final String RUNMODE_HANG_TIAN = "8";

}
