package com.pl.bll;

import java.util.ArrayList;

import com.pl.dal.GroupBindDao;
import com.pl.entity.GroupBind;

import android.content.Context;

public class GroupBindBiz {

	private GroupBindDao groupBindDao;

	public GroupBindBiz(Context context) {
		groupBindDao = new GroupBindDao(context);
	}

	public ArrayList<GroupBind> getGroupBindByGroupNo(String groupNo) {
		return groupBindDao.getGroupBindByGroupNo(groupNo);
	}

	public ArrayList<GroupBind> getGroupBindByGroupName(String id, String name) {
		return groupBindDao.getGroupBindByGroupName(id, name);
	}

	// 添加绑定信息
	public long addGroupBind(GroupBind gpBind) {
		return groupBindDao.addGroupBind(gpBind);
	}

	public int removeGroupBind(String meterNo, String groupNo) {
		return groupBindDao.removeGroupBind(meterNo, groupNo);
	}

	// 获得库内所有绑定表具
	public ArrayList<GroupBind> getGroupBindAll() {
		return groupBindDao.getGroupBindAll();
	}

	// 根据分组号删除绑定信息
	public int removeGroupBindByGroupNo(String groupNo) {
		return groupBindDao.removeGroupBindByGroupNo(groupNo);
	}

	// 根据分组编号查询表号
	public ArrayList<String> getMeterNoByGroupNo(String groupNo) {
		return groupBindDao.getMeterNoByGroupNo(groupNo);
	}

	// 删除所有绑定信息
	public int removeGroupBindAll() {
		return groupBindDao.removeGroupBindAll();
	}
}
