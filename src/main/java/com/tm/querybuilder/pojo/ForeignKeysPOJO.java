package com.tm.querybuilder.pojo;

public class ForeignKeysPOJO {

	private String referenceTable;
	private String referencecolumn;
	private String columnName;
	
	public String getReferenceTable() {
		return referenceTable;
	}
	public String getReferencecolumn() {
		return referencecolumn;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setReferenceTable(String referenceTable) {
		this.referenceTable = referenceTable;
	}
	public void setReferencecolumn(String referencecolumn) {
		this.referencecolumn = referencecolumn;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	
	
	
}
