package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/9/18
 * �޸ı�Ǳ�Ż����ۼ���
 */

public class HtGetMessageChangeBookNoOrCumulant extends HtGetMessage {

    private String newBookNo;//�±��ַ
    private String operationResult="55";//�������

    public String getNewBookNo() {
        return newBookNo;
    }

    public void setNewBookNo(String newBookNo) {
        this.newBookNo = newBookNo;
    }

    public String getOperationResult() {
        if(operationResult.equals("55")){
            return "ʧ��";
        }else {
            return "�ɹ�";
        }
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    @Override
    public String getResult() {
        String result="";
        result+="\n��������:"+getCommandType();
        result+="\n�±��:"+getNewBookNo();
        result+="\n�����Ƿ�ɹ�:"+getOperationResult();
        return result;
    }
}
