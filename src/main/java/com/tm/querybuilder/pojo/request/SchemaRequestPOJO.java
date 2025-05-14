package com.tm.querybuilder.pojo.request;

import javax.validation.constraints.NotBlank;

public class SchemaRequestPOJO {

	@NotBlank(message = "Enter Connection Id")
	private String connectionId;

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}
}
