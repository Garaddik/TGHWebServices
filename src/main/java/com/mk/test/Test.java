package com.mk.test;

import java.security.MessageDigest;
import java.util.Date;

public class Test {
	
	public static void main(String[] args) {
		
		datetest();
		//String encryptedPassword = encryption("123");
		
		//System.out.println(encryptedPassword);
		
		//String decreptedPassword = decryption(encryptedPassword);
		
	}
	
	public static void datetest(){
	
		Date date = new Date();
		
		System.out.println(date.toString());
	}
	private static String decryption(String encryptedPassword) {
		
		return null;
	}

	public static String encryption( String password){
		
		String algorithm = "SHA";
		String hipherText = null;
		
		byte[] plainText = password.getBytes();
		
		MessageDigest md = null;
		
		try {		
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		md.reset();		
		md.update(plainText);
		byte[] encodedPassword = md.digest();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				sb.append("0");
			}
			
			sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}
		if(sb.length() > 6){
			hipherText = sb.substring(0,6);
		}
		
		return hipherText;
	}
	
}
