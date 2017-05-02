package com.pl.utils;

/**
 * Created by zangyi_shuai_ge on 2017/5/2
 * Excel 创建辅助类
 */

public class CreateExcelUtils {

    //账册信息
    public static final String[] bookInfoTitle = {"分组编号", "小区编号", "分组名称", "备注", "备注", "表计类型编号", "账册编号"};
    //原始抄表数据(无线、机械)
    public static final String[] copyDataTitle = {
            "流水号", "表计编号", "上次读数", "上次用量", "本次读数",
            "本次用量", "单价", "打印标志(0-未打印 1-已打印)",
            "表计状态", "抄表方式", "抄表状态", "抄表时间", "抄表员姓名",
            "操作员", "操作时间", "结算标志", "备注", "抄表员姓名"};


}
