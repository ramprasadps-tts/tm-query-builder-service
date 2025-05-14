package com.tm.querybuilder.dto;

import com.tm.querybuilder.enums.KeyColumn;

public class FetchTableDetailsDTO {

	private String columnName;
	private String tableName;
	private String referenceTable;
	private String referenceColumn;

	private KeyColumn columnKey;

	public String getColumnName() {
		return columnName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getReferenceTable() {
		return referenceTable;
	}

	public String getReferenceColumn() {
		return referenceColumn;
	}

	public KeyColumn getColumnKey() {
		return columnKey;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setReferenceTable(String referenceTable) {
		this.referenceTable = referenceTable;
	}

	public void setReferenceColumn(String referenceColumn) {
		this.referenceColumn = referenceColumn;
	}

	public void setColumnKey(KeyColumn columnKey) {
		this.columnKey = columnKey;
	}

}
