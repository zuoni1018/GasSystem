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

	private int Id=0;// ��ˮ��
	private String meterNo="";// ��Ʊ��
	private String lastShow="";// �ϴζ���
	private String lastDosage="";// �ϴ�����
	private String currentShow="";// ���ζ���
	private String currentDosage="";// ��������
	private String unitPrice="";// ����
	private int printFlag=0;// ��ӡ��־(0-δ��ӡ 1-�Ѵ�ӡ)
	private int meterState=0;// ���״̬
	private String copyWay="";// ����ʽ(S���߳��� M�˹����볭�� G����YԶ�̳���)
	private int copyState=0;// ����״̬(0δ�� 1�ѳ�)
	private String copyTime="";// ����ʱ��
	private String copyMan="";// ����Ա����
	private String Operator="";// ����Ա
	private String operateTime="";// ����ʱ��
	private int isBalance=0;// �����־(0-δ����1-�ѽ���2-����ʧ��)
	private String Remark="";// ��ע
	private String meterName="";// �������
	private String dBm="";// �ź�ǿ��
	private String elec="";// ����

}
