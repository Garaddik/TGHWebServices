package com.mk.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mk.constants.AppConstants;
import com.mk.constants.Utility;
import com.mk.exception.TGHDBException;
import com.mk.exception.TGHException;
import com.mk.service.UserService;
import com.mk.service.UserServiceImpl;

@Path("/users")
public class UserControllerImpl {

	// @Autowired
	UserService service = new UserServiceImpl();

	private final Logger logger = LoggerFactory.getLogger(UserControllerImpl.class);

	@GET
	@Path("/testing")
	@Produces(MediaType.APPLICATION_JSON)
	public String testing(String request) {
		logger.info(AppConstants.STARTMETHOD + "testing");
		logger.info(AppConstants.ENDMETHOD + "testing");
		return "{\"message\":\"1000\"}";

	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String login(String request) {

		logger.info(AppConstants.STARTMETHOD + "login");
		logger.info(request);
		String response = null;

		try {
			response = service.login(request);

		} catch (TGHException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tlogin\t" + e.getStatus() + "\t" + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tupdateUserInformation\t" + e.getStatus() + "\t"
					+ e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}

		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "login");
		return response;
	}

	@POST
	@Path("/signup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String signup(String request) {

		logger.info(AppConstants.STARTMETHOD + "singup");
		logger.info(request);
		String response = null;

		try {
			response = service.signup(request);

		} catch (TGHException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tsignup\t" + e.getStatus() + "\t" + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tupdateUserInformation\t" + e.getStatus() + "\t"
					+ e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}
		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "signup");
		return response;
	}

	@POST
	@Path("/change")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String changePassword(String request) {

		logger.info(AppConstants.STARTMETHOD + "changePassword");
		logger.info(request);
		String response = null;

		try {
			response = service.changePassword(request);

		} catch (TGHException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tchangePassword\t" + e.getStatus() + "\t" + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tupdateUserInformation\t" + e.getStatus() + "\t"
					+ e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}

		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "changePassword");
		return response;
	}

	@POST
	@Path("/set")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String setPassword(String request) {

		logger.info(AppConstants.STARTMETHOD + "setPassword");
		logger.info(request);
		String response = null;

		try {
			response = service.setPassword(request);

		} catch (TGHException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tsetPassword\t" + e.getStatus() + "\t" + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tupdateUserInformation\t" + e.getStatus() + "\t"
					+ e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}

		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "setPassword");
		return response;
	}

	@POST
	@Path("/forgotpassword")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String forgotPassword(String request) {

		logger.info(AppConstants.STARTMETHOD + "forgotPassword");
		logger.info(request);
		String response = null;

		try {
			response = service.forgotPassword(request);
		} catch (TGHException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tforgotPassword\t" + e.getStatus() + "\t" + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tupdateUserInformation\t" + e.getStatus() + "\t"
					+ e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}

		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "forgotPassword");
		return response;
	}

	@POST
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateUserInformation(String request) {

		logger.info(AppConstants.STARTMETHOD + "updateUserInformation");
		logger.info(request);

		String response = null;

		try {
			response = service.updateUserInformation(request);
		} catch (TGHException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tupdateUserInformation\t" + e.getStatus() + "\t"
					+ e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tupdateUserInformation\t" + e.getStatus() + "\t"
					+ e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}
		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "updateuserInformation");
		return response;
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getUserInformation(@PathParam("id") int id) {
		logger.info(AppConstants.STARTMETHOD + "getUserInformation");
		logger.info("user ID : " + id);
		String response = null;

		try {
			response = service.getUserInformation(id);

		} catch (TGHException e) {
			logger.error(
					AppConstants.ERRORMETHOD + "\tgetuserinformation\t" + e.getStatus() + "\t" + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.error(
					AppConstants.ERRORMETHOD + "\tgetUserInformation\t" + e.getStatus() + "\t" + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}

		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "getUserInformation");
		return response;
	}
}
