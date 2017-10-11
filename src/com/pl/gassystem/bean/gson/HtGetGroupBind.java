package com.pl.gassystem.bean.gson;

import com.pl.gassystem.bean.ht.HtCustomerInfoBean;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/10/9
 */

public class HtGetGroupBind {


    /**
     * ArrayOfModCustomerinfo : {"xmlns":"http://localhost/","xmlns:xsd":"http://www.w3.org/2001/XMLSchema","xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance","ModCustomerinfo":[{"KPXD":"14","KCQZSJ":"0023","DJR":"","KEYCODE":"0102030405060708","MeterFacNo":"2","KPYZ":"9","Id":"1395","ADDR":"20170721","CommunicateNo":"05170016","KEYVER":"1"},{"KPXD":"14","KCQZSJ":"1122","DJR":"","KEYCODE":"0102030405060708","MeterFacNo":"2","KPYZ":"9","Id":"2726","ADDR":"燃网大院1幢","CommunicateNo":"04000015","KEYVER":"01"},{"KPXD":"14","KCQZSJ":"1122","DJR":"","KEYCODE":"0102030405060708","MeterFacNo":"2","KPYZ":"9","Id":"2727","ADDR":"燃网大院1幢1","CommunicateNo":"04160105","KEYVER":"01"}]}
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
         * ModCustomerinfo : [{"KPXD":"14","KCQZSJ":"0023","DJR":"","KEYCODE":"0102030405060708","MeterFacNo":"2","KPYZ":"9","Id":"1395","ADDR":"20170721","CommunicateNo":"05170016","KEYVER":"1"},{"KPXD":"14","KCQZSJ":"1122","DJR":"","KEYCODE":"0102030405060708","MeterFacNo":"2","KPYZ":"9","Id":"2726","ADDR":"燃网大院1幢","CommunicateNo":"04000015","KEYVER":"01"},{"KPXD":"14","KCQZSJ":"1122","DJR":"","KEYCODE":"0102030405060708","MeterFacNo":"2","KPYZ":"9","Id":"2727","ADDR":"燃网大院1幢1","CommunicateNo":"04160105","KEYVER":"01"}]
         */

        private List<HtCustomerInfoBean> ModCustomerinfo;

        public List<HtCustomerInfoBean> getModCustomerinfo() {
            return ModCustomerinfo;
        }

        public void setModCustomerinfo(List<HtCustomerInfoBean> ModCustomerinfo) {
            this.ModCustomerinfo = ModCustomerinfo;
        }


    }
}
