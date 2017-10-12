package com.pl.gassystem.bean.gson;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/10/10
 */

public class HtGetReadMeterInfo {


    /**
     * ArrayOfModCustomerinfo : {"xmlns":"http://localhost/","xmlns:xsd":"http://www.w3.org/2001/XMLSchema","xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance","ModCustomerinfo":[{"HUNAME":"","AdrCode":"5","KCQZSJ":"0023","DJR":"","KEYCODE":"0102030405060708","XBDS":"","KPYZ":"9","Id":"66","ADDR":"","MQBBH":"","HTEL":"","KPXD":"14","OTEL":"","HUCODE":"","MeterType":"7","AreaNo":"≤‚ ‘","MeterFacNo":"2","YICODE":"111","CommunicateNo":"04160102","KEYVER":"1"},{"HUNAME":"","AdrCode":"7","KCQZSJ":"0023","DJR":"","KEYCODE":"0102030405060708","XBDS":"","KPYZ":"9","Id":"62","ADDR":"","MQBBH":"","HTEL":"","KPXD":"14","OTEL":"","HUCODE":"","MeterType":"7","AreaNo":"≤‚ ‘","MeterFacNo":"2","YICODE":"111","CommunicateNo":"04000032","KEYVER":"1"},{"HUNAME":"","AdrCode":"4","KCQZSJ":"0023","DJR":"","KEYCODE":"0102030405060708","XBDS":"","KPYZ":"9","Id":"65","ADDR":"","MQBBH":"","HTEL":"","KPXD":"14","OTEL":"","HUCODE":"","MeterType":"7","AreaNo":"≤‚ ‘","MeterFacNo":"2","YICODE":"111","CommunicateNo":"04160101","KEYVER":"1"},{"HUNAME":"","AdrCode":"4","KCQZSJ":"0023","DJR":"","KEYCODE":"0102030405060708","XBDS":"","KPYZ":"9","Id":"65","ADDR":"","MQBBH":"","HTEL":"","KPXD":"14","OTEL":"","HUCODE":"","MeterType":"7","AreaNo":"≤‚ ‘","MeterFacNo":"2","YICODE":"111","CommunicateNo":"04160101","KEYVER":"1"},{"HUNAME":"–Ì±¶∏˘","AdrCode":"724200000020100110020","KCQZSJ":"","DJR":"","KEYCODE":"","XBDS":"","KPYZ":"","Id":"69","ADDR":"∫Õ–≥ºŒ‘∞±±‘∑1¥±1µ•‘™5011 “","MQBBH":"","HTEL":"","KPXD":"","OTEL":"","HUCODE":"","MeterType":"8","AreaNo":"≤‚ ‘","MeterFacNo":"0","YICODE":"756821","CommunicateNo":"8162013867","KEYVER":""}]}
     */

    private ArrayOfModCustomerinfoBean ArrayOfModCustomerinfo;

    public ArrayOfModCustomerinfoBean getArrayOfModCustomerinfo() {
        return ArrayOfModCustomerinfo;
    }

    public void setArrayOfModCustomerinfo(ArrayOfModCustomerinfoBean ArrayOfModCustomerinfo) {
        this.ArrayOfModCustomerinfo = ArrayOfModCustomerinfo;
    }

    public static class ArrayOfModCustomerinfoBean {
        /**
         * xmlns : http://localhost/
         * xmlns:xsd : http://www.w3.org/2001/XMLSchema
         * xmlns:xsi : http://www.w3.org/2001/XMLSchema-instance
         * ModCustomerinfo : [{"HUNAME":"","AdrCode":"5","KCQZSJ":"0023","DJR":"","KEYCODE":"0102030405060708","XBDS":"","KPYZ":"9","Id":"66","ADDR":"","MQBBH":"","HTEL":"","KPXD":"14","OTEL":"","HUCODE":"","MeterType":"7","AreaNo":"≤‚ ‘","MeterFacNo":"2","YICODE":"111","CommunicateNo":"04160102","KEYVER":"1"},{"HUNAME":"","AdrCode":"7","KCQZSJ":"0023","DJR":"","KEYCODE":"0102030405060708","XBDS":"","KPYZ":"9","Id":"62","ADDR":"","MQBBH":"","HTEL":"","KPXD":"14","OTEL":"","HUCODE":"","MeterType":"7","AreaNo":"≤‚ ‘","MeterFacNo":"2","YICODE":"111","CommunicateNo":"04000032","KEYVER":"1"},{"HUNAME":"","AdrCode":"4","KCQZSJ":"0023","DJR":"","KEYCODE":"0102030405060708","XBDS":"","KPYZ":"9","Id":"65","ADDR":"","MQBBH":"","HTEL":"","KPXD":"14","OTEL":"","HUCODE":"","MeterType":"7","AreaNo":"≤‚ ‘","MeterFacNo":"2","YICODE":"111","CommunicateNo":"04160101","KEYVER":"1"},{"HUNAME":"","AdrCode":"4","KCQZSJ":"0023","DJR":"","KEYCODE":"0102030405060708","XBDS":"","KPYZ":"9","Id":"65","ADDR":"","MQBBH":"","HTEL":"","KPXD":"14","OTEL":"","HUCODE":"","MeterType":"7","AreaNo":"≤‚ ‘","MeterFacNo":"2","YICODE":"111","CommunicateNo":"04160101","KEYVER":"1"},{"HUNAME":"–Ì±¶∏˘","AdrCode":"724200000020100110020","KCQZSJ":"","DJR":"","KEYCODE":"","XBDS":"","KPYZ":"","Id":"69","ADDR":"∫Õ–≥ºŒ‘∞±±‘∑1¥±1µ•‘™5011 “","MQBBH":"","HTEL":"","KPXD":"","OTEL":"","HUCODE":"","MeterType":"8","AreaNo":"≤‚ ‘","MeterFacNo":"0","YICODE":"756821","CommunicateNo":"8162013867","KEYVER":""}]
         */

