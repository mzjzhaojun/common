package com.yt.app.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestConnection {
	private static TestConnection getcon;

	private TestConnection() {
		init();
	}

	public static TestConnection getInstance() {
		if (getcon == null) {
			getcon = new TestConnection();
		}
		return getcon;
	}

	private static String drivers;
	private static String url;
	private static String username;
	private static String password;
	private static String dbType;
	public static String dbName;
	public static String filePath;
	public static String basePage;
	public static String commndPage;
	public static String osName;
	public static String version;

	private static String FILE_PATH_NAME = "/config/mysql.properties";

	private void init() {
		try {
			String filePaths = TestConnection.class.getResource(FILE_PATH_NAME).getPath();
			File file = new File(filePaths);
			InputStream in = new FileInputStream(file);
			Properties props = new Properties();
			props.load(in);
			in.close();
			drivers = props.getProperty("mysql.masterdriver");
			url = props.getProperty("mysql.masterjdbcurl");
			username = props.getProperty("mysql.masteruser");
			password = props.getProperty("mysql.masterpassword");
			dbType = props.getProperty("mysql.type");
			dbName = props.getProperty("mysql.dbName");
			osName = props.getProperty("mysql.osName");
			version = props.getProperty("mysql.version");
			filePath = props.getProperty("mysql.jdbcUrl");
			commndPage = props.getProperty("mysql.user");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Connection getCon() {
		if (drivers == null || drivers.equals("")) {
			init();
		}
		Connection conn = null;
		try {
			Class.forName(drivers);
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public String getType() {
		return dbType;
	}
}
