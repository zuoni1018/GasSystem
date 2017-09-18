package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/9/18
 * 修改标记编号或者累计量
 */

public class HtGetMessageChangeBookNoOrCumulant extends HtGetMessage {

    private String newBookNo;//新表地址
    private String operationResult="55";//操作结果

    public String getNewBookNo() {
        return newBookNo;
    }

    public void setNewBookNo(String newBookNo) {
        this.newBookNo = newBookNo;
    }

    public String getOperationResult() {
        if(operationResult.equals("55")){
            return "失败";
        }else {
            return "成功";
        }
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    @Override
    public String getResult() {
        String result="";
        result+="\n命令类型:"+getCommandType();
        result+="\n新表号:"+getNewBookNo();
        result+="\n操作是否成功:"+getOperationResult();
        return result;
    }
}
