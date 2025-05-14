package com.tm.querybuilder.pojo.response;

public class QueryBuilderResponsePOJO {

	private String message;
	private Object responseData;
	private Boolean isSuccess;

	public void response(String message, Object responseData, boolean isSuccess) {
		setMessage(message);
		setResponseData(responseData);
		setIsSuccess(isSuccess);
	}

	public QueryBuilderResponsePOJO response(String message, boolean isSuccess) {
		response(message, null, isSuccess);
		return this;
	}

	public QueryBuilderResponsePOJO errorResponse(String message) {
		response(message, null, false);
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Object getResponseData() {
		return responseData;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

}
