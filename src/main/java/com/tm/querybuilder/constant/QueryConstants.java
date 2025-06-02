package com.tm.querybuilder.constant;

public class QueryConstants {

	private QueryConstants() {
	}

	public static final String IS_SCHEMA_EXIST = "SELECT count(table_schema) FROM information_schema.tables WHERE table_schema = :schemaName ";
	public static final String IS_SCHEMA_EXIST_POSTGRES = "SELECT EXISTS (SELECT 1 FROM information_schema.schemata  WHERE schema_name = :schemaName);";
	public static final String GET_DATATYPE = "SELECT concat(table_name,'.',column_name) as table_column,data_type FROM information_schema.columns WHERE table_schema = :schemaName and table_name in (:tableName) and concat(table_name,'.',column_name) in (:columns)";
	public static final String IS_VALID_TABLE = "SELECT count(*) FROM information_schema.tables WHERE table_name IN (:tableName) AND table_schema = :schemaName";
	public static final String IS_VALID_COLUMN = "SELECT count(*) FROM information_schema.columns WHERE table_schema = :schemaName AND table_name IN (:tableName) AND column_name IN (:columns)";
	public static final String FETCH_COLUMN_DETAILS = "SELECT column_name, data_type,table_name FROM information_schema.columns WHERE table_schema = :schemaName AND table_name IN (:tableName)";
	public static final String SCHEMA_DETAIL = "SELECT column_details.data_type,column_details.column_name,column_details.table_name,column_details.column_key as columnKey,key_column.REFERENCED_TABLE_NAME as referenceTable,key_column.REFERENCED_COLUMN_NAME as referenceColumn FROM information_schema.columns AS column_details LEFT JOIN information_schema.key_column_usage AS key_column ON column_details.table_schema = key_column.table_schema AND column_details.table_name = key_column.table_name AND column_details.column_name = key_column.column_name WHERE column_details.table_schema = :schemaName";
	public static final String FETCH_TABLE_DETAILS = "SELECT concat(column_details.table_name,'.',key_column.REFERENCED_COLUMN_NAME) as referenceColumn,column_details.column_name,column_details.table_name,column_details.column_key as columnKey,REFERENCED_TABLE_NAME as referenceTable FROM information_schema.columns AS column_details LEFT JOIN information_schema.key_column_usage AS key_column ON column_details.table_schema = key_column.table_schema AND column_details.table_name = key_column.table_name AND column_details.column_name = key_column.column_name WHERE column_details.table_schema = :schemaName and column_details.table_name IN (:tableName)";
	public static final String SCHEMA_DETAILS_POSTGRES = "SELECT cols.data_type, cols.column_name, cols.table_name,\n"
			+ "       CASE WHEN tc.constraint_type = 'PRIMARY KEY' THEN 'PRI' WHEN tc.constraint_type = 'FOREIGN KEY' THEN 'FOR' ELSE NULL END AS columnKey,\n"
			+ "       ccu.table_name AS referenceTable, ccu.column_name AS referenceColumn\n"
			+ "FROM information_schema.columns cols\n"
			+ "LEFT JOIN information_schema.key_column_usage kcu ON cols.table_schema = kcu.table_schema AND cols.table_name = kcu.table_name AND cols.column_name = kcu.column_name\n"
			+ "LEFT JOIN information_schema.table_constraints tc ON kcu.constraint_schema = tc.constraint_schema AND kcu.constraint_name = tc.constraint_name AND kcu.table_name = tc.table_name\n"
			+ "LEFT JOIN information_schema.constraint_column_usage ccu ON tc.constraint_schema = ccu.constraint_schema AND tc.constraint_name = ccu.constraint_name\n"
			+ "WHERE cols.table_schema = :schemaName;\n" + "";
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
	public static final String CROSSJOIN = "CROSS JOIN";

}
