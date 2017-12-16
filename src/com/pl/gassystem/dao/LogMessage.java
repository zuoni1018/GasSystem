package com.pl.gassystem.dao;

/**
 * Created by zangyi_shuai_ge on 2017/11/6
 */

public class LogMessage {
    private String time;
    private String type="";
    private String message="";

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
