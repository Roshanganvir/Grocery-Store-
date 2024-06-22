package com.grocery.management.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class AdminUtils {
	
	
	public String makePassword(String password) {

		int length = password.length();

		StringBuilder maskedPassword = new StringBuilder();

		for (int i = 0; i < length; i++) {
			maskedPassword.append("*");
		}

		return maskedPassword.toString();
	}

	
	
	private static final String ENCRYPTION_KEY = "0123456789abcdef";
	private static final String AES_ALGORITHM = "AES";
	
	
	


	public String encryptedPassword(String password) {
		try {
			SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), AES_ALGORITHM);
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptedBytes = cipher.doFinal(password.getBytes());
			String encryptedPassword = Base64.getEncoder().encodeToString(encryptedBytes);
			return encryptedPassword;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			return "Error encrypting password";
		}
	}

	public String decryptedPassword(String password) {
		try {
			SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), AES_ALGORITHM);
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decryptedPasswordBytes = cipher.doFinal(Base64.getDecoder().decode(password));
			String decryptedPassword = new String(decryptedPasswordBytes);
			return decryptedPassword;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			return "Error decrypting password";
		}
	}

	
	
	
}
