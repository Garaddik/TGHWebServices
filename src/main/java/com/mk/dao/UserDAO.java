package com.mk.dao;

import com.mk.exception.TGHDBException;
import com.mk.pojo.User;

public interface UserDAO {

	User login(User user) throws TGHDBException;

	boolean signup(User user) throws TGHDBException;

	int changePassword(User user) throws TGHDBException;
	
	int setPassword(User user) throws TGHDBException;
	
	User forgotPassword(User user) throws TGHDBException;
	
	int updateUserInformation(User user) throws TGHDBException;

	User getUserInformation(int id) throws TGHDBException;
}
