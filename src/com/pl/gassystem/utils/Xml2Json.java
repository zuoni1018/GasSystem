package com.pl.gassystem.utils;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/**
 * Created by zangyi_shuai_ge on 2017/10/9
 */

public class Xml2Json {
    public static String xml2JSON(String xml) {
        try {
            XmlToJson xmlToJson = new XmlToJson.Builder(xml).build();
            return xmlToJson.toString().replace("\\","");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String xml2JSON2List(String a,String b,String xml) {
        XmlToJson xmlToJson = new XmlToJson.Builder(xml)
                .forceList("/"+a+"/"+b)
                .build();
        return xmlToJson.toString();
    }
}
