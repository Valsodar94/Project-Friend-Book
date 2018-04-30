package com.friendBook.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String DB_HOST = "localhost";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "235689";
	private static final String DB_PORT = "3306";
	private static final String DB_SCHEMA = "friend_book";
	
	private static DBConnection instance = null;
	private final Connection connection;

	private DBConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		this.connection = DriverManager.getConnection(
				"jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_SCHEMA,
				DB_USER, DB_PASS);
	}
	
	public synchronized static DBConnection getInstance() throws SQLException, ClassNotFoundException {
		if (instance == null) {
			instance = new DBConnection();
		}
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}
	
}
