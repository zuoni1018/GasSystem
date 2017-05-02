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

	private String groupNo=""; // 分组编号
	private String estateNo=""; // 小区编号
	private String groupName=""; // 分组名称
	private String Remark=""; // 备注
	private String meterTypeNo=""; // 表计类型编号,表计类型编号只允许为“05”（无线表），“06”（机械表）“04”（IC无线）
	private String bookNo=""; // 账册编号
}
