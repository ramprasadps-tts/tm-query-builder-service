package com.tm.querybuilder.pojo;

import java.util.List;
import java.util.Map;

public class TableDetailPOJO {

	Map<String, String> column;

	private String primarykey;
	
	List<ForeignKeysPOJO> foreignKeys;

	public Map<String, String> getColumn() {
		return column;
	}

	public String getPrimarykey() {
		return primarykey;
	}

	public List<ForeignKeysPOJO> getForeignKeys() {
		return foreignKeys;
	}

	public void setColumn(Map<String, String> column) {
		this.column = column;
	}

	public void setPrimarykey(String primarykey) {
		this.primarykey = primarykey;
	}

	public void setForeignKeys(List<ForeignKeysPOJO> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}
	
	
	
	
	
}
