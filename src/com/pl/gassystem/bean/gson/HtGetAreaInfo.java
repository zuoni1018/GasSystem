package com.pl.gassystem.bean.gson;

import com.pl.gassystem.bean.ht.HtAreaInfoBean;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/10/10
 */

public class HtGetAreaInfo {

    /**
     * ArrayOfModAreainfo : {"xmlns":"http://localhost/","xmlns:xsd":"http://www.w3.org/2001/XMLSchema","ModAreainfo":[{"PayType":"1","Id":"1","EndReadDate":"","StartReadDate":"","AreaName":"观邸国际","AreaNo":"0000000001","Remark":"22222222"},{"PayType":"","Id":"2","EndReadDate":"","StartReadDate":"","AreaName":"测试","AreaNo":"0000000002","Remark":""},{"PayType":"1","Id":"3","EndReadDate":"","StartReadDate":"","AreaName":"升级小区","AreaNo":"0102030405","Remark":""}],"xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance"}
     */

    private ArrayOfModAreainfoBean ArrayOfModAreainfo;

    public ArrayOfModAreainfoBean getArrayOfModAreainfo() {
        return ArrayOfModAreainfo;
    }

    public void setArrayOfModAreainfo(ArrayOfModAreainfoBean ArrayOfModAreainfo) {
        this.ArrayOfModAreainfo = ArrayOfModAreainfo;
    }

    public static class ArrayOfModAreainfoBean {
        /**
         * xmlns : http://localhost/
         * xmlns:xsd : http://www.w3.org/2001/XMLSchema
         * ModAreainfo : [{"PayType":"1","Id":"1","EndReadDate":"","StartReadDate":"","AreaName":"观邸国际","AreaNo":"0000000001","Remark":"22222222"},{"PayType":"","Id":"2","EndReadDate":"","StartReadDate":"","AreaName":"测试","AreaNo":"0000000002","Remark":""},{"PayType":"1","Id":"3","EndReadDate":"","StartReadDate":"","AreaName":"升级小区","AreaNo":"0102030405","Remark":""}]
         * xmlns:xsi : http://www.w3.org/2001/XMLSchema-instance
         */

        private List<HtAreaInfoBean> ModAreainfo;

        public List<HtAreaInfoBean> getModAreainfo() {
            return ModAreainfo;
        }

        public void setModAreainfo(List<HtAreaInfoBean> ModAreainfo) {
            this.ModAreainfo = ModAreainfo;
        }



    }
}
