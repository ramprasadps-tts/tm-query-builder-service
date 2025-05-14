package com.tm.querybuilder.pojo;

import javax.validation.constraints.NotBlank;

import com.tm.querybuilder.enums.Condition;
import com.tm.querybuilder.enums.LogicalCondition;
import com.tm.querybuilder.validation.ObjectNoWhiteSpace;

public class ConditionPOJO {

	@NotBlank(message = "Enter column")
	private String column;

	private Condition condition;

	@ObjectNoWhiteSpace
	private Object value;
	
	
	private LogicalCondition logicalCondition;

	public String getColumn() {
		return column;
	}

	public Condition getCondition() {
		return condition;
	}

	public Object getValue() {
		return value;
	}

	public LogicalCondition getLogicalCondition() {
		return logicalCondition;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setLogicalCondition(LogicalCondition logicalCondition) {
		this.logicalCondition = logicalCondition;
	}

}
