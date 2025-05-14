package com.tm.querybuilder.pojo;

public class DatabaseConnectionDTO {

	private String connectionId;
	private int connectionPort;
	private String connectionHost;
	private String connectionDriver;

	private String connectionUser;
	private String connectionPassword;
	private String connectionDb;

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	public int getConnectionPort() {
		return connectionPort;
	}

	public String getConnectionHost() {
		return connectionHost;
	}

	public String getConnectionDriver() {
		return connectionDriver;
	}

	public String getConnectionUser() {
		return connectionUser;
	}

	public String getConnectionPassword() {
		return connectionPassword;
	}

	public String getConnectionDb() {
		return connectionDb;
	}

	public void setConnectionPort(int connectionPort) {
		this.connectionPort = connectionPort;
	}

	public void setConnectionHost(String connectionHost) {
		this.connectionHost = connectionHost;
	}

	public void setConnectionDriver(String connectionDriver) {
		this.connectionDriver = connectionDriver;
	}

	public void setConnectionUser(String connectionUser) {
		this.connectionUser = connectionUser;
	}

	public void setConnectionPassword(String connectionPassword) {
		this.connectionPassword = connectionPassword;
	}

	public void setConnectionDb(String connectionDb) {
		this.connectionDb = connectionDb;
	}

}
