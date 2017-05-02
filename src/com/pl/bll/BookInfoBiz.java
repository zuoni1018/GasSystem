package com.pl.bll;

import java.util.ArrayList;

import android.content.Context;

import com.pl.dal.BookInfoDao;
import com.pl.entity.BookInfo;

public class BookInfoBiz {

	private BookInfoDao bookInfoDao;

	public BookInfoBiz(Context context) {
		bookInfoDao = new BookInfoDao(context);
	}

	// 查询所有账册
	public ArrayList<BookInfo> getBookInfos() {
		return bookInfoDao.getBookInfos();
	}

	// 添加账册信息
	public long addBookInfo(BookInfo bkInfo) {
		return bookInfoDao.addBookInfo(bkInfo);
	}

	// 更新账册信息
	public int updateBookInfo(BookInfo bkInfo) {
		return bookInfoDao.updateBookInfo(bkInfo);
	}

	// 删除账册信息
	public int removeBookInfo(String bookNo) {
		return bookInfoDao.removeBookInfo(bookNo);
	}

	public String getLastBookNo() {
		return bookInfoDao.getLastBookNo();
	}

	public BookInfo getBookInfoByBookNo(String bookNo) {
		return bookInfoDao.getBookInfoByBookNo(bookNo);
	}

	// 删除所有账册信息
	public int removeBookInfoAll() {
		return bookInfoDao.removeBookInfoAll();
	}
}
