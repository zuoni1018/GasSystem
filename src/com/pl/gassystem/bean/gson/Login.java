package com.pl.gassystem.bean.gson;

/**
 * Created by zangyi_shuai_ge on 2017/10/12
 */

public class Login {


    /**
     * string : {"content":"0","xmlns":"http://localhost/"}
     */

    private StringBean string;

    public StringBean getString() {
        return string;
    }

    public void setString(StringBean string) {
        this.string = string;
    }

    public static class StringBean {
        /**
         * content : 0
         * xmlns : http://localhost/
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
