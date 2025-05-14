package com.tm.querybuilder.pojo.request;

import javax.validation.Valid;

import com.tm.querybuilder.pojo.FilterDataPOJO;

public class QueryBuilderRequestPOJO extends SchemaRequestPOJO {

	@Valid
	private FilterDataPOJO requestData;

	public FilterDataPOJO getRequestData() {
		return requestData;
	}

	public void setRequestData(FilterDataPOJO requestData) {
		this.requestData = requestData;
	}

}
