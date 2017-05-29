package com.mk.service;

import com.mk.exception.TGHDBException;
import com.mk.exception.TGHException;

public interface UserService {

	String login(String request) throws TGHException,TGHDBException;

	String signup(String request) throws TGHException,TGHDBException;

	String changePassword(String request) throws TGHException,TGHDBException;

	String setPassword(String request) throws TGHException,TGHDBException;

	String forgotPassword(String request) throws TGHException,TGHDBException;

	String updateUserInformation(String request) throws TGHException,TGHDBException;

	String getUserInformation(int id) throws TGHException,TGHDBException;
	
}
