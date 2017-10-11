package com.pl.gassystem.bean.gson;

import com.pl.gassystem.bean.ht.HtGroupBindBean;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/10/9
 */

public class HtGetGroupBind {

    /**
     * ArrayOfModApp_groupbind : {"ModApp_groupbind":[{"MeterTypeNo":"1","MeterNo":"12345678","GroupNo":"1"},{"MeterTypeNo":"1","MeterNo":"12345679","GroupNo":"1"}],"xmlns":"http://localhost/","xmlns:xsd":"http://www.w3.org/2001/XMLSchema","xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance"}
     */

    private ArrayOfModAppGroupbindBean ArrayOfModApp_groupbind;

    public ArrayOfModAppGroupbindBean getArrayOfModApp_groupbind() {
        return ArrayOfModApp_groupbind;
    }

    public void setArrayOfModApp_groupbind(ArrayOfModAppGroupbindBean ArrayOfModApp_groupbind) {
        this.ArrayOfModApp_groupbind = ArrayOfModApp_groupbind;
    }

    public static class ArrayOfModAppGroupbindBean {
        /**
         * ModApp_groupbind : [{"MeterTypeNo":"1","MeterNo":"12345678","GroupNo":"1"},{"MeterTypeNo":"1","MeterNo":"12345679","GroupNo":"1"}]
         * xmlns : http://localhost/
         * xmlns:xsd : http://www.w3.org/2001/XMLSchema
         * xmlns:xsi : http://www.w3.org/2001/XMLSchema-instance
         */

        private List<HtGroupBindBean> ModApp_groupbind;

        public List<HtGroupBindBean> getModApp_groupbind() {
            return ModApp_groupbind;
        }

        public void setModApp_groupbind(List<HtGroupBindBean> ModApp_groupbind) {
            this.ModApp_groupbind = ModApp_groupbind;
        }


    }
}
