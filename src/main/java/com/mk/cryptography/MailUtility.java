package com.mk.cryptography;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mk.constants.AppConstants;
import com.mk.exception.TGHException;

public class MailUtility {

	private static final Logger logger = LoggerFactory.getLogger(MailUtility.class);

	private static final String  emailId = "mkanytime33@gmail.com";
	private static final String password = "9964269233";
	public static String sendMail(String fromaddress, String frompassword, String toaddress, String subject,
			String body) throws TGHException {
		logger.info(AppConstants.STARTMETHOD + " sendMail userMailId" + fromaddress);
		
		String status = "FAILURE";
		// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailId, password);
			}
		});

		// compose message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailId));// change accordingly
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toaddress));
			message.setSubject(subject);
			message.setContent(body,"text/html; charset=utf-8");
			//message.setText(body);

			// send message
			Transport.send(message);

			status = "SUCCESS";

		} catch (MessagingException e) {
			logger.error(
					AppConstants.ERRORMETHOD + "sendMail Error Occurred " + e.getMessage() + "MailId : " + toaddress);
			throw new TGHException(550, "Unable to send Information to " + fromaddress + "address");
		}
		logger.info(AppConstants.ENDMETHOD + " sendMail");
		return status;
	}
}
