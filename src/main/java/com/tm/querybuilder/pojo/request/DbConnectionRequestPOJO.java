package com.tm.querybuilder.pojo.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;

public class DbConnectionRequestPOJO {

	@Range(min = 1, message = "Enter the Connection Port")
	private int connectionPort;
	@NotBlank(message = "Enter Connection Port")
	private String connectionHost;
	@NotBlank(message = "Enter Driver")
	private String connectionDriver;

	@NotBlank(message = "Enter User Name")
	private String databaseUser;
	@NotBlank(message = "Enter Password")
	private String databasePassword;
	@NotBlank(message = "Enter Database Name")
	private String databaseName;
	private String schemaName;

	public String getSchemaName() {
		return schemaName;
	}

	public int getConnectionPort() {
		return connectionPort;
	}

	public String getConnectionHost() {
		return connectionHost;
	}

	public void setConnectionHost(String connectionHost) {
		this.connectionHost = connectionHost;
	}

	public String getConnectionDriver() {
		return connectionDriver;
	}

	public String getDatabaseUser() {
		return databaseUser;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setConnectionPort(int connectionPort) {
		this.connectionPort = connectionPort;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public void setConnectionDriver(String connectionDriver) {
		this.connectionDriver = connectionDriver;
	}

	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

}
