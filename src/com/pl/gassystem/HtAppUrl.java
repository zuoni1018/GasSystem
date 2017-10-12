package com.pl.gassystem;

/**
 * Created by zangyi_shuai_ge on 2017/10/9
 */

public class HtAppUrl {
    private static final String BASE_URL = "http://116.62.6.184:8088/GNTMeterReadService.asmx";
    public static final String GET_BOOK_INFO = BASE_URL + "/GetBookInfo";
    public static final String GET_GROUP_INFO = BASE_URL + "/GetGroupInfo";
    public static final String GET_GROUP_BIND = BASE_URL + "/GetGroupBind";
    public static final String GET_UPDATE_METER_INFO = BASE_URL + "/GetUpdateMeterInfo";
    public static final String GET_READ_METER_INFO = BASE_URL + "/GetReadMeterInfo";
    public static final String GET_AREA_INFO = BASE_URL + "/GetAreaInfo";
    public static final String GET_COPY_DATA_LORA = BASE_URL + "/GetCopyDataLora";
    public static final String GET_COPY_DATA_PHOTO = BASE_URL + "/GetCopyDataPhoto";


    public static final String UP_LOAD_METER_INFO = BASE_URL + "/UploadMeterInfo";
    public static final String LOGIN = BASE_URL + "/Login";


}
