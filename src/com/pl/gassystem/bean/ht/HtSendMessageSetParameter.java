package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/9/18
 * 批量设置表具参数
 */


public class HtSendMessageSetParameter extends HtSendMessage {

    private boolean needKuoPinYinZi = false;
    private String kuo_pin_xin_dao = "00";
    private String kuo_pin_yin_zi = "00";
    private String dong_jie_ri = "0000";
    private String kai_chuang_qi_zhi_shi_jian = "0000";

    public String  isNeedKuoPinYinZi() {
        if(needKuoPinYinZi){
            return "01";
        }else {
            return "00";
        }
    }

    public void setNeedKuoPinYinZi(boolean needKuoPinYinZi) {
        this.needKuoPinYinZi = needKuoPinYinZi;
    }

    public String getKuo_pin_xin_dao() {
        return kuo_pin_xin_dao;
    }

    public void setKuo_pin_xin_dao(String kuo_pin_xin_dao) {
        this.kuo_pin_xin_dao = kuo_pin_xin_dao;
    }

    public String getKuo_pin_yin_zi() {
        return kuo_pin_yin_zi;
    }

    public void setKuo_pin_yin_zi(String kuo_pin_yin_zi) {
        this.kuo_pin_yin_zi = kuo_pin_yin_zi;
    }

    public String getDong_jie_ri() {
        return dong_jie_ri;
    }

    public void setDong_jie_ri(String dong_jie_ri) {
        this.dong_jie_ri = dong_jie_ri;
    }

    public String getKai_chuang_qi_zhi_shi_jian() {
        return kai_chuang_qi_zhi_shi_jian;
    }

    public void setKai_chuang_qi_zhi_shi_jian(String kai_chuang_qi_zhi_shi_jian) {
        this.kai_chuang_qi_zhi_shi_jian = kai_chuang_qi_zhi_shi_jian;
    }
}


