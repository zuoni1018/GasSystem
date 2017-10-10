package com.pl.gassystem.bean.gson;

import com.pl.gassystem.bean.ht.HtBookInfoBean;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/10/9
 */

public class HtGetBookInfo {


    /**
     * ArrayOfModApp_bookinfo : {"xmlns":"http://localhost/","xmlns:xsd":"http://www.w3.org/2001/XMLSchema","xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance","ModApp_bookinfo":[{"StaffNo":"1","MeterTypeNo":"1","BookName":"È¼Íø1","AreaNo":"0000000001","BookNo":"1","Remark":""},{"StaffNo":"1","MeterTypeNo":"1","BookName":"È¼Íø2","AreaNo":"0000000002","BookNo":"2","Remark":""}]}
     */

    private ArrayOfModAppBookinfoBean ArrayOfModApp_bookinfo;

    public ArrayOfModAppBookinfoBean getArrayOfModApp_bookinfo() {
        return ArrayOfModApp_bookinfo;
    }

    public void setArrayOfModApp_bookinfo(ArrayOfModAppBookinfoBean ArrayOfModApp_bookinfo) {
        this.ArrayOfModApp_bookinfo = ArrayOfModApp_bookinfo;
    }

    public static class ArrayOfModAppBookinfoBean {
        /**
         * xmlns : http://localhost/
         * xmlns:xsd : http://www.w3.org/2001/XMLSchema
         * xmlns:xsi : http://www.w3.org/2001/XMLSchema-instance
         * ModApp_bookinfo : [{"StaffNo":"1","MeterTypeNo":"1","BookName":"È¼Íø1","AreaNo":"0000000001","BookNo":"1","Remark":""},{"StaffNo":"1","MeterTypeNo":"1","BookName":"È¼Íø2","AreaNo":"0000000002","BookNo":"2","Remark":""}]
         */

        private List<HtBookInfoBean> ModApp_bookinfo;

        public List<HtBookInfoBean> getModApp_bookinfo() {
            return ModApp_bookinfo;
        }

        public void setModApp_bookinfo(List<HtBookInfoBean> ModApp_bookinfo) {
            this.ModApp_bookinfo = ModApp_bookinfo;
        }


    }
}
