package com.tm.querybuilder.pojo;

import java.util.List;

public class FetchTableDetailsPOJO {

	private String primarykey;

	List<ForeignKeysPOJO> foreignKeys;

	
	public String getPrimarykey() {
		return primarykey;
	}

	public List<ForeignKeysPOJO> getForeignKeys() {
		return foreignKeys;
	}

	public void setPrimarykey(String primarykey) {
		this.primarykey = primarykey;
	}

	public void setForeignKeys(List<ForeignKeysPOJO> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}
	
	

}
