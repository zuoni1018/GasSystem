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

	// ��Ӱ���Ϣ
	public long addGroupBind(GroupBind gpBind) {
		return groupBindDao.addGroupBind(gpBind);
	}

	public int removeGroupBind(String meterNo, String groupNo) {
		return groupBindDao.removeGroupBind(meterNo, groupNo);
	}

	// ��ÿ������а󶨱��
	public ArrayList<GroupBind> getGroupBindAll() {
		return groupBindDao.getGroupBindAll();
	}

	// ���ݷ����ɾ������Ϣ
	public int removeGroupBindByGroupNo(String groupNo) {
		return groupBindDao.removeGroupBindByGroupNo(groupNo);
	}

	// ���ݷ����Ų�ѯ���
	public ArrayList<String> getMeterNoByGroupNo(String groupNo) {
		return groupBindDao.getMeterNoByGroupNo(groupNo);
	}

	// ɾ�����а���Ϣ
	public int removeGroupBindAll() {
		return groupBindDao.removeGroupBindAll();
	}
}
