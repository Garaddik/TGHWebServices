package com.mk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mk.constants.AppConstants;
import com.mk.cryptography.Cryptography;
import com.mk.cryptography.MailUtility;
import com.mk.dao.UserDAO;
import com.mk.dao.UserDAOImpl;
import com.mk.exception.TGHDBException;
import com.mk.exception.TGHException;
import com.mk.pojo.Response;
import com.mk.pojo.User;

public class UserServiceImpl implements UserService {

	// @Autowired
	UserDAO userDAO = new UserDAOImpl();

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public String login(String request) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "login");

		String response = null;
		Response responseObj = null;

		try {

			Gson gson = new Gson();

			User user = gson.fromJson(request, User.class);

			if (null != user && null != user.getUserName() && null != user.getPassword()) {

				user.setPassword(Cryptography.encrypt(user.getPassword()));

				user = userDAO.login(user);

				if (null != user) {
					if (user.getIstemp() == 1) {
						responseObj = new Response(200, "SUCCESS", user);
						responseObj.setIstemp(1);
						user.setIstemp(null);

					} else {
						responseObj = new Response(200, "SUCCESS", user);
						responseObj.setIstemp(0);
						user.setIstemp(null);
					}
				} else {
					responseObj = new Response(403, "Invalid Credentials");
				}
			} else {
				responseObj = new Response(422, "InSufficient Request.");
			}
			response = gson.toJson(responseObj);

		} catch (NullPointerException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tlogin\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tsignup\t " + e.getMessage());
			throw new TGHException(422, "error occurred in Server : " + e.getMessage());
		}

		logger.info(AppConstants.ENDMETHOD + "login");
		return response;
	}

	@Override
	public String signup(String request) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "signup");

		String response = null;

		Response responseObj = null;

		boolean status = false;

		Gson gson = null;

		try {

			gson = new Gson();

			User user = gson.fromJson(request, User.class);

			if (null != user && null != user.getUserName() && !user.getUserName().isEmpty() && null != user.getEmailId()
					&& !user.getEmailId().isEmpty() && null != user.getPhoneNumber()
					&& !user.getPhoneNumber().isEmpty()) {
				String generatedPassword = Cryptography.generateTempPassword();
				String encryptedPassword = Cryptography.encrypt(generatedPassword);
				user.setPassword(encryptedPassword);
				status = userDAO.signup(user);
				if (!status) {
					String body = UserServiceImplHelper.signup(user.getEmailId(), user.getUserName(),
							generatedPassword);
					MailUtility.sendMail("mkanytime33@gmail.com","9964269233", user.getEmailId(),AppConstants.SIGNUP,body);
					logger.info("Signup : " + body);
					responseObj = new Response(200, "Please Check Your mailId to Login with tempory Credentials.");
				} else {
					responseObj = new Response(503, "Unable to Register,Please try after some time.");
				}
			} else {
				responseObj = new Response(422, "InSufficient Request.");
			}
			response = gson.toJson(responseObj);

		} catch (NullPointerException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tsignup\t " + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tsignup\t " + e.getMessage());
			throw new TGHException(422, "error occurred in Server : " + e.getMessage());
		}

		logger.info(AppConstants.ENDMETHOD + "signup");
		return response;
	}

	@Override
	public String changePassword(String request) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "changePassword");

		String response = null;
		Response responseObj = null;
		int status = 0;
		Gson gson = null;

		try {

			gson = new Gson();

			User user = gson.fromJson(request, User.class);

			status = userDAO.changePassword(user);
			if (null != user && 0 < user.getId() && null != user.getNewPassword() && null != user.getPassword()) {

				user.setNewPassword(Cryptography.encrypt(user.getNewPassword()));
				if (status > 0) {
					responseObj = new Response(200, "SUCCESS");
				} else {
					responseObj = new Response(500, "Unable to reset Password!");
				}
			} else {
				responseObj = new Response(422, "Insufficient Request");
			}
			response = gson.toJson(responseObj);
		} catch (NullPointerException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tchangePassword\t " + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tsignup\t " + e.getMessage());
			throw new TGHException(422, "error occurred in Server : " + e.getMessage());
		}

		logger.info(AppConstants.ENDMETHOD + "changePassword");
		return response;
	}

	@Override
	public String setPassword(String request) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "setPassword");

		String response = null;
		Response responseObj = null;
		int status = 0;
		Gson gson = null;

		try {

			gson = new Gson();

			User user = gson.fromJson(request, User.class);

			if (null != user && null != user.getPassword() && 0 < user.getId()) {

				user.setPassword(Cryptography.encrypt(user.getPassword()));

				status = userDAO.setPassword(user);

				if (status > 0) {
					responseObj = new Response(200, "SUCCESS");
				} else {
					responseObj = new Response(500, "Unable to reset Password!");
				}
			}
			response = gson.toJson(responseObj);
		} catch (NullPointerException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tsetPassword\t " + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tsignup\t " + e.getMessage());
			throw new TGHException(422, "error occurred in Server : " + e.getMessage());
		}

		logger.info(AppConstants.ENDMETHOD + "setPassword");
		return response;
	}

	@Override
	public String forgotPassword(String request) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "forgotPassword");

		String response = null;
		Response responseObj = null;
		Gson gson = null;
		User user = null;
		try {

			gson = new Gson();

			user = gson.fromJson(request, User.class);

			if (null != user && null != user.getUserName()) {

				String generatedPassword = Cryptography.generateTempPassword();

				String encryptedPassword = Cryptography.encrypt(generatedPassword);

				user.setTempPassword(encryptedPassword);

				User userresponse = userDAO.forgotPassword(user);

				if (null != userresponse) {

					String body = UserServiceImplHelper.forgotPassword(userresponse.getEmailId(),
							userresponse.getUserName(), generatedPassword);
					MailUtility.sendMail("mkanytime33@gmail.com","9964269233",userresponse.getEmailId(),AppConstants.FORGOTPASSWORD,body);
					logger.info("Forgot Password" + body);
					responseObj = new Response(200, "Please Check Your Email Id to login with tempory credentials");

				} else {
					responseObj = new Response(500, "Email Id or Phone Number not exist!");
				}

			} else {
				responseObj = new Response(422, "Insufficient Request");
			}
			response = gson.toJson(responseObj);

		} catch (NullPointerException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tforgotPassword\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tsignup\t " + e.getMessage());
			throw new TGHException(422, "error occurred in Server : " + e.getMessage());
		}

		logger.info(AppConstants.ENDMETHOD + "forgotPassword");
		return response;
	}

	@Override
	public String updateUserInformation(String request) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "updateUserInformation");

		String response = null;
		Response responseObj = null;
		int status = 0;
		Gson gson = null;
		User user = null;
		try {
			gson = new Gson();
			user = gson.fromJson(request, User.class);
			status = userDAO.updateUserInformation(user);

			if (status > 0) {
				responseObj = new Response(200, "SUCCESS");
			} else {
				responseObj = new Response(500, "UnAutherized Access");
			}

			response = gson.toJson(responseObj);

		} catch (NullPointerException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tupdateUserInformation\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tsignup\t " + e.getMessage());
			throw new TGHException(422, "error occurred in Server : " + e.getMessage());
		}

		logger.info(AppConstants.ENDMETHOD + "updateUserInformation");
		return response;
	}

	public static void main(String[] args) {

		UserServiceImpl service = new UserServiceImpl();
		try {
			String str = service.forgotPassword("{\"userName\":\"123456zs7890\"}");
			System.out.println(str);
		} catch (TGHDBException | TGHException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUserInformation(int id) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "getUserInformation");
		String response = null;
		Response responseObj = null;
		User user = null;

		try {
			Gson gson = new Gson();
			if (id > 0) {
				user = userDAO.getUserInformation(id);
				if (null != user) {
					responseObj = new Response(200, "SUCCESS", user);
				} else {
					responseObj = new Response(403, "Unable to get User information");
				}
			} else {
				responseObj = new Response(422, "InSufficient Request.");
			}
			response = gson.toJson(responseObj);
		} catch (NullPointerException e) {
			logger.error(AppConstants.ERRORMETHOD + "\t getUserInformation \t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.error(AppConstants.ERRORMETHOD + "\t getUserInformation \t " + e.getMessage());
			throw new TGHException(422, "error occurred in Server : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + " getUserInformation ");
		return response;
		
	}
}
