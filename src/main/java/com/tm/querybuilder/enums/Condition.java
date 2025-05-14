package com.tm.querybuilder.enums;

public enum Condition {

	EQUAL("="), NOT_EQUAL("<>"), LT("<"), GT(">"), GTE(">="), LTE("<="),CONTAINS("LIKE"),LIKE("LIKE"),
	BETWEEN("BETWEEN"),IN("IN"),NOTIN("NOT IN"),ISNOTNULL(" IS NOT NULL"),ISNULL("IS NULL"),STARTWITH("LIKE"),ENDWITH("LIKE");
    
	private final String operator;

	Condition(String operator) {
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}

}
