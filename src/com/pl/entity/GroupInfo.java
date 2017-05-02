package com.pl.entity;

import java.io.Serializable;

public class GroupInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5135814151659962695L;

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public String getEstateNo() {
		return estateNo;
	}

	public void setEstateNo(String estateNo) {
		this.estateNo = estateNo;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getMeterTypeNo() {
		return meterTypeNo;
	}

	public void setMeterTypeNo(String meterTypeNo) {
		this.meterTypeNo = meterTypeNo;
	}

	public String getBookNo() {
		return bookNo;
	}

	public void setBookNo(String bookNo) {
		this.bookNo = bookNo;
	}

	private String groupNo=""; // ������
	private String estateNo=""; // С�����
	private String groupName=""; // ��������
	private String Remark=""; // ��ע
	private String meterTypeNo=""; // ������ͱ��,������ͱ��ֻ����Ϊ��05�������߱�����06������е����04����IC���ߣ�
	private String bookNo=""; // �˲���
}
