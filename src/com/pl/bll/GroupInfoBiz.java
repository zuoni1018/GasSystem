package com.pl.bll;

import java.util.ArrayList;

import com.pl.dal.GroupInfoDao;
import com.pl.entity.GroupInfo;

import android.content.Context;

public class GroupInfoBiz {

	private GroupInfoDao groupInfoDao;

	public GroupInfoBiz(Context context) {
		groupInfoDao = new GroupInfoDao(context);
	}

	// ��ѯ����
	public ArrayList<GroupInfo> getGroupInfos(String BookNo) {
		return groupInfoDao.getGroupInfos(BookNo);
	}

	// ����˲���Ϣ
	public long addGroupInfo(GroupInfo gpInfo) {
		return groupInfoDao.addGroupInfo(gpInfo);
	}

	// �����˲���Ϣ
	public int updateGroupInfo(GroupInfo gpInfo) {
		return groupInfoDao.updateGroupInfo(gpInfo);
	}

	// ɾ���˲���Ϣ
	public int removeGroupInfo(String groupNo) {
		return groupInfoDao.removeGroupInfo(groupNo);
	}

	public String getLastGroupNo() {
		return groupInfoDao.getLastGroupNo();
	}

	public String getMeterTypeNoByGroupNo(String groupNo) {
		return groupInfoDao.getMeterTypeNoByGroupNo(groupNo);
	}

	public int removeGroupInfoByBookNo(String bookNo) {
		return groupInfoDao.removeGroupInfoByBookNo(bookNo);
	}

	// ɾ�����з�����Ϣ
	public int removeGroupInfoAll() {
		return groupInfoDao.removeGroupInfoAll();
	}
}
