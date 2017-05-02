package com.pl.entity;

import java.io.Serializable;

public class BookInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7275518504544756071L;

	public String getBookNo() {
		return bookNo;
	}

	public void setBookNo(String bookNo) {
		this.bookNo = bookNo;
	}

	public String getEstateNo() {
		return estateNo;
	}

	public void setEstateNo(String estateNo) {
		this.estateNo = estateNo;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
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

	private String bookNo;// �˲���
	private String estateNo;// С�����
	private String bookName;// �˲�����
	private String staffNo;// �������
	private String Remark;// ��ע
	private String meterTypeNo;// ������ͱ�ţ�������ͱ��ֻ����Ϊ��05�������߱�����10�����������04����IC���ߣ�

}
