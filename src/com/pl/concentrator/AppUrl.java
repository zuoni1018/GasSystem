package com.pl.concentrator;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class AppUrl {

    //    private static final String BASE_URL = "http://app.hh-ic.com/WebMain.asmx/";
//    private static final String BASE_URL = "http://192.168.2.13:8055/WebMain.asmx/";
    private static final String BASE_URL = "WebMain.asmx/";
    public static final String GET_COLLECTOR_INFO = BASE_URL + "GetCollectorInfo";//�������б�
    public static final String GET_COLLECTOR_NET_WORKING = BASE_URL + "GetCollectorNetWorking";//������ �еı�

    public static final String GET_COLLECTOR_INFO_BY_COLLECTOR_NO = BASE_URL + "GetCollectorInfoByCollectorNo";//�������� ��ѯ��������Ϣ
    public static final String MOVE_COMMUNICATES = BASE_URL + "MoveCommunicates";//�ƶ���

    public static final String MOVE_COMMUNICATES_CTRL_CMD = BASE_URL + "MoveCommunicatesCtrlCmd";//�ƶ���
    public static final String UPDATE_COMMUNICATES = BASE_URL + "UpdateCommunicates";//�ϴ���������
    public static final String METER_READING = BASE_URL + "MeterReading";//����������


}
