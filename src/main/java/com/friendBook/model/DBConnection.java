package com.friendBook.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class DBConnection {
	
	private final Connection connection;

	public DBConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Properties prop = new Properties();
		String DB_HOST = null;
		String DB_USER = null;
		String DB_PASS = null;
		String DB_PORT = null;
		String DB_SCHEMA = null;
		InputStream input = null;
		try {
			input = new FileInputStream("C:\\Users\\User\\Desktop\\rado2\\Project-Friend-Book\\src\\main\\resources\\config.properties");
			prop.load(input);	
			DB_HOST = prop.getProperty("DB_HOST");
			DB_USER = prop.getProperty("DB_USER");
			DB_PASS = prop.getProperty("DB_PASS");
			DB_PORT = prop.getProperty("DB_PORT");
			DB_SCHEMA = prop.getProperty("DB_SCHEMA");
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		this.connection = DriverManager.getConnection(
			"jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_SCHEMA,
			DB_USER, DB_PASS);
	}

	public Connection getConnection() {
		return connection;
	}
	
}
