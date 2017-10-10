package com.pl.gassystem.bean.gson;

import com.pl.gassystem.bean.ht.HtGroupInfoBean;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/10/9
 */

public class HtGetGroupInfo {

    /**
     * ArrayOfModApp_groupinfo : {"xmlns":"http://localhost/","xmlns:xsd":"http://www.w3.org/2001/XMLSchema","xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance","ModApp_groupinfo":[{"KPXD":"1","KCQZSJ":"1122","DJR":"1122","KEYCODE":"0102030405060708","BookNo":"1","MeterTypeNo":"1","AreaNo":"0000000001","KPYZ":"1","Remark":"","GroupName":"È¼Íø1","GroupNo":"1","KEYVER":"1"},{"KPXD":"","KCQZSJ":"","DJR":"","KEYCODE":"","BookNo":"1","MeterTypeNo":"0","AreaNo":"0000000001","KPYZ":"","Remark":"","GroupName":"È¼Íø2","GroupNo":"2","KEYVER":""}]}
     */

    private ArrayOfModAppGroupinfoBean ArrayOfModApp_groupinfo;

    public ArrayOfModAppGroupinfoBean getArrayOfModApp_groupinfo() {
        return ArrayOfModApp_groupinfo;
    }

    public void setArrayOfModApp_groupinfo(ArrayOfModAppGroupinfoBean ArrayOfModApp_groupinfo) {
        this.ArrayOfModApp_groupinfo = ArrayOfModApp_groupinfo;
    }

    public static class ArrayOfModAppGroupinfoBean {
        /**
         * xmlns : http://localhost/
         * xmlns:xsd : http://www.w3.org/2001/XMLSchema
         * xmlns:xsi : http://www.w3.org/2001/XMLSchema-instance
         * ModApp_groupinfo : [{"KPXD":"1","KCQZSJ":"1122","DJR":"1122","KEYCODE":"0102030405060708","BookNo":"1","MeterTypeNo":"1","AreaNo":"0000000001","KPYZ":"1","Remark":"","GroupName":"È¼Íø1","GroupNo":"1","KEYVER":"1"},{"KPXD":"","KCQZSJ":"","DJR":"","KEYCODE":"","BookNo":"1","MeterTypeNo":"0","AreaNo":"0000000001","KPYZ":"","Remark":"","GroupName":"È¼Íø2","GroupNo":"2","KEYVER":""}]
         */

        private List<HtGroupInfoBean> ModApp_groupinfo;

        public List<HtGroupInfoBean> getModApp_groupinfo() {
            return ModApp_groupinfo;
        }

        public void setModApp_groupinfo(List<HtGroupInfoBean> ModApp_groupinfo) {
            this.ModApp_groupinfo = ModApp_groupinfo;
        }


    }
}
