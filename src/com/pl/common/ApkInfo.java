package com.pl.common;

import java.io.Serializable;

/**
 * ������Ϣ
 * 
 * @author��
 * @date��
 */
public class ApkInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String downloadUrl; // ���ص�ַ
	private String apkVersion; // apk�汾
	private String apkSize; // apk�ļ���С
	private int apkCode; // apk�汾��(���±ر�)
	private String apkName; // apk����
	private String apkLog; // apk������־

	public ApkInfo(String downloadUrl, String apkVersion, String apkSize,
			int apkCode, String apkName, String apkLog) {
		super();
		this.downloadUrl = downloadUrl;
		this.apkVersion = apkVersion;
		this.apkSize = apkSize;
		this.apkCode = apkCode;
		this.apkName = apkName;
		this.apkLog = apkLog;
	}

	/**
	 * @return the downloadUrl
	 */
	public String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * @param downloadUrl
	 *            the downloadUrl to set
	 */
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	/**
	 * @return the apkVersion
	 */
	public String getApkVersion() {
		return apkVersion;
	}

	/**
	 * @param apkVersion
	 *            the apkVersion to set
	 */
	public void setApkVersion(String apkVersion) {
		this.apkVersion = apkVersion;
	}

	/**
	 * @return the apkSize
	 */
	public String getApkSize() {
		return apkSize;
	}

	/**
	 * @param apkSize
	 *            the apkSize to set
	 */
	public void setApkSize(String apkSize) {
		this.apkSize = apkSize;
	}

	/**
	 * @return the apkLog
	 */
	public String getApkLog() {
		return apkLog;
	}

	/**
	 * @param apkLog
	 *            the apkLog to set
	 */
	public void setApkLog(String apkLog) {
		this.apkLog = apkLog;
	}

	/**
	 * @return the apkName
	 */
	public String getApkName() {
		return apkName;
	}

	/**
	 * @param apkName
	 *            the apkName to set
	 */
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	/**
	 * @return the apkCode
	 */
	public int getApkCode() {
		return apkCode;
	}

	/**
	 * @param apkCode
	 *            the apkCode to set
	 */
	public void setApkCode(int apkCode) {
		this.apkCode = apkCode;
	}

}
