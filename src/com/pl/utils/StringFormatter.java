package com.pl.utils;

public class StringFormatter {

	// 10Î»±àºÅ+1
	public static String getAddStringNo(String No) {
		// String oldNo = No.substring(No.lastIndexOf("0"));
		int Ino = Integer.parseInt(No) + 1;
		String newNo = "";
		if (Ino < 10) {
			newNo = "000000000" + Integer.toString(Ino);
		} else if (Ino < 100) {
			newNo = "00000000" + Integer.toString(Ino);
		} else if (Ino < 1000) {
			newNo = "0000000" + Integer.toString(Ino);
		} else if (Ino < 10000) {
			newNo = "000000" + Integer.toString(Ino);
		} else if (Ino < 100000) {
			newNo = "00000" + Integer.toString(Ino);
		}
		return newNo;
	}

	// 5Î»±àºÅ+1
	public static String getAddStringGroupNo(String No) {
		// String oldNo = No.substring(No.lastIndexOf("0"));
		int Ino = Integer.parseInt(No) + 1;
		String newNo = "";
		if (Ino < 10) {
			newNo = "0000" + Integer.toString(Ino);
		} else if (Ino < 100) {
			newNo = "000" + Integer.toString(Ino);
		} else if (Ino < 1000) {
			newNo = "00" + Integer.toString(Ino);
		} else if (Ino < 10000) {
			newNo = "0" + Integer.toString(Ino);
		}
		return newNo;
	}
}
