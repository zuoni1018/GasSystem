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

	private String bookNo;// 账册编号
	private String estateNo;// 小区编号
	private String bookName;// 账册名称
	private String staffNo;// 抄表工编号
	private String Remark;// 备注
	private String meterTypeNo;// 表计类型编号，表计类型编号只允许为“05”（无线表），“10”（摄像表）“04”（IC无线）

}
