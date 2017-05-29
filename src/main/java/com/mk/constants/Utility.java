package com.mk.constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mk.exception.TGHDBException;

public class Utility {

	private static final Logger logger = LoggerFactory.getLogger(Utility.class);

	static {

		try {
			logger.info("Registaring mySQL Driver");
			Class.forName("com.mysql.jdbc.Driver").newInstance();

		} catch (Exception ex) {
			logger.error("Error Occured while Registering SQL Driver Class");
			ex.printStackTrace();

		}

	}

	public static Connection getConnection() throws TGHDBException {

		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/tgh?" + "user=root&password=");

		} catch (SQLException ex) {
			logger.error(AppConstants.ERRORMETHOD + "\t" + ex.getMessage());
			throw new TGHDBException(503, ex.getMessage());
		}
		return conn;
	}

	public static String ErrorResponse(int status, String message) {

		StringBuilder builder = new StringBuilder();
		builder.append("{\"status\":");
		builder.append(status + ",");
		builder.append("\"message\":");
		builder.append("\"" + message + "\"");
		builder.append("}");
		return builder.toString();

	}

	public static String ValidResponse(int status, String message) {

		StringBuilder builder = new StringBuilder();
		builder.append("{\"status\":");
		builder.append(status + ",");
		builder.append("\"message\":");
		builder.append("\"" + message + "\"");
		builder.append("}");
		return builder.toString();

	}

	public static void main(String[] args) {

		try {

			Connection conn = Utility.getConnection();

			System.out.println(conn.toString());
		} catch (TGHDBException e) {
			e.printStackTrace();
		}
	}
}
