package com.pl.entity;

public class CopyData {

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getMeterNo() {
		return meterNo;
	}

	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}

	public String getLastShow() {
		return lastShow;
	}

	public void setLastShow(String lastShow) {
		this.lastShow = lastShow;
	}

	public String getLastDosage() {
		return lastDosage;
	}

	public void setLastDosage(String lastDosage) {
		this.lastDosage = lastDosage;
	}

	public String getCurrentShow() {
		return currentShow;
	}

	public void setCurrentShow(String currentShow) {
		this.currentShow = currentShow;
	}

	public String getCurrentDosage() {
		return currentDosage;
	}

	public void setCurrentDosage(String currentDosage) {
		this.currentDosage = currentDosage;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getPrintFlag() {
		return printFlag;
	}

	public void setPrintFlag(int printFlag) {
		this.printFlag = printFlag;
	}

	public int getMeterState() {
		return meterState;
	}

	public void setMeterState(int meterState) {
		this.meterState = meterState;
	}

	public String getCopyWay() {
		return copyWay;
	}

	public void setCopyWay(String copyWay) {
		this.copyWay = copyWay;
	}

	public int getCopyState() {
		return copyState;
	}

	public void setCopyState(int copyState) {
		this.copyState = copyState;
	}

	public String getCopyTime() {
		return copyTime;
	}

	public void setCopyTime(String copyTime) {
		this.copyTime = copyTime;
	}

	public String getCopyMan() {
		return copyMan;
	}

	public void setCopyMan(String copyMan) {
		this.copyMan = copyMan;
	}

	public String getOperator() {
		return Operator;
	}

	public void setOperator(String operator) {
		Operator = operator;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public int getIsBalance() {
		return isBalance;
	}

	public void setIsBalance(int isBalance) {
		this.isBalance = isBalance;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	public String getdBm() {
		return dBm;
	}

	public void setdBm(String dBm) {
		this.dBm = dBm;
	}

	public String getElec() {
		return elec;
	}

	public void setElec(String elec) {
		this.elec = elec;
	}

	private int Id=0;// 流水号
	private String meterNo="";// 表计编号
	private String lastShow="";// 上次读数
	private String lastDosage="";// 上次用量
	private String currentShow="";// 本次读数
	private String currentDosage="";// 本次用量
	private String unitPrice="";// 单价
	private int printFlag=0;// 打印标志(0-未打印 1-已打印)
	private int meterState=0;// 表具状态
	private String copyWay="";// 抄表方式(S无线抄表 M人工输入抄表 G估抄Y远程抄表)
	private int copyState=0;// 抄表状态(0未抄 1已抄)
	private String copyTime="";// 抄表时间
	private String copyMan="";// 抄表员姓名
	private String Operator="";// 操作员
	private String operateTime="";// 操作时间
	private int isBalance=0;// 结算标志(0-未结算1-已结算2-结算失败)
	private String Remark="";// 备注
	private String meterName="";// 表具名称
	private String dBm="";// 信号强度
	private String elec="";// 电量

}
