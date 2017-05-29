package com.mk.service;

public class UserServiceImplHelper {

	public static String signup(String emailId, String userName, String password) {

		StringBuffer buffer = new StringBuffer();

		buffer.append("<html>");
		buffer.append("<p>Hi " + userName + ",</p>");
		buffer.append("<p>    UserName : " + userName + " </p>");
		buffer.append("<p>    Password : " + password + "</p>");
		buffer.append("<p></p>");
		buffer.append("<p></p>");
		buffer.append("<p>Thanks,</p>");
		buffer.append("<p>Support Team</p>");
		buffer.append("</html>");
		return buffer.toString();
	}

	public static String forgotPassword(String emailId, String userName, String password) {

		StringBuffer buffer = new StringBuffer();

		buffer.append("<html>");
		buffer.append("<p>Hi " + userName + ",</p>");
		buffer.append("<p>    UserName : " + userName + " </p>");
		buffer.append("<p>    Password : " + password + "</p>");
		buffer.append("<p></p>");
		buffer.append("<p></p>");
		buffer.append("<p>Thanks,</p>");
		buffer.append("<p>TGHSupport Team</p>");
		buffer.append("</html>");
		return buffer.toString();

	}
}
