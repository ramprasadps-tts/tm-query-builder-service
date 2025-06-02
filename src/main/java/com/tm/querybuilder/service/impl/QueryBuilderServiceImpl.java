package com.tm.querybuilder.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tm.querybuilder.clauses.Clauses;
import com.tm.querybuilder.constant.MessageConstants;
import com.tm.querybuilder.constant.QueryConstants;
import com.tm.querybuilder.dao.QueryBuilderDao;
import com.tm.querybuilder.dto.ColumnDatatypeDTO;
import com.tm.querybuilder.dto.ColumnDetailsDTO;
import com.tm.querybuilder.dto.CountRowDTO;
import com.tm.querybuilder.dto.FetchTableDetailsDTO;
import com.tm.querybuilder.enums.KeyColumn;
import com.tm.querybuilder.keyword.KeyTypes;
import com.tm.querybuilder.pojo.AggregateFunctionPOJO;
import com.tm.querybuilder.pojo.ConditionGroupPOJO;
import com.tm.querybuilder.pojo.ConditionPOJO;
import com.tm.querybuilder.pojo.DatabaseConnectionDTO;
import com.tm.querybuilder.pojo.FetchTableDetailsPOJO;
import com.tm.querybuilder.pojo.FilterDataPOJO;
import com.tm.querybuilder.pojo.ForeignKeysPOJO;
import com.tm.querybuilder.pojo.JoinConditionPOJO;
import com.tm.querybuilder.pojo.JoinDataPOJO;
import com.tm.querybuilder.pojo.JoinsPOJO;
import com.tm.querybuilder.pojo.OrderByPOJO;
import com.tm.querybuilder.pojo.request.DbConnectionRequestPOJO;
import com.tm.querybuilder.service.QueryBuilderService;
import com.tm.querybuilder.validation.EmptyNotNull;

@Service
public class QueryBuilderServiceImpl implements QueryBuilderService {

	@Value("${querybuilder.limit}")
	private int limit;

	@Value("${querybuilder.offset}")
	private int offset;

	private final QueryBuilderDao queryBuilderDao;

	public QueryBuilderServiceImpl(QueryBuilderDao queryBuilderDao) {
		this.queryBuilderDao = queryBuilderDao;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryBuilderServiceImpl.class);

