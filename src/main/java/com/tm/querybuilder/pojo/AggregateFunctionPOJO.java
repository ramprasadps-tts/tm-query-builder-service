package com.tm.querybuilder.pojo;

import javax.validation.constraints.NotBlank;

import com.tm.querybuilder.enums.AggregateTypes;

public class AggregateFunctionPOJO {

	private AggregateTypes aggregateTypes;

	@NotBlank(message = "Enter the column name for aggregate function")
	private String columnName;

	public AggregateTypes getAggregateTypes() {
		return aggregateTypes;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setAggregateTypes(AggregateTypes aggregateFunction) {
		this.aggregateTypes = aggregateFunction;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

}
