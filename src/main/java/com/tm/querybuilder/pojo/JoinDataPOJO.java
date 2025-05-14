package com.tm.querybuilder.pojo;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.tm.querybuilder.enums.JoinTypes;
import com.tm.querybuilder.validation.NoWhitespaceList;

public class JoinDataPOJO {

	@Valid
	private List<JoinConditionPOJO> joinCondition;

	@NotBlank(message = "Enter the Right TableName")
	private String joinTableName;

	private JoinTypes joinType;

	// @NotEmpty(message = "String list cannot be empty")
	@Size(min = 1, message = "Minimum One column should be selected")
	@NoWhitespaceList
	private List<String> columnNames;

	@Valid
	public List<JoinConditionPOJO> getJoinCondition() {
		return joinCondition;
	}

	public String getJoinTableName() {
		return joinTableName;
	}

	public void setJoinTableName(String joinTableName) {
		this.joinTableName = joinTableName;
	}

	public JoinTypes getJoinType() {
		return joinType;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setJoinCondition(List<JoinConditionPOJO> joinCondition) {
		this.joinCondition = joinCondition;
	}

	public void setJoinType(JoinTypes joinType) {
		this.joinType = joinType;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

}
