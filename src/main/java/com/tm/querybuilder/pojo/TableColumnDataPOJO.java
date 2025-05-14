package com.tm.querybuilder.pojo;

import java.util.Set;

public class TableColumnDataPOJO {
	private Set<String> tableList;
	private Set<Object> columnList;


	public TableColumnDataPOJO(Set<String> tableList, Set<Object> columnList) {
		this.tableList = tableList;
		this.columnList = columnList;
	}

	public Set<String> getTableList() {
		return tableList;
	}

	public void setTableList(Set<String> tableList) {
		this.tableList = tableList;
	}

	public Set<Object> getColumnList() {
		return columnList;
	}

	public void setColumnList(Set<Object> columnList) {
		this.columnList = columnList;
	}
}
