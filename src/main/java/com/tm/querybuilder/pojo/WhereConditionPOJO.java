package com.tm.querybuilder.pojo;

import java.util.List;

import javax.validation.Valid;

public class WhereConditionPOJO {

	@Valid	
	private List<ConditionGroupPOJO> conditionData;

	public List<ConditionGroupPOJO> getConditionData() {
		return conditionData;
	}

	public void setConditionData(List<ConditionGroupPOJO> conditionData) {
		this.conditionData = conditionData;
	}

}
