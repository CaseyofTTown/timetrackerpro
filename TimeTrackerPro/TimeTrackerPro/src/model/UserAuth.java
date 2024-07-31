package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class UserAuth {
	private DatabaseManager db;

	public UserAuth(DatabaseManager db) {
		this.db = db;
	}

	public void registerUser(String username, String password, int pin) {
		String salt = generateSalt();
		String hashedPassword = hashPassword(password, salt);
		System.out.println("generating salt, hashing password");
		db.registerUser(username, hashedPassword, salt, pin);
	}
	
	public boolean authenticateUser(String username, String password) {
		//retrieve the salt and hashed password from the database
		String[] saltAndHashedPassword = db.getSaltAndHashedPassword(username);
		if(saltAndHashedPassword == null) {
			//user not found
			System.out.println("Error, user not found in db");
			return false;
		}
		String salt = saltAndHashedPassword[0];
		String storedHashedPassword = saltAndHashedPassword[1];
		
		//Hash the entered password with the retrieved salt
		String hashedPassword = hashPassword(password, salt);
		
		//Compare the hashedpassword with the stored hashed password
		return hashedPassword.equals(storedHashedPassword);
	}

	// salt password to prevent rainbow table attack
	private String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	private String hashPassword(String password, String salt) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashedBytes = md.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for(byte b : hashedBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	//if user forgets password, use pin to reset it 
	public boolean resetPasswordWithPin(String username, int pin, String newPassword) {
		//retrieve the stored pin from the database
		int storedPin = db.getPin(username);
		if(storedPin == pin) {
			//if the entered pin matches the stored pin, reset the password
			String newSalt = generateSalt();
			String newHashedPassword = hashPassword(newPassword, newSalt);
			db.updatePasswordAndSalt(username, newHashedPassword, newSalt);
			return true;
		} else {
			//if the entered pin does not match the stored pin, return false
			return false;
		}
	}

}
