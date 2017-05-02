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

	// ��ѯ�����˲�
	public ArrayList<BookInfo> getBookInfos() {
		return bookInfoDao.getBookInfos();
	}

	// ����˲���Ϣ
	public long addBookInfo(BookInfo bkInfo) {
		return bookInfoDao.addBookInfo(bkInfo);
	}

	// �����˲���Ϣ
	public int updateBookInfo(BookInfo bkInfo) {
		return bookInfoDao.updateBookInfo(bkInfo);
	}

	// ɾ���˲���Ϣ
	public int removeBookInfo(String bookNo) {
		return bookInfoDao.removeBookInfo(bookNo);
	}

	public String getLastBookNo() {
		return bookInfoDao.getLastBookNo();
	}

	public BookInfo getBookInfoByBookNo(String bookNo) {
		return bookInfoDao.getBookInfoByBookNo(bookNo);
	}

	// ɾ�������˲���Ϣ
	public int removeBookInfoAll() {
		return bookInfoDao.removeBookInfoAll();
	}
}
