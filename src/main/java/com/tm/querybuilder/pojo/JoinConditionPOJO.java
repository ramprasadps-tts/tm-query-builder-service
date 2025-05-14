package com.tm.querybuilder.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.tm.querybuilder.enums.Condition;
import com.tm.querybuilder.enums.LogicalCondition;

public class JoinConditionPOJO {

	@NotBlank(message = "Enter the LeftColumn")
	private String lsColumn;
	
	@NotNull(message = "Enter the Right column")
	private String rsColumn;

	@NotNull(message = "Enter the ConditionBuilder")
	private Condition condition;

	private LogicalCondition logicalCondition;

	public String getLsColumn() {
		return lsColumn;
	}

	
	public Condition getCondition() {
		return condition;
	}

	public LogicalCondition getLogicalCondition() {
		return logicalCondition;
	}

	public void setLsColumn(String lsColumn) {
		this.lsColumn = lsColumn;
	}

	public String getRsColumn() {
		return rsColumn;
	}


	public void setRsColumn(String rsColumn) {
		this.rsColumn = rsColumn;
	}


	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public void setLogicalCondition(LogicalCondition logicalCondition) {
		this.logicalCondition = logicalCondition;
	}

}