	/**
	 * @param schemaString
	 * @return This method will check the schema name and table exist in dao.
	 * 
	 */
	// Check if the schema exists for the given connectionId.
	@Override
	public Boolean isSchemaExist(String connectionId) {
		LOGGER.info("Schema Exist service Method");
		boolean isSchemaExist = false;
		try {
			DatabaseConnectionDTO databaseConnectionDTO = queryBuilderDao.fetchdatabaseConnection(connectionId);
			String p = MessageConstants.POSTGRES;
			if (p.equalsIgnoreCase(databaseConnectionDTO.getConnectionDriver())) {
				isSchemaExist = queryBuilderDao.isSchemaExist(databaseConnectionDTO.getConnectionSchemaname(),
						databaseConnectionDTO);
			} else {
				isSchemaExist = queryBuilderDao.isSchemaExist(databaseConnectionDTO.getConnectionDb(),
						databaseConnectionDTO);
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while checking is Schema Exist in service.");
			throw new DataAccessResourceFailureException("An error occurred while checking is Schema Exist in service.",
					exception);
		}
		LOGGER.debug("Is Schema Exist in dao layer with boolean value {}", isSchemaExist);
		return isSchemaExist;
	}

	/**
	 * @param schemaString
	 * @param tableName
	 * @return This method will check the schema and table and column in dao.
	 */
	// Validate whether the specified table and joined tables exist.
	@Override
	public Boolean isValidTable(String tableString, JoinsPOJO joinsPOJO, String connectionId) {
		LOGGER.info("isValid table service");
		Boolean isValidTable = false;
		try {
			Set<String> tablesList = new HashSet<>();
			// Collect all joined table names
			if (EmptyNotNull.isValidInput(joinsPOJO)) {
				for (JoinDataPOJO joinTable : joinsPOJO.getJoin()) {
					tablesList.add(joinTable.getJoinTableName());
				}
			}
			tablesList.add(tableString);
			DatabaseConnectionDTO databaseConnectionDTO = queryBuilderDao.fetchdatabaseConnection(connectionId);
			String p = MessageConstants.POSTGRES;
			// Validate table existence depending on DB driver
			if (p.equalsIgnoreCase(databaseConnectionDTO.getConnectionDriver())) {
				isValidTable = queryBuilderDao.isValidTable(databaseConnectionDTO.getConnectionSchemaname(), tablesList,
						databaseConnectionDTO);
			} else {
				isValidTable = queryBuilderDao.isValidTable(databaseConnectionDTO.getConnectionDb(), tablesList,
						databaseConnectionDTO);
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while checking is valid Table in service.");
			throw new DataAccessResourceFailureException("An error occurred while checking is valid Table in service.",
					exception);
		}
		LOGGER.debug("Is valid table dao with boolean value {}", isValidTable);
		return isValidTable;
	}

	/**
	 * @param schemaName
	 * @return get table and columm by using schema name get the details with dao
	 *         layer.
	 */
	@Override
	public List<ColumnDetailsDTO> fetchColumnDetails(String connectionId) {
		LOGGER.info("fetch table, column, and its datatype service method");
		List<ColumnDetailsDTO> columnDetailsList;
		try {
			DatabaseConnectionDTO databaseConnectionDTO = queryBuilderDao.fetchdatabaseConnection(connectionId);
			String p = MessageConstants.POSTGRES;
			if (p.equalsIgnoreCase(databaseConnectionDTO.getConnectionDriver())) {
				columnDetailsList = queryBuilderDao.fetchColumnDetails(databaseConnectionDTO.getConnectionSchemaname(),
						databaseConnectionDTO);
			} else {
				columnDetailsList = queryBuilderDao.fetchColumnDetails(databaseConnectionDTO.getConnectionDb(),
						databaseConnectionDTO);
			}

		} catch (DataAccessException exception) {
			LOGGER.error("An error occurred while fetch ColumnDetailsDTO in service layer", exception);
			throw new DataAccessResourceFailureException(
					"An error occurred while fetch ColumnDetailsDTO in service layer.");
		}
		LOGGER.debug("fetch column details in service layer return columnDetailsDTO {}", columnDetailsList);
		return columnDetailsList;
	}

	/**
	 * @param queryString
	 * @return List<Map<String, Object>> By getting string as query in parameter
	 *         based on the query in will execute.
	 */

	// Executes a count query and returns row count results.
	private List<CountRowDTO> fetchCountQuery(String connectionId, String countqueryString) {
		LOGGER.info("fetch Result Data service");
		List<CountRowDTO> countQuery;
		try {
			DatabaseConnectionDTO databaseConnectionDTO = queryBuilderDao.fetchdatabaseConnection(connectionId);
			countQuery = queryBuilderDao.countQuery(countqueryString, databaseConnectionDTO);
		} catch (Exception exception) {
			LOGGER.error("An error occurred while fetch Data in service layer.", exception);
			throw new DataAccessResourceFailureException("An error occurred while fetch Data");
		}
		LOGGER.debug("fetch count of query in service layer return in CountTowDTO list {}", countQuery);
		return countQuery;
	}

	/**
	 * @param queryString
	 * @return List<Map<String, Object>> By getting string as query in parameter
	 *         based on the query in will execute.
	 */
	@Override
	public List<Map<String, Object>> fetchResultData(String selectQueryString, String countQueryString,
			String connectionId) {
		LOGGER.info("fetch Result Data service");
		List<Map<String, Object>> responseList = new ArrayList<>();
		Map<String, Object> countMap = new HashMap<>();
		try {
			DatabaseConnectionDTO databaseConnectionDTO = queryBuilderDao.fetchdatabaseConnection(connectionId);
			responseList = queryBuilderDao.fetchResultData(selectQueryString, databaseConnectionDTO);
			if (!CollectionUtils.isEmpty(responseList)) {
				countMap.put("rowCount", fetchCountQuery(connectionId, countQueryString).get(0).getCount());
				responseList.add(countMap);
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while fetch Data in service layer.", exception);
			throw new DataAccessResourceFailureException("An error occurred while fetch Data");
		}
		LOGGER.debug("fetch Result Data in service layer return list of map {}", responseList);
		return responseList;
	}

	/**
	 * @param filterData
	 * @return String This method build the query based on the request. In this
	 *         method get request from pojo based on the request this method will
	 *         send data to dao layer. select query with and without where caluse
	 */
	@Override
	public Map<String, String> fetchQuery(String connectionId, FilterDataPOJO filterData) {
		Map<String, String> queryMap = new HashMap<>();
		KeyTypes keyTypes = new KeyTypes();
		LOGGER.info("fetch query service");

		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder selectBuilder = new StringBuilder();

		try {
			// SELECT clause
			selectBuilder.append(QueryConstants.SELECT);
			buildSelectClause(selectBuilder, filterData);

			// FROM clause
			DatabaseConnectionDTO dbConnection = queryBuilderDao.fetchdatabaseConnection(connectionId);
			String dbDriver = dbConnection.getConnectionDriver();
			String schemaOrDb = dbDriver.equalsIgnoreCase(MessageConstants.POSTGRES)
					? dbConnection.getConnectionSchemaname()
					: dbConnection.getConnectionDb();

			queryBuilder.append(QueryConstants.FROM).append(schemaOrDb).append(".").append(filterData.getTableName());

			// WHERE / JOIN / GROUP BY / HAVING
			String conditionalQuery = ifQueryBuilder(filterData, schemaOrDb, connectionId);
			if (EmptyNotNull.isValidInput(conditionalQuery)) {
				queryBuilder.append(conditionalQuery);
			}

			// ORDER BY
			if (EmptyNotNull.isValidInput(filterData.getOrderBy())) {
				queryBuilder.append(keyTypes.getColumnOrderBy(filterData.getOrderBy()));
			}

			// LIMIT and OFFSET
			queryBuilder.append(keyTypes.getLimit(filterData, limit, offset));

		} catch (Exception exception) {
			LOGGER.error("An error occurred while fetch Query.");
			throw new DataAccessResourceFailureException("An error occurred while fetch Query.", exception);
		}

		// Final query assembly
		queryMap.put("selectQuery", selectBuilder.append(queryBuilder).toString());
		queryMap.put("countQuery", buildCountQuery(selectBuilder.toString()));

		LOGGER.debug("Build Query for the request data service:{}", queryBuilder);
		return queryMap;
	}

	private void buildSelectClause(StringBuilder selectBuilder, FilterDataPOJO filterData) {
		if (EmptyNotNull.isValidInput(filterData.getGroupBy()) && !CollectionUtils.isEmpty(filterData.getColumnNames())
				&& !CollectionUtils.isEmpty(filterData.getAggregateFunction())) {

			selectBuilder.append(String.join(",", filterData.getColumnNames()));

			for (AggregateFunctionPOJO aggregate : filterData.getAggregateFunction()) {
				selectBuilder.append(", ").append(aggregate.getAggregateTypes()).append("(")
						.append(aggregate.getColumnName()).append(")");
			}

		} else if (!CollectionUtils.isEmpty(filterData.getColumnNames())) {
			selectBuilder.append(String.join(",", filterData.getColumnNames()));

		} else if (!CollectionUtils.isEmpty(filterData.getAggregateFunction())) {
			for (AggregateFunctionPOJO aggregate : filterData.getAggregateFunction()) {
				selectBuilder.append(aggregate.getAggregateTypes()).append("(").append(aggregate.getColumnName())
						.append(")");
			}

		} else {
			LOGGER.error("** Both the column list and aggregate function are empty **");
			throw new DataAccessResourceFailureException("Both the column List and aggregate function are empty");
		}
	}

	private String buildCountQuery(String selectQuery) {
		return new StringBuilder().append(QueryConstants.SELECT_COUNT).append(QueryConstants.FROM).append("( ")
				.append(selectQuery).append(") AS count").toString();
	}

	private String ifQueryBuilder(FilterDataPOJO filterData, String schemaString, String connectionId) {
		StringBuilder querBuilder = new StringBuilder();
		Clauses clauses = new Clauses();
		try {
			// JOIN
			if (EmptyNotNull.isValidInput(filterData.getJoinData())) {
				querBuilder.append(clauses.getOnCondition(filterData.getJoinData().getJoin(), schemaString));
			}
			// WHERE
			if (EmptyNotNull.isValidInput(filterData.getWhereData())) {
				querBuilder.append(
						clauses.whereCondition(filterData, getDataType(filterData, schemaString, connectionId)));
			}
			// GROUPBY
			if (EmptyNotNull.isValidInput(filterData.getGroupBy())) {
				if (!CollectionUtils.isEmpty(filterData.getGroupBy().getColumnList())) {
					querBuilder.append(clauses.groupBy(filterData.getGroupBy(), filterData.getColumnNames()));
				}
				if (!CollectionUtils.isEmpty(filterData.getGroupBy().getConditionData())) {
					querBuilder.append(clauses.having(filterData.getGroupBy().getConditionData(),
							getDataType(filterData, schemaString, connectionId), filterData.getAggregateFunction()));
				}
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while ifQuery Builder.");
			throw new DataAccessResourceFailureException("An error occurred while ifQuery Builder.", exception);
		}
		LOGGER.debug("build the query ifQueryBuilder in service layer return stringbuilder:{}", querBuilder);
		return querBuilder.toString();
	}

	/**
	 * @param filterData
	 * @return Get the datatype of column in the whereClause
	 */
	private Map<String, Object> getDataType(FilterDataPOJO filterData, String schemaString, String connectionId) {
		LOGGER.info("Get data type service");
		Map<String, Object> schemaMap = new LinkedHashMap<>();
		try {
			Set<String> tablesList = new HashSet<>();
			Set<String> columnsList = new HashSet<>();
			if (EmptyNotNull.isValidInput(filterData.getWhereData())) {
				columnsList.addAll(fetchConditionColumn(filterData.getWhereData().getConditionData()));
			}
			if (EmptyNotNull.isValidInput(filterData.getGroupBy())) {
				columnsList.addAll(filterData.getColumnNames());
				if (!CollectionUtils.isEmpty(filterData.getGroupBy().getConditionData())) {
					columnsList.addAll(fetchConditionColumn(filterData.getGroupBy().getConditionData()));
				}
			}
			if (EmptyNotNull.isValidInput(filterData.getJoinData())) {
				for (JoinDataPOJO joinTable : filterData.getJoinData().getJoin()) {
					tablesList.add(joinTable.getJoinTableName());
				}
			}
			tablesList.add(filterData.getTableName());
			DatabaseConnectionDTO databaseConnectionDTO = queryBuilderDao.fetchdatabaseConnection(connectionId);
			List<ColumnDatatypeDTO> columnDetails = queryBuilderDao.getDataType(schemaString, tablesList, columnsList,
					databaseConnectionDTO);
			for (ColumnDatatypeDTO columnDetail : columnDetails) {
				schemaMap.put(columnDetail.getTableColumn(), columnDetail.getDataType());
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while Getting Data Type.");
			throw new DataAccessResourceFailureException("An error occurred while Getting Data Type.", exception);
		}
		LOGGER.debug("get datatype in service:{}", schemaMap);
		return schemaMap;
	}

	/**
	 * get the column valid using columnList with its table and schema
	 * 
	 * @param columnList
	 * @param tableName
	 * @param schemaString
	 */
	@Override
	public Boolean isValidColumns(FilterDataPOJO filterData, String connectionId) {
		LOGGER.info("Is Valid TableDetailPOJO service method");
		boolean isValidColumn = false;

		try {
			Set<String> tablesList = extractTables(filterData);
			Set<String> columnsList = extractColumns(filterData);

			tablesList.add(filterData.getTableName());

			DatabaseConnectionDTO dbConnection = queryBuilderDao.fetchdatabaseConnection(connectionId);
			String driver = dbConnection.getConnectionDriver();
			String schemaOrDb = driver.equalsIgnoreCase(MessageConstants.POSTGRES)
					? dbConnection.getConnectionSchemaname()
					: dbConnection.getConnectionDb();

			isValidColumn = queryBuilderDao.isValidColumns(columnsList, tablesList, schemaOrDb, dbConnection);

		} catch (Exception exception) {
			LOGGER.error("An error occurred Checking is valid Column details.");
			throw new DataAccessResourceFailureException("An error occurred Checking is valid Column Details",
					exception);
		}

		LOGGER.debug("is valid column in service layer:{}", isValidColumn);
		return isValidColumn;
	}

	private Set<String> extractTables(FilterDataPOJO filterData) {
		Set<String> tables = new HashSet<>();
		if (EmptyNotNull.isValidInput(filterData.getJoinData())) {
			for (JoinDataPOJO join : filterData.getJoinData().getJoin()) {
				tables.add(join.getJoinTableName());
			}
		}
		return tables;
	}

	private Set<String> extractColumns(FilterDataPOJO filterData) {
		Set<String> columns = new HashSet<>();

		extractJoinColumns(filterData, columns);
		extractWhereColumns(filterData, columns);
		extractGroupByColumns(filterData, columns);
		extractOrderByColumns(filterData, columns);
		extractSelectColumns(filterData, columns);
		extractAggregateColumns(filterData, columns);

		return columns;
	}

	private void extractJoinColumns(FilterDataPOJO filterData, Set<String> columns) {
		if (!EmptyNotNull.isValidInput(filterData.getJoinData()))
			return;

		for (JoinDataPOJO joinTable : filterData.getJoinData().getJoin()) {
			if (CollectionUtils.isEmpty(joinTable.getJoinCondition()))
				continue;

			for (JoinConditionPOJO joinCondition : joinTable.getJoinCondition()) {
				columns.add(joinCondition.getLsColumn());
				columns.add(joinCondition.getRsColumn());
			}
		}
	}

	private void extractWhereColumns(FilterDataPOJO filterData, Set<String> columns) {
		if (EmptyNotNull.isValidInput(filterData.getWhereData())) {
			columns.addAll(fetchConditionColumn(filterData.getWhereData().getConditionData()));
		}
	}

	private void extractGroupByColumns(FilterDataPOJO filterData, Set<String> columns) {
		if (!EmptyNotNull.isValidInput(filterData.getGroupBy()))
			return;

		if (!CollectionUtils.isEmpty(filterData.getGroupBy().getColumnList())) {
			columns.addAll(filterData.getGroupBy().getColumnList());
		}

		if (!CollectionUtils.isEmpty(filterData.getGroupBy().getConditionData())) {
			columns.addAll(fetchConditionColumn(filterData.getGroupBy().getConditionData()));
		}
	}

	private void extractOrderByColumns(FilterDataPOJO filterData, Set<String> columns) {
		if (!CollectionUtils.isEmpty(filterData.getOrderBy())) {
			for (OrderByPOJO orderBy : filterData.getOrderBy()) {
				columns.add(orderBy.getOrderColumnName());
			}
		}
	}

	private void extractSelectColumns(FilterDataPOJO filterData, Set<String> columns) {
		if (!CollectionUtils.isEmpty(filterData.getColumnNames())) {
			columns.addAll(filterData.getColumnNames());
		}
	}

	private void extractAggregateColumns(FilterDataPOJO filterData, Set<String> columns) {
		if (!CollectionUtils.isEmpty(filterData.getAggregateFunction())) {
			for (AggregateFunctionPOJO aggregate : filterData.getAggregateFunction()) {
				columns.add(aggregate.getColumnName());
			}
		}
	}

	private Map<String, FetchTableDetailsPOJO> fetchTableDetails(FilterDataPOJO filterDataPOJO, String connectionId) {
		Map<String, FetchTableDetailsPOJO> tablesMap = new HashMap<>();
		try {
			Set<String> tablesList = new HashSet<>();
			if (!CollectionUtils.isEmpty(filterDataPOJO.getJoinData().getJoin())) {
				for (JoinDataPOJO joinTable : filterDataPOJO.getJoinData().getJoin()) {
					tablesList.add(joinTable.getJoinTableName());
				}
			}
			tablesList.add(filterDataPOJO.getTableName());
			DatabaseConnectionDTO databaseConnectionDTO = queryBuilderDao.fetchdatabaseConnection(connectionId);
			List<FetchTableDetailsDTO> fetchTableDetails = queryBuilderDao.fetchTableDetails(tablesList,
					databaseConnectionDTO.getConnectionDb(), databaseConnectionDTO);
			for (FetchTableDetailsDTO fetchTableDetailsDTO : fetchTableDetails) {
				FetchTableDetailsPOJO fetchTableDetailsPOJO = new FetchTableDetailsPOJO();
				if (!tablesMap.containsKey(fetchTableDetailsDTO.getTableName())) {
					tablesMap.put(fetchTableDetailsDTO.getTableName(), fetchTableDetailsPOJO);
				}
				if (KeyColumn.PRI.equals(fetchTableDetailsDTO.getColumnKey())) {
					fetchTableDetailsPOJO = tablesMap.get(fetchTableDetailsDTO.getTableName());
					fetchTableDetailsPOJO.setPrimarykey(
							fetchTableDetailsDTO.getTableName() + "." + fetchTableDetailsDTO.getColumnName());
				} else if (KeyColumn.MUL.equals(fetchTableDetailsDTO.getColumnKey())) {
					List<ForeignKeysPOJO> foreignKeyList = new ArrayList<>();
					ForeignKeysPOJO foreignKeys = new ForeignKeysPOJO();
					foreignKeys.setColumnName(fetchTableDetailsDTO.getColumnName());
					foreignKeys.setReferencecolumn(fetchTableDetailsDTO.getReferenceColumn());
					foreignKeys.setReferenceTable(fetchTableDetailsDTO.getReferenceTable());
					fetchTableDetailsPOJO = tablesMap.get(fetchTableDetailsDTO.getTableName());
					foreignKeyList.add(foreignKeys);
					fetchTableDetailsPOJO.setForeignKeys(foreignKeyList);
				}
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred Checking is valid Column details.");
			throw new DataAccessResourceFailureException("An error occurred Checking is valid Column Details",
					exception);
		}
		return tablesMap;
	}

	@Override
	public Boolean joinConditionValidator(FilterDataPOJO filterData, String connectionId) {
		boolean isValidCondition = false;
		try {
			Map<String, FetchTableDetailsPOJO> tableDetails = fetchTableDetails(filterData, connectionId);
			isValidCondition = conditionValidator(filterData, tableDetails);
		} catch (Exception exception) {
			LOGGER.error("** An error occurred while validating the join condition in service **");
			throw new DataAccessResourceFailureException("An error occurred while validating the join condition.",
					exception);
		}
		LOGGER.debug("On conditon : {}", isValidCondition);
		return isValidCondition;
	}

	private Boolean conditionValidator(FilterDataPOJO filterData, Map<String, FetchTableDetailsPOJO> tableDetails) {
		LOGGER.info("build On condition using string builder method");
		boolean isValid = true;

		try {
			for (JoinDataPOJO joinData : filterData.getJoinData().getJoin()) {
				if (CollectionUtils.isEmpty(joinData.getJoinCondition()))
					continue;

				for (JoinConditionPOJO joinConditionDto : joinData.getJoinCondition()) {
					FetchTableDetailsPOJO leftTable = tableDetails.get(filterData.getTableName());
					FetchTableDetailsPOJO rightTable = tableDetails.get(joinData.getJoinTableName());

					String leftColumn = joinConditionDto.getLsColumn();
					String rightColumn = joinConditionDto.getRsColumn();

					if (leftTable.getPrimarykey().equals(leftColumn)
							&& rightTable.getPrimarykey().equals(rightColumn)) {
						isValid = false;
					} else {
						boolean isPrimary = isPrimaryKeyMatch(leftTable, rightTable, leftColumn, rightColumn);
						boolean isForeign = isForeignKeyMatch(leftTable, rightTable, leftColumn, rightColumn,
								filterData.getTableName(), joinData.getJoinTableName());

						isValid = isPrimary && isForeign;
					}
				}
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while Getting Join condition query.");
			throw new DataAccessResourceFailureException("An error occurred while getting Join condition query.",
					exception);
		}

		LOGGER.debug("conditionValidator in service layer: {}", isValid);
		return isValid;
	}

	private boolean isPrimaryKeyMatch(FetchTableDetailsPOJO leftTable, FetchTableDetailsPOJO rightTable,
			String leftColumn, String rightColumn) {
		return leftTable.getPrimarykey().equals(leftColumn) || rightTable.getPrimarykey().equals(rightColumn);
	}

	private boolean isForeignKeyMatch(FetchTableDetailsPOJO leftTable, FetchTableDetailsPOJO rightTable,
			String leftColumn, String rightColumn, String leftTableName, String rightTableName) {

		boolean isForeign = false;

		if (!CollectionUtils.isEmpty(leftTable.getForeignKeys())) {
			isForeign = getKeyIteration(leftTable.getForeignKeys(), rightTableName, leftColumn);
		}

		if (!CollectionUtils.isEmpty(rightTable.getForeignKeys())) {
			isForeign = isForeign || getKeyIteration(rightTable.getForeignKeys(), leftTableName, rightColumn);
		}

		return isForeign;
	}

	private Set<String> fetchConditionColumn(List<ConditionGroupPOJO> conditionGroupPOJO) {
		Set<String> columnsListSet = new HashSet<>();
		try {
			for (ConditionGroupPOJO conditionGroupList : conditionGroupPOJO) {
				for (ConditionPOJO conditions : conditionGroupList.getConditionList()) {
					columnsListSet.add(conditions.getColumn());
				}
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while getting fetch condition column");
			throw new DataAccessResourceFailureException("An error occurred while getting Join condition query.",
					exception);
		}
		LOGGER.debug("fetch Condition columns service : {}", columnsListSet);
		return columnsListSet;
	}

	private Boolean getKeyIteration(List<ForeignKeysPOJO> foreignKeyPojo, String tableName, String columnLsRs) {
		boolean isForeign = false;
		try {
			for (ForeignKeysPOJO foreignKeysPOJO : foreignKeyPojo) {
				isForeign = foreignKeysPOJO.getReferenceTable().equals(tableName)
						&& foreignKeysPOJO.getReferencecolumn().equals(columnLsRs);
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while getting key Iteration in service");
			throw new DataAccessResourceFailureException("An error occurred while getting key Iteration in service.",
					exception);
		}
		LOGGER.debug("fetch get Key Iteration : {}", isForeign);
		return isForeign;
	}

	@Override
	public String getDatabaseConnection(DbConnectionRequestPOJO dbConnectionRequestPojo) {
		String response = null;
		try {
			Connection connection = queryBuilderDao.getlocalConnection(dbConnectionRequestPojo);
			// verify the connection already exist
			if (EmptyNotNull.isValidInput(connection)) {
				response = queryBuilderDao.getDatabaseConnection(dbConnectionRequestPojo);
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while get database connection in service");
			throw new DataAccessResourceFailureException(
					"An error occurred while getting database connection in service.", exception);
		}
		return response;
	}

	@Override
	public boolean isValidConnection(String connectionId) {
		boolean isValid = false;
		try {
			DatabaseConnectionDTO databaseConnectionDTO = queryBuilderDao.fetchdatabaseConnection(connectionId);
			if (EmptyNotNull.isValidInput(databaseConnectionDTO)) {
				isValid = true;
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while checking is valid connection in service");
			throw new DataAccessResourceFailureException(
					"An error occurred while checking is valid connection in service", exception);
		}
		return isValid;
	}
}
