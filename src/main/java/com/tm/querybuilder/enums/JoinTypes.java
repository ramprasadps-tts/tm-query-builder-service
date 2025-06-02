package com.tm.querybuilder.enums;

public enum JoinTypes {

	INNERJOIN("INNER JOIN"), LEFTJOIN("LEFT JOIN"), RIGHTJOIN("RIGHT JOIN"), FULLJOIN("FULL JOIN"),
	CROSSJOIN("CROSS JOIN");

	private final String operator;

	JoinTypes(String operator) {
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}

}
