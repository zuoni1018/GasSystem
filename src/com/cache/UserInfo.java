package com.cache;

import org.litepal.crud.DataSupport;

/**
 * Created by zangyi_shuai_ge on 2017/8/24
 */

public class UserInfo extends DataSupport {
    //ͨ����߱��ȥ��ѯ�û���Ϣ
    //һ����߱��ֻ��Ӧһ���û���Ϣ
    //ÿ��ȥ���ʱ��û������� �������

    //ͨ��
    private String tableNumber="";//��߱��
    private String userName="";//�û���
    private String address="";//��ַ
    private String userNum="";//�û����
    private String userPhone="";//�û��ֻ�����
    private String tableName="";//�������
    //����
    private String xiNingTableNumber;//������߱�ţ������ı�߸ֺ�Ϊͨ�ð�ı�߱�ţ�

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    @Override
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }



    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getXiNingTableNumber() {
        return xiNingTableNumber;
    }

    public void setXiNingTableNumber(String xiNingTableNumber) {
        this.xiNingTableNumber = xiNingTableNumber;
    }
}
