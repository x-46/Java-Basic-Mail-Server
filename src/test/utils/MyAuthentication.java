package test.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import utils.userHandling.Authentication;

public class MyAuthentication implements Authentication {

	private MySQLConnection sql;

	public MyAuthentication() {
		sql = new MySQLConnection(MySQLConnection.ip, MySQLConnection.port, MySQLConnection.name,
				MySQLConnection.password, MySQLConnection.db);
		sql.connect();
		sql.setupServer();
	}

	@Override
	public boolean user(String mail, String password) {
		return sql.authenticateUser(mail.toLowerCase(), password);
	}

	public static String encryptThisString(String input) {
		try {
			// getInstance() method is called with algorithm SHA-512
			MessageDigest md = MessageDigest.getInstance("SHA-512");

			// digest() method is called
			// to calculate message digest of the input string
			// returned as array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);

			// Add preceding 0s to make it 32 bit
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}

			// return the HashText
			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
