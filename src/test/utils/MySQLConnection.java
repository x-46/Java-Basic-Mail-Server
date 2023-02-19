package test.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection extends SQLConnection {
	
	public static final String ip = ""; 
	public static final int port = 3306; 
	public static final String name = ""; 
	public static final String password = ""; 
	public static final String db = "";

	public MySQLConnection(String ip, int port, String name, String password, String db) {
		super(ip, port, name, password, db);
	}
	
	public void setupServer() {
		try {
			Statement stm = connection.createStatement();
			stm.executeUpdate("CREATE TABLE IF NOT EXISTS `User` (\n"
					+ " `id` int NOT NULL AUTO_INCREMENT,\n"
					+ " `userName` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'full mail address [name@domain.xyz]',\n"
					+ " `password` text COLLATE utf8_bin NOT NULL COMMENT 'hash algorithm sha512 used',\n"
					+ " PRIMARY KEY (`id`)\n"
					+ ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin");
			
		/*	stm = connection.createStatement();
			stm.executeUpdate("CREATE TABLE IF NOT EXISTS `Alias` (\n"
					+ " `userID` int NOT NULL,\n"
					+ " `mail` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'full mail address [name@domain.xyz]',\n"
					+ " KEY `Alias_ibfk_1` (`userID`),\n"
					+ " CONSTRAINT `Alias_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `User` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT\n"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin");
			*/

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	public String getUserMainMail(String aliasMail) {

		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `Alias` JOIN `User` WHERE userID = id AND (mail = ? or userName = ?)", ResultSet.TYPE_SCROLL_SENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, aliasMail);
			stmt.setString(2, aliasMail);

			ResultSet rs = stmt.executeQuery();
			
			rs.last();
			int  size = rs.getRow();
			rs.beforeFirst();
			
			
			if(size != 0) {
				rs.next();
				return rs.getString("userName");
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}*/
	

	
	public boolean checkMail(String mail) {
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `User` WHERE `userName` = ?", ResultSet.TYPE_SCROLL_SENSITIVE, 
			        ResultSet.CONCUR_UPDATABLE);
		
			stmt.setString(1, mail);

			ResultSet rs = stmt.executeQuery();
			
			rs.last();
			int  size = rs.getRow();
			rs.beforeFirst();
			
			
			if(size != 0) {
				return true;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	public boolean authenticateUser(String mail, String password) {
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `User` WHERE `userName` = ? AND `password` = ?", ResultSet.TYPE_SCROLL_SENSITIVE, 
			        ResultSet.CONCUR_UPDATABLE);
		
			stmt.setString(1, mail);
			stmt.setString(2, password);

			ResultSet rs = stmt.executeQuery();
			
			rs.last();
			int  size = rs.getRow();
			rs.beforeFirst();
			
			
			if(size != 0) {
				return true;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
