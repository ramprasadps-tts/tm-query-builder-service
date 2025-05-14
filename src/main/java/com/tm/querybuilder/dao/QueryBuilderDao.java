package com.tm.querybuilder.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.springframework.stereotype.Service;

import com.tm.querybuilder.dto.ColumnDatatypeDTO;
import com.tm.querybuilder.dto.ColumnDetailsDTO;
import com.tm.querybuilder.dto.CountRowDTO;
import com.tm.querybuilder.dto.FetchTableDetailsDTO;
import com.tm.querybuilder.pojo.DatabaseConnectionDTO;
import com.tm.querybuilder.pojo.request.DbConnectionRequestPOJO;

@Service
public interface QueryBuilderDao {

	/**
	 * This Api for dynamic join query for multiple tables
	 * 
	 * @param queryString
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> fetchResultData(String queryString, DatabaseConnectionDTO databaseConnectionDTO);

	/**
	 * this method is to check the schema exist in the db.
	 * 
	 * @param schemaString
	 * @return boolean
	 */
	public Boolean isSchemaExist(String schemaString, DatabaseConnectionDTO databaseConnectionDTO);

	/**
	 * In this method it validate the table in the schema
	 * 
	 * @param schemaString
	 * @param tableString
	 * @return
	 */
	public Boolean isValidTable(String schemaString, Set<String> tableList,
			DatabaseConnectionDTO databaseConnectionDTO);

	/**
	 * In this method validate the column by using schema and table name using
	 * column list
	 * 
	 * @param columnsList
	 * @param tableString
	 * @param schemaString
	 * @return
	 */
	public Boolean isValidColumns(Set<String> columnsList, Set<String> tableList, String schemaString,
			DatabaseConnectionDTO databaseConnectionDTO);

	/**
	 * get the data type of the column in where clause
	 * 
	 * @param schemaString
	 * @param tableName
	 * @param columnList
	 * @return
	 */
	public List<ColumnDatatypeDTO> getDataType(String schemaString, Set<String> tableList, Set<String> columnList,
			DatabaseConnectionDTO databaseConnectionDTO);

	/**
	 * This method will return the column And TableName of the database
	 * 
	 * @param schemaString
	 * @param tableString
	 * @return
	 */
	List<ColumnDetailsDTO> fetchColumnDetails(String schemaString, DatabaseConnectionDTO databaseConnectionDTO);

	/**
	 * Fetch the table details
	 * 
	 * @param tableList
	 * @param schemaString
	 * @return
	 */
	public List<FetchTableDetailsDTO> fetchTableDetails(Set<String> tableList, String schemaString,
			DatabaseConnectionDTO databaseConnectionDTO);

	public List<CountRowDTO> countQuery(String countQueryString, DatabaseConnectionDTO databaseConnectionDTO);

	public String getDatabaseConnection(DbConnectionRequestPOJO dbConnectionRequestPojo);

	DatabaseConnectionDTO fetchdatabaseConnection(String connectionId);

	public Connection getlocalConnection(DbConnectionRequestPOJO dbConnectionRequestPojo);

}
