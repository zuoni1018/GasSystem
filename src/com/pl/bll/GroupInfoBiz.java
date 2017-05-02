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

	// 查询分组
	public ArrayList<GroupInfo> getGroupInfos(String BookNo) {
		return groupInfoDao.getGroupInfos(BookNo);
	}

	// 添加账册信息
	public long addGroupInfo(GroupInfo gpInfo) {
		return groupInfoDao.addGroupInfo(gpInfo);
	}

	// 更新账册信息
	public int updateGroupInfo(GroupInfo gpInfo) {
		return groupInfoDao.updateGroupInfo(gpInfo);
	}

	// 删除账册信息
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

	// 删除所有分组信息
	public int removeGroupInfoAll() {
		return groupInfoDao.removeGroupInfoAll();
	}
}
