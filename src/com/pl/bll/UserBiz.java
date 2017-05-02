package com.pl.bll;

import android.content.Context;

import com.pl.dal.UserDao;
import com.pl.entity.User;

public class UserBiz {

	private UserDao dao;

	public UserBiz(Context context) {
		dao = new UserDao(context);
	}

	public boolean exists(User user) {
		return dao.exists(user);
	}
}
