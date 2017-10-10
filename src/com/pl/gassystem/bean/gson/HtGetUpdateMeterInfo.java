package com.pl.gassystem.bean.gson;

import com.pl.gassystem.bean.ht.HtUpdateMeterInfoBean;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/10/10
 */

public class HtGetUpdateMeterInfo {


    /**
     * ArrayOfModApp_updatemeterinfo : {"xmlns":"http://localhost/","xmlns:xsd":"http://www.w3.org/2001/XMLSchema","ModApp_updatemeterinfo":[{"MeterTypeNo":"2","KEYVER2":"01","KCQZSJ1":"1123","KPYZ2":"11","KPXD2":"9","MeterNo":"05170016","DJR1":"1122","KPYZ1":"9","KEYCODE2":"0102030405060708","KPXD1":"9","KEYCODE1":"0102030405060708","AreaNo":"0000000001","KEYVER1":"01","DJR2":"1122","KCQZSJ2":"1123"},{"MeterTypeNo":"2","KEYVER2":"01","KCQZSJ1":"1124","KPYZ2":"11","KPXD2":"9","MeterNo":"12345678","DJR1":"1112","KPYZ1":"9","KEYCODE2":"0102030405060708","KPXD1":"9","KEYCODE1":"0102030405060708","AreaNo":"0000000001","KEYVER1":"01","DJR2":"1122","KCQZSJ2":"1124"}],"xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance"}
     */

    private ArrayOfModAppUpdatemeterinfoBean ArrayOfModApp_updatemeterinfo;

    public ArrayOfModAppUpdatemeterinfoBean getArrayOfModApp_updatemeterinfo() {
        return ArrayOfModApp_updatemeterinfo;
    }

    public void setArrayOfModApp_updatemeterinfo(ArrayOfModAppUpdatemeterinfoBean ArrayOfModApp_updatemeterinfo) {
        this.ArrayOfModApp_updatemeterinfo = ArrayOfModApp_updatemeterinfo;
    }

    public static class ArrayOfModAppUpdatemeterinfoBean {
        /**
         * xmlns : http://localhost/
         * xmlns:xsd : http://www.w3.org/2001/XMLSchema
         * ModApp_updatemeterinfo : [{"MeterTypeNo":"2","KEYVER2":"01","KCQZSJ1":"1123","KPYZ2":"11","KPXD2":"9","MeterNo":"05170016","DJR1":"1122","KPYZ1":"9","KEYCODE2":"0102030405060708","KPXD1":"9","KEYCODE1":"0102030405060708","AreaNo":"0000000001","KEYVER1":"01","DJR2":"1122","KCQZSJ2":"1123"},{"MeterTypeNo":"2","KEYVER2":"01","KCQZSJ1":"1124","KPYZ2":"11","KPXD2":"9","MeterNo":"12345678","DJR1":"1112","KPYZ1":"9","KEYCODE2":"0102030405060708","KPXD1":"9","KEYCODE1":"0102030405060708","AreaNo":"0000000001","KEYVER1":"01","DJR2":"1122","KCQZSJ2":"1124"}]
         * xmlns:xsi : http://www.w3.org/2001/XMLSchema-instance
         */

        private List<HtUpdateMeterInfoBean> ModApp_updatemeterinfo;

        public List<HtUpdateMeterInfoBean> getModApp_updatemeterinfo() {
            return ModApp_updatemeterinfo;
        }

        public void setModApp_updatemeterinfo(List<HtUpdateMeterInfoBean> ModApp_updatemeterinfo) {
            this.ModApp_updatemeterinfo = ModApp_updatemeterinfo;
        }
    }
}
