package com.pl.cxml;

import java.util.Set;
import java.util.Vector;
import java.util.HashMap;

public class CXmlData {
    private Vector<HashMap<String, String>> allData;
    private int nowSet = -1, nowGet = 0;// ��Ԫ����-1.size-1
    private String defaultAttrName;

    public CXmlData() {
        allData = new Vector<HashMap<String, String>>();
    }

    public void AddOneElement()// ����һ��Ԫ��
    {
        allData.add(new HashMap<String, String>());
        ++nowSet;
    }

    public void AddOneAttr(String k, String v)// ���ӵ�ǰԪ��һ������
    {
        if (allData.size() == 0)
            AddOneElement();
        allData.get(nowSet).put(k, v);
    }

    public boolean AddOneAttr(int n, String k, String v)// ��n��Ԫ��һ�����ԣ�(n��0��ʼ)
    {
        if (allData.size() == 0)
            AddOneElement();
        if (n < 0 || n > allData.size() - 1)
            return false;
        allData.get(n).put(k, v);
        return true;

    }

    public int GetSumOfElement()// ���Ԫ�ظ���
    {
        return allData.size();
    }

    public void SetDefaultAttrName(String name)// ���ò�ѯʱĬ�ϵ�������,��GetAttr���������ʱʹ�����ֵ��Ϊ��ֵ
    {
        defaultAttrName = name;
    }

    /*
     * ����1�� ��õ�ǰԪ������ֵ ��Σ�Ԫ�����ԣ����û�в������Ϊ""����Ϊ"."���򷵻����Ԫ�ص����֡�(�緵�� "p0000"
     * ���أ����Ե�ֵ,���û�У��ͷ���""
     */
    public String GetAttr()// ���Ԫ������ֵ
    {
        return GetAttr(defaultAttrName);
    }

    public String GetAttr(String key)// ���Ԫ������ֵ
    {
        if (key == "" || key == null)
            key = ".";
        if (allData.size() == 0)
            return null;
        return allData.get(nowGet).get(key);
    }

    /*
     * ����2�� �л����ڼ���Ԫ��,��1��ʼ ��Σ��ڼ���Ԫ�� ���أ����Ե�ֵ,���Ϊfalse,��ʾ�Ѿ�û�������ˡ�
     */
    public boolean ChangeTo(int getn)// ���Ԫ������ֵ,��1��ʼ
    {
        if (getn < 1 || getn > allData.size())
            return false;
        else
            nowGet = getn - 1;
        return true;
    }

    public boolean Change_to(int getn)// ���Ԫ������ֵ,��0��ʼ
    {
        if (getn < 0 || getn > allData.size() - 1)
            return false;
        else
            nowGet = getn;
        return true;
    }

    /*
     * ����3�� ������������� (δ���) ��Σ��� ���أ�Vector<String>
     */
    public Set<String> GetAllAttrName() {
        return allData.get(nowGet).keySet();
    }
}
