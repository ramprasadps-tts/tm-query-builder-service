package com.tm.querybuilder.enums;

public enum DataType {
	VARCHAR("varchar"), CHAR("char"), ENUM("enum"), TEXT("text"), DATE("date"), TIMESTAMP("timestamp"), TIME("time"),
	YEAR("year"), INT("int"), FLOAT("float"), DOUBLE("double"), LONG("long"), BIGINT("bigint"), TINYINT("tinyint"),
	SMALLINT("smallint"), DECIMAL("decimal"), MEDIUMINT("mediumint");

	private final String datatypes;

	DataType(String datatypes) {
		this.datatypes = datatypes;
	}

	public String getOperator() {
		return datatypes;
	}

}