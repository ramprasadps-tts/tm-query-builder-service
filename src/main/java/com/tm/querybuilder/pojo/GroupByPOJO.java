package com.tm.querybuilder.pojo;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.tm.querybuilder.validation.NoWhitespaceList;

public class GroupByPOJO {
    
	@Size(min = 1, message = "Minimum One column should be in Group by column list")
	@NoWhitespaceList
	private List<String>columnList;
	
	@Valid
	private List<ConditionGroupPOJO>conditionData;
	
	public List<String> getColumnList() {
		return columnList;
	}
	public List<ConditionGroupPOJO> getConditionData() {
		return conditionData;
	}
	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}
	public void setConditionData(List<ConditionGroupPOJO> conditionGroup) {
		this.conditionData = conditionGroup;
	}
	
	
}
