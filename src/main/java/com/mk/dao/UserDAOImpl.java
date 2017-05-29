package com.mk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mk.constants.AppConstants;
import com.mk.constants.Utility;
import com.mk.exception.TGHDBException;
import com.mk.pojo.User;

public class UserDAOImpl implements UserDAO {

	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	@Override
	public User login(User user) throws TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "login");

		Connection connection = null;
		User responseUser = null;
		String sqlQuery = null;
		PreparedStatement statement = null;
		try {

			connection = Utility.getConnection();
			if (null != connection) {
				
				sqlQuery = "SELECT id,userName,emailId,phoneNumber,isAdmin,istemp FROM USER WHERE (emailId = ? or phoneNumber = ?) and (password = ? or tempPassword = ?)";
				statement = connection.prepareStatement(sqlQuery);

				statement.setString(1, user.getUserName());
				statement.setString(2, user.getUserName());
				statement.setString(3, user.getPassword());
				statement.setString(4, user.getPassword());

				ResultSet rs = statement.executeQuery();

				if (rs.next()) {

					responseUser = new User();
					responseUser.setId(rs.getInt(1));
					responseUser.setUserName(rs.getString(2));
					responseUser.setEmailId(rs.getString(3));
					responseUser.setPhoneNumber(rs.getString(4));
					responseUser.setAdmin(rs.getBoolean(5));
					responseUser.setIstemp(rs.getInt(6));
				}
				connection.close();
			}
		} catch (SQLException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tlogin\t" + e.getMessage());
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "login");
		return responseUser;
	}

	@Override
	public boolean signup(User user) throws TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "signup");

		boolean status = true;
		Connection connection = null;

		String sqlQuery = null;
		PreparedStatement statement = null;
		Date date = null;

		try {

			connection = Utility.getConnection();

			date = new Date();

			if (null != connection) {

				sqlQuery = "INSERT INTO USER(username,emailId,phonenumber,active,tempPassword,createdDate,istemp) values(?,?,?,?,?,?,?)";

				statement = connection.prepareStatement(sqlQuery);

				statement.setString(1, user.getUserName());
				statement.setString(2, user.getEmailId());
				statement.setString(3, user.getPhoneNumber());
				statement.setInt(4, 1);
				statement.setString(5, user.getPassword());
				statement.setString(6, date.toString());
				statement.setInt(7, 1);

				status = statement.execute();

				connection.close();
			}

		} catch (SQLException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tsignup\t" + e.getMessage());
			if(e.getSQLState().equals("23000")){
				throw new TGHDBException(503, "EmailId or Phone Number already exist!");	
			}else{
				throw new TGHDBException(503, "Database Error occured : " + e.getMessage().trim());
				
			}
			
		}

		logger.info(AppConstants.ENDMETHOD + "signup");
		return status;
	}

	@Override
	public int changePassword(User user) throws TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "changePassword");

		Connection connection = null;
		int status = 0;
		String sqlQuery = null;
		PreparedStatement statement = null;
		try {
			connection = Utility.getConnection();
			if (null != connection) {
				sqlQuery = "update user set password = ? where id =? and password=?";
				statement = connection.prepareStatement(sqlQuery);
				statement.setString(1, user.getNewPassword());
				statement.setInt(2, user.getId());
				statement.setString(3, user.getPassword());
				status = statement.executeUpdate();
				connection.close();
			}

		} catch (SQLException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tchangePassword\t" + e.getMessage());
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "changePassword");
		return status;
	}

	@Override
	public int setPassword(User user) throws TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "setPassword");

		Connection connection = null;
		int status = 0;
		String sqlQuery = null;
		PreparedStatement statement = null;

		try {

			connection = Utility.getConnection();

			if (null != connection) {
				sqlQuery = "update user set password = ?,tempPassword='null',istemp=0 where id =?";
				statement = connection.prepareStatement(sqlQuery);
				statement.setString(1, user.getPassword());
				statement.setInt(2, user.getId());

				status = statement.executeUpdate();
				connection.close();
			}

		} catch (SQLException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tsetPassword\t" + e.getMessage());
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "setPassword");
		return status;
	}

	@Override
	public User forgotPassword(User user) throws TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "forgotPassword");

		Connection connection = null;
		int status = 0;
		String sqlQuery = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		User userresponse = null;
		try {

			connection = Utility.getConnection();

			if (null != connection) {

				sqlQuery = "select  id,userName,emailId from USER  where emailId = ? or phoneNumber = ?";
				statement = connection.prepareStatement(sqlQuery);

				statement.setString(1, user.getUserName());
				statement.setString(2, user.getUserName());

				rs = statement.executeQuery();
				
				if (rs.next()) {

					userresponse = new User();

					userresponse.setId(rs.getInt(1));
					userresponse.setUserName(rs.getString(2));
					userresponse.setEmailId(rs.getString(3));

				}

				if (null != userresponse && userresponse.getId() > 0) {
					sqlQuery = "update user set password = 'null',istemp=1,tempPassword=? where id = ?";
					statement = connection.prepareStatement(sqlQuery);
					statement.setString(1, user.getTempPassword());
					statement.setInt(2, userresponse.getId());
					boolean isInserted = statement.execute();
					status = isInserted == false ? 1 : 0;
					
					if(status == 0){
						userresponse = null; 
					}
				}
				connection.close();
			}
		} catch (SQLException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tforgotPassword\t" + e.getMessage());
			throw new TGHDBException(503,"unable to Recover Password!");
		}

		logger.info(AppConstants.ENDMETHOD + "forgotPassword");
		return userresponse;
	}

	@Override
	public int updateUserInformation(User user) throws TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "updateUserINformation");

		Connection connection = null;
		int status = 0;
		String sqlQuery = null;
		PreparedStatement statement = null;
		Date date = new Date();
		try {
			connection = Utility.getConnection();
			if (null != connection) {
				sqlQuery = "update user set firstName = ?,lastName=?,dob=?,gender=?,address=?,modifiedDate = ? where id =? ";
				statement = connection.prepareStatement(sqlQuery);
				statement.setString(1, user.getFirstName());
				statement.setString(2, user.getLastName());
				statement.setString(3, user.getDob());
				statement.setString(4, user.getGender());
				statement.setString(5, user.getAddress());
				statement.setString(6, date.toString());
				statement.setInt(7, user.getId());
				status = statement.executeUpdate();
				connection.close();
			}
		} catch (SQLException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tupdateUserInformation\t" + e.getMessage());
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "updateUserInformation");
		return status;
	}

	public static void main(String[] args) {

		UserDAOImpl dao = new UserDAOImpl();
		User user = new User();
		user.setUserName("kirya1");
		user.setEmailId("kirangaraddi33@gmail.com");
		user.setPhoneNumber("9964269233");
		try {
			boolean status = dao.signup(user);
			System.out.println(status);
		} catch (TGHDBException e) {
			System.out.println(e.getErrorMessage());
		}
	}

	
	@Override
	public User getUserInformation(int id) throws TGHDBException {
	
		logger.info(AppConstants.STARTMETHOD + "getUserInformation");

		Connection connection = null;
		String sqlQuery = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		User userresponse = null;
		try {

			connection = Utility.getConnection();

			if (null != connection) {
				sqlQuery = "select firstName,lastName,address,phoneNumber,emailId from USER  where id = ?";
				statement = connection.prepareStatement(sqlQuery);
				statement.setInt(1, id);
				rs = statement.executeQuery();
				if (rs.next()) {
					userresponse = new User();
					userresponse.setId(id);
					userresponse.setFirstName(rs.getString(1));
					userresponse.setLastName(rs.getString(2));
					userresponse.setAddress(rs.getString(3));
					userresponse.setPhoneNumber(rs.getString(4));
					userresponse.setEmailId(rs.getString(5));
				}
				connection.close();
			}
		} catch (SQLException e) {
			logger.error(AppConstants.ERRORMETHOD + "\t getUserInformation \t" + e.getMessage());
			throw new TGHDBException(503,"user information" + e.getMessage());
		}

		logger.info(AppConstants.ENDMETHOD + "getUserInformation");
		return userresponse;
	}
}
