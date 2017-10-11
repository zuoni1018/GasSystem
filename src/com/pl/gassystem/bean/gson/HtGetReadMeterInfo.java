package com.pl.gassystem.bean.gson;

/**
 * Created by zangyi_shuai_ge on 2017/10/10
 */

public class HtGetReadMeterInfo {


    /**
     * ArrayOfModCustomerinfo : {"xmlns":"http://localhost/","xmlns:xsd":"http://www.w3.org/2001/XMLSchema","xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance","ModCustomerinfo":{"HUNAME":"","AdrCode":"4","KCQZSJ":"0023","DJR":"","KEYCODE":"0102030405060708","XBDS":"","KPYZ":"9","Id":"65","ADDR":"","MQBBH":"","HTEL":"","KPXD":"14","OTEL":"","HUCODE":"","MeterType":"7","AreaNo":"≤‚ ‘","MeterFacNo":"2","YICODE":"111","CommunicateNo":"04160101","KEYVER":"1"}}
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
         * ModCustomerinfo : {"HUNAME":"","AdrCode":"4","KCQZSJ":"0023","DJR":"","KEYCODE":"0102030405060708","XBDS":"","KPYZ":"9","Id":"65","ADDR":"","MQBBH":"","HTEL":"","KPXD":"14","OTEL":"","HUCODE":"","MeterType":"7","AreaNo":"≤‚ ‘","MeterFacNo":"2","YICODE":"111","CommunicateNo":"04160101","KEYVER":"1"}
         */

        private ModCustomerinfoBean ModCustomerinfo;

        public ModCustomerinfoBean getModCustomerinfo() {
            return ModCustomerinfo;
        }

        public void setModCustomerinfo(ModCustomerinfoBean ModCustomerinfo) {
            this.ModCustomerinfo = ModCustomerinfo;
        }

        public static class ModCustomerinfoBean {
            /**
             * HUNAME :
             * AdrCode : 4
             * KCQZSJ : 0023
             * DJR :
             * KEYCODE : 0102030405060708
             * XBDS :
             * KPYZ : 9
             * Id : 65
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
             * CommunicateNo : 04160101
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
                return MeterFacNo;
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