        private List<ModCustomerinfoBean> ModCustomerinfo;

        public List<ModCustomerinfoBean> getModCustomerinfo() {
            return ModCustomerinfo;
        }

        public void setModCustomerinfo(List<ModCustomerinfoBean> ModCustomerinfo) {
            this.ModCustomerinfo = ModCustomerinfo;
        }

        public static class ModCustomerinfoBean {

            private boolean isCheck = false;
            private boolean isChoose = false;

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public boolean isChoose() {
                return isChoose;
            }

            public void setChoose(boolean choose) {
                isChoose = choose;
            }

            /**
             * HUNAME :
             * AdrCode : 5
             * KCQZSJ : 0023
             * DJR :
             * KEYCODE : 0102030405060708
             * XBDS :
             * KPYZ : 9
             * Id : 66
             * ADDR :
             * MQBBH :
             * HTEL :
             * KPXD : 14
             * OTEL :
             * HUCODE :
             * MeterType : 7
             * AreaNo : ≤‚ ‘
             * MeterFacNo : 2
             * YICODE : 111
             * CommunicateNo : 04160102
             * KEYVER : 1
             */

            private String HUNAME;
            private String AdrCode;
            private String KCQZSJ;
            private String DJR;
            private String KEYCODE;
            private String XBDS;
            private String KPYZ;
            private String Id;
            private String ADDR;
            private String MQBBH;
            private String HTEL;
            private String KPXD;
            private String OTEL;
            private String HUCODE;
            private String MeterType;
            private String AreaNo;
            private String MeterFacNo;
            private String YICODE;
            private String CommunicateNo;
            private String KEYVER;

            public String getHUNAME() {
                return HUNAME;
            }

            public void setHUNAME(String HUNAME) {
                this.HUNAME = HUNAME;
            }

            public String getAdrCode() {
                return AdrCode;
            }

            public void setAdrCode(String AdrCode) {
                this.AdrCode = AdrCode;
            }

            public String getKCQZSJ() {
                return KCQZSJ;
            }

            public void setKCQZSJ(String KCQZSJ) {
                this.KCQZSJ = KCQZSJ;
            }

            public String getDJR() {
                return DJR;
            }

            public void setDJR(String DJR) {
                this.DJR = DJR;
            }

            public String getKEYCODE() {
                return KEYCODE;
            }

            public void setKEYCODE(String KEYCODE) {
                this.KEYCODE = KEYCODE;
            }

            public String getXBDS() {
                return XBDS;
            }

            public void setXBDS(String XBDS) {
                this.XBDS = XBDS;
            }

            public String getKPYZ() {
                return KPYZ;
            }

            public void setKPYZ(String KPYZ) {
                this.KPYZ = KPYZ;
            }

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public String getADDR() {
                return ADDR;
            }

            public void setADDR(String ADDR) {
                this.ADDR = ADDR;
            }

            public String getMQBBH() {
                return MQBBH;
            }

            public void setMQBBH(String MQBBH) {
                this.MQBBH = MQBBH;
            }

            public String getHTEL() {
                return HTEL;
            }

            public void setHTEL(String HTEL) {
                this.HTEL = HTEL;
            }

            public String getKPXD() {
                return KPXD;
            }

            public void setKPXD(String KPXD) {
                this.KPXD = KPXD;
            }

            public String getOTEL() {
                return OTEL;
            }

            public void setOTEL(String OTEL) {
                this.OTEL = OTEL;
            }

            public String getHUCODE() {
                return HUCODE;
            }

            public void setHUCODE(String HUCODE) {
                this.HUCODE = HUCODE;
            }

            public String getMeterType() {
                return MeterType;
            }

            public void setMeterType(String MeterType) {
                this.MeterType = MeterType;
            }

            public String getAreaNo() {
                return AreaNo;
            }

            public void setAreaNo(String AreaNo) {
                this.AreaNo = AreaNo;
            }

            public String getMeterFacNo() {
                switch (MeterFacNo) {
                    case "0":
                        return "…„œÒ±Ì";
                    case "1":
                        return "¥øŒﬁœﬂ";
                    case "2":
                        return "¿©∆µ±Ì";
                    default:
                        return "Œ¥÷™";
                }
            }

            public void setMeterFacNo(String MeterFacNo) {
                this.MeterFacNo = MeterFacNo;
            }

            public String getYICODE() {
                return YICODE;
            }

            public void setYICODE(String YICODE) {
                this.YICODE = YICODE;
            }

            public String getCommunicateNo() {
                return CommunicateNo;
            }

            public void setCommunicateNo(String CommunicateNo) {
                this.CommunicateNo = CommunicateNo;
            }

            public String getKEYVER() {
                return KEYVER;
            }

            public void setKEYVER(String KEYVER) {
                this.KEYVER = KEYVER;
            }
        }
    }
}
