package test.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// https://dev.mysql.com/downloads/connector/j/

public class SQLConnection {
	
	private String name; 
	private String password;
	private String url;
	
	protected Connection connection;
	
	public SQLConnection(String ip, int port, String name, String password, String db) {
		this.name = name; 
		this.password = password;
		this.url = "jdbc:mysql://" + ip + ":" + port + "/" + db;
	}
	
	public void connect() {
		 try {
			connection = DriverManager.getConnection(url, name, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
}
