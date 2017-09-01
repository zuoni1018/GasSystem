package com.pl.cxml;

import java.util.Set;
import java.util.Vector;
import java.util.HashMap;

public class CXmlData {
    private Vector<HashMap<String, String>> allData;
    private int nowSet = -1, nowGet = 0;// 总元素数-1.size-1
    private String defaultAttrName;

    public CXmlData() {
        allData = new Vector<HashMap<String, String>>();
    }

    public void AddOneElement()// 增加一个元素
    {
        allData.add(new HashMap<String, String>());
        ++nowSet;
    }

    public void AddOneAttr(String k, String v)// 增加当前元素一个属性
    {
        if (allData.size() == 0)
            AddOneElement();
        allData.get(nowSet).put(k, v);
    }

    public boolean AddOneAttr(int n, String k, String v)// 第n个元素一个属性，(n从0开始)
    {
        if (allData.size() == 0)
            AddOneElement();
        if (n < 0 || n > allData.size() - 1)
            return false;
        allData.get(n).put(k, v);
        return true;

    }

    public int GetSumOfElement()// 获得元素个数
    {
        return allData.size();
    }

    public void SetDefaultAttrName(String name)// 设置查询时默认的属性名,在GetAttr不传入参数时使用这个值作为键值
    {
        defaultAttrName = name;
    }

    /*
     * 方法1： 获得当前元素属性值 入参：元素属性，如果没有参数获得为""或者为"."，则返回这个元素的名字。(如返回 "p0000"
     * 返回：属性的值,如果没有，就返回""
     */
    public String GetAttr()// 获得元素属性值
    {
        return GetAttr(defaultAttrName);
    }

    public String GetAttr(String key)// 获得元素属性值
    {
        if (key == "" || key == null)
            key = ".";
        if (allData.size() == 0)
            return null;
        return allData.get(nowGet).get(key);
    }

    /*
     * 方法2： 切换到第几个元素,从1开始 入参：第几个元素 返回：属性的值,如果为false,表示已经没有数据了。
     */
    public boolean ChangeTo(int getn)// 获得元素属性值,从1开始
    {
        if (getn < 1 || getn > allData.size())
            return false;
        else
            nowGet = getn - 1;
        return true;
    }

    public boolean Change_to(int getn)// 获得元素属性值,从0开始
    {
        if (getn < 0 || getn > allData.size() - 1)
            return false;
        else
            nowGet = getn;
        return true;
    }

    /*
     * 方法3： 获得所有属性名 (未完成) 入参：无 返回：Vector<String>
     */
    public Set<String> GetAllAttrName() {
        return allData.get(nowGet).keySet();
    }
}
