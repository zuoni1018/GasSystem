package com.pl.protocol;

import java.util.Vector;

import android.util.Pair;

public class CqueueData {
	private boolean isSet = false;

	private String frameStart; // 帧起始符

	// / 控制域
	private String systemId; // 系统标识
	private String addressEn; // 地址启用标识
	private String targetType; // 目标类型
	private String sourceType; // 源类型
	private String targetAdd; // 目标地址
	private String sourceAdd; // 源地址
	private int dataLength; // 数据域长度

	// / 同步符
	private String syncSymbol;

	// / 数据域
	private String cmdType; // 命令类型
	private String dataBCD; // 数据

	public double sumUseGas; // 累计用气量
	public String meterState; // 表状态
	public int signalStrength; // 信号强度
	public int powerState; // 电池电压 //表电量

	// / 校验域
	private String checkDomain;

	// / BCD时间
	private String bcdDate;

	public String getBcdDate() {
		return bcdDate;
	}

	public void setBcdDate(String bcdDate) {
		this.bcdDate = bcdDate;
	}

	// / 帧结束符
	private String frameEnd;

	private String moudleName; // 指令名称
	private Vector<Pair<String, String>> params;

	public CqueueData() {
		params = new Vector<Pair<String, String>>();
	}

	// 累计用气量
	public void setSumUseGas(double sumUseGas) {
		this.sumUseGas = sumUseGas;
	}

	public double getSumUseGas() {
		return sumUseGas;
	}

	public void setMeterState(String meterState) {
		this.meterState = meterState;
	}

	public String getMeterState() {
		return meterState;
	}

	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}

	public int getSignalStrength() {
		return signalStrength;
	}

	public void setPowerState(int powerState) {
		this.powerState = powerState;
	}

	public int getPowerState() {
		return powerState;
	}

	/***************** set *****************/

	public void setMname(String mName) {
		moudleName = mName;
	}

	public String getMname() {
		return moudleName;
	}

	public void setFrameStart(String frameS) {
		frameStart = frameS;
	}

	public void setSystemId(String sysId) {
		systemId = sysId;
	}

	public void setAddressEn(String addEn) {
		addressEn = addEn;
	}

	public void setTargetType(String tType) {
		targetType = tType;
	}

	public void setSourceType(String sType) {
		sourceType = sType;
	}

	// 目标地址 5BCD通讯号
	public void setTargetAddr(String tAdd) {
		targetAdd = tAdd;
	}

	// 源地址 5BCD通讯号
	public void setSourceAddr(String sAdd) {
		sourceAdd = sAdd;
	}

	public void setDataLength(int length) {
		dataLength = length;
	}

	public void setSyncSymbol(String symbol) {
		syncSymbol = symbol;
	}

	public void setCmdType(String cType) {
		cmdType = cType;
	}

	public void setDataBCD(String data) {
		dataBCD = data;
	}

	public void setCheckDomain(String cDomain) {
		checkDomain = cDomain;
	}

	public void setFrameEnd(String fEnd) {
		frameEnd = fEnd;
	}

	public void addParam(Pair<String, String> param) {
		params.add(param);
	}

	public void addParam(String cmd, String data) {
		params.add(new Pair<String, String>(cmd, data));
	}

	public void addParam(String cmd) {
		params.add(new Pair<String, String>(cmd, null));
	}

	public void addParams(Vector<Pair<String, String>> paras) {
		params.addAll(paras);
	}

	public void setParams(Vector<Pair<String, String>> paras) {
		params = paras;
	}

	/***************** get *****************/
	public String getFrameStart() {
		return frameStart;
	}

	public String getSystemId() {
		return systemId;
	}

	public String getAddressEn() {
		return addressEn;
	}

	public String getTargetType() {
		return targetType;
	}

	public String getSourceType() {
		return sourceType;
	}

	public String getTargetAddr() {
		return targetAdd;
	}

	public String getSourceAddr() {
		return sourceAdd;
	}

	public int getDataLength() {
		return dataLength;
	}

	public String getSyncSymbol() {
		return syncSymbol;
	}

	public String getCmdType() {
		return cmdType;
	}

	public String getDataBCD() {
		return dataBCD;
	}

	public String getCheckDomain() {
		return checkDomain;
	}

	public String getFrameEnd() {
		return frameEnd;
	}

	public Vector<Pair<String, String>> getParams() {
		return params;
	}

	public boolean isSet() {
		return isSet;
	}

	public void set() {
		isSet = true;
	}

	public void clear() {
		params.clear();
	}

	public String getCmdData() {
		return dataBCD;
		// String s = cmdType + "-" + dataBCD;
		// return s;
	}

}
