package com.pl.concentrator;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class AppUrl {

    //    private static final String BASE_URL = "http://app.hh-ic.com/WebMain.asmx/";
//    private static final String BASE_URL = "http://192.168.2.13:8055/WebMain.asmx/";
    private static final String BASE_URL = "WebMain.asmx/";
    public static final String GET_COLLECTOR_INFO = BASE_URL + "GetCollectorInfo";//集中器列表
    public static final String GET_COLLECTOR_NET_WORKING = BASE_URL + "GetCollectorNetWorking";//集中器 中的表

    public static final String GET_COLLECTOR_INFO_BY_COLLECTOR_NO = BASE_URL + "GetCollectorInfoByCollectorNo";//集中器号 查询集中器信息
    public static final String MOVE_COMMUNICATES = BASE_URL + "MoveCommunicates";//移动表

    public static final String MOVE_COMMUNICATES_CTRL_CMD = BASE_URL + "MoveCommunicatesCtrlCmd";//移动表
    public static final String UPDATE_COMMUNICATES = BASE_URL + "UpdateCommunicates";//上传抄表数据
    public static final String METER_READING = BASE_URL + "MeterReading";//集中器抄表


}
