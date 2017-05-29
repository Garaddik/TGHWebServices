package com.mk.cryptography;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mk.constants.AppConstants;
import com.mk.exception.TGHException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Cryptography {

	private static SecureRandom random = new SecureRandom();

	private static final String ALGORITHM = "AES";

	private static final byte[] keyValue = new byte[] { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't',
			'K', 'e', 'y' };
	private static final Logger logger = LoggerFactory.getLogger(Cryptography.class);

	public static String encrypt(String valueToEnc) throws TGHException {

		Key key = generateKey();
		Cipher c;
		String encryptedValue = null;

		try {

			c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encValue = c.doFinal(valueToEnc.getBytes());
			encryptedValue = new BASE64Encoder().encode(encValue);

		} catch (NoSuchAlgorithmException e) {
			logger.error(AppConstants.ERRORMETHOD + " encrypt " + e.getMessage());
			throw new TGHException(500, e.getMessage());
		} catch (NoSuchPaddingException e) {
			logger.error(AppConstants.ERRORMETHOD + " encrypt " + e.getMessage());
			throw new TGHException(500, e.getMessage());
		} catch (InvalidKeyException e) {
			logger.error(AppConstants.ERRORMETHOD + " encrypt " + e.getMessage());
			throw new TGHException(500, e.getMessage());
		} catch (IllegalBlockSizeException e) {
			logger.error(AppConstants.ERRORMETHOD + " encrypt " + e.getMessage());
			throw new TGHException(500, e.getMessage());
		} catch (BadPaddingException e) {
			logger.error(AppConstants.ERRORMETHOD + " encrypt " + e.getMessage());
			throw new TGHException(500, e.getMessage());
		}
		return encryptedValue;

	}

	public static String decrypt(String encryptedValue) throws TGHException{

		Key key = generateKey();
		Cipher c;
		String decryptedValue = null;

		try {
			c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
			byte[] decValue = c.doFinal(decordedValue);
			decryptedValue = new String(decValue);

		} catch (NoSuchAlgorithmException e) {
			logger.error(AppConstants.ERRORMETHOD + " Decrypt " + e.getMessage());
			throw new TGHException(500, e.getMessage());
		} catch (NoSuchPaddingException e) {
			logger.error(AppConstants.ERRORMETHOD + " Decrypt " + e.getMessage());
			throw new TGHException(500, e.getMessage());
		} catch (InvalidKeyException e) {
			logger.error(AppConstants.ERRORMETHOD + " Decrypt " + e.getMessage());
			throw new TGHException(500, e.getMessage());
		} catch (IOException e) {
			logger.error(AppConstants.ERRORMETHOD + " Decrypt " + e.getMessage());
			throw new TGHException(500, e.getMessage());
		} catch (IllegalBlockSizeException e) {
			logger.error(AppConstants.ERRORMETHOD + " Decrypt " + e.getMessage());
			throw new TGHException(500, e.getMessage());
		} catch (BadPaddingException e) {
			logger.error(AppConstants.ERRORMETHOD + " Decrypt " + e.getMessage());
			throw new TGHException(500, e.getMessage());
		}
		return decryptedValue;

	}

	private static Key generateKey() {
		logger.info(AppConstants.STARTMETHOD + "generateKey");
		Key key = new SecretKeySpec(keyValue, ALGORITHM);
		logger.info(AppConstants.ENDMETHOD + "generateKey");
		return key;
	}

	public static String generateTempPassword() {
		logger.info(AppConstants.STARTMETHOD + "nextTempPassword");
		String randomString = new BigInteger(130, random).toString(35);
		String tempPassword = null;
		do {
			tempPassword = randomString.substring(0, 4);
			randomString = new BigInteger(130, random).toString(35);
		} while (null != randomString && randomString.length() < 4);
		if (null != tempPassword) {
			tempPassword = tempPassword.toUpperCase();
		}
		logger.info(AppConstants.ENDMETHOD + "nextTempPassword");
		return tempPassword;
	}
}
