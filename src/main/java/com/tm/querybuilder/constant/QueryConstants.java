package com.tm.querybuilder.constant;

public class QueryConstants {

	private QueryConstants() {
	}

	public static final String IS_SCHEMA_EXIST = "SELECT count(table_schema) FROM information_schema.tables WHERE table_schema = :schemaName ";

	public static final String GET_DATATYPE = "SELECT concat(table_name,'.',column_name) as table_column,data_type FROM information_schema.columns WHERE table_schema = :schemaName and table_name in (:tableName) and concat(table_name,'.',column_name) in (:columns)";

	public static final String IS_VALID_TABLE = "SELECT count(*) FROM information_schema.tables WHERE table_name IN (:tableName) AND table_schema = :schemaName";
	public static final String IS_VALID_COLUMN = "SELECT count(*) FROM information_schema.columns WHERE table_schema = :schemaName AND table_name IN (:tableName) AND concat(table_name,'.',column_name) IN (:columns)";

	public static final String FETCH_COLUMN_DETAILS = "SELECT column_name, data_type,table_name FROM information_schema.columns WHERE table_schema = :schemaName AND table_name IN (:tableName)";

	public static final String SCHEMA_DETAIL = "SELECT column_details.data_type,column_details.column_name,column_details.table_name,column_details.column_key as columnKey,key_column.REFERENCED_TABLE_NAME as referenceTable,key_column.REFERENCED_COLUMN_NAME as referenceColumn FROM information_schema.columns AS column_details LEFT JOIN information_schema.key_column_usage AS key_column ON column_details.table_schema = key_column.table_schema AND column_details.table_name = key_column.table_name AND column_details.column_name = key_column.column_name WHERE column_details.table_schema = :schemaName";
	public static final String FETCH_TABLE_DETAILS = "SELECT concat(column_details.table_name,'.',key_column.REFERENCED_COLUMN_NAME) as referenceColumn,column_details.column_name,column_details.table_name,column_details.column_key as columnKey,REFERENCED_TABLE_NAME as referenceTable FROM information_schema.columns AS column_details LEFT JOIN information_schema.key_column_usage AS key_column ON column_details.table_schema = key_column.table_schema AND column_details.table_name = key_column.table_name AND column_details.column_name = key_column.column_name WHERE column_details.table_schema = :schemaName and column_details.table_name IN (:tableName)";

	public static final String FROM = " FROM ";
	public static final String SELECT = "SELECT ";
	public static final String WHERE = " WHERE ";
	public static final String LIMIT = " LIMIT ";
	public static final String OFFSET = " OFFSET ";
	public static final String ORDERBY = " ORDER BY ";
	public static final String GROUPBY = " GROUP BY ";
	public static final String HAVING = " HAVING ";
	public static final String SELECT_COUNT = "SELECT count(*) as count";
	public static final String JDBC = "jdbc";
	public static final String CROSSJOIN="CROSS JOIN";

}
