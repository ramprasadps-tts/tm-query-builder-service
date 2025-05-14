package com.tm.querybuilder.pojo;

import java.util.List;

import javax.validation.Valid;

import com.tm.querybuilder.enums.LogicalCondition;

public class ConditionGroupPOJO {

    @Valid
	private List<ConditionPOJO> conditionList;
	private LogicalCondition logicalCondition;

	public LogicalCondition getLogicalCondition() {
		return logicalCondition;
	}

	public void setLogicalCondition(LogicalCondition logicalCondition) {
		this.logicalCondition = logicalCondition;
	}

	public List<ConditionPOJO> getConditionList() {
		return conditionList;
	}

	public void setConditionList(List<ConditionPOJO> whereConditon) {
		this.conditionList = whereConditon;
	}

}
