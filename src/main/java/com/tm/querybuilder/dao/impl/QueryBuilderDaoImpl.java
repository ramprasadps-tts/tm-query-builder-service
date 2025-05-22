package com.tm.querybuilder.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.tm.querybuilder.connection.DynamicDataSource;
import com.tm.querybuilder.constant.MessageConstants;
import com.tm.querybuilder.constant.QueryConstants;
import com.tm.querybuilder.dao.QueryBuilderDao;
import com.tm.querybuilder.dto.ColumnDatatypeDTO;
import com.tm.querybuilder.dto.ColumnDetailsDTO;
import com.tm.querybuilder.dto.CountRowDTO;
import com.tm.querybuilder.dto.FetchTableDetailsDTO;
import com.tm.querybuilder.pojo.DatabaseConnectionDTO;
import com.tm.querybuilder.pojo.request.DbConnectionRequestPOJO;

/**
 * @author TTS-467-balavignesh
 *
 */
@Service
public class QueryBuilderDaoImpl implements QueryBuilderDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryBuilderDaoImpl.class);

	@Value("${spring.datasource.url}")
	private String jdbcUrl;

	@Value("${spring.datasource.username}")
	private String user;

	@Value("${spring.datasource.password}")
	private String password;

	/**
	 * This method will check the schema name and table exist in dao.
	 * 
	 * @param schemaString
	 * @return
	 */
	@Override
	public Boolean isSchemaExist(String schemaString, DatabaseConnectionDTO databaseConnectionDTO) {
	    LOGGER.info("Is Schema Exist Dao layer.");
	    boolean isSchemaExist = false;
	    try {
	        MapSqlParameterSource paramsObj = new MapSqlParameterSource();
	        NamedParameterJdbcTemplate namedParameterJdbcTemplate = getConnection(databaseConnectionDTO);
	        LOGGER.info("Checking schema '{}' in DB with driver '{}'", schemaString, databaseConnectionDTO.getConnectionDriver());
	        String existsSqlString;
	        String p=MessageConstants.POSTGRES;
	        if (p.equalsIgnoreCase(databaseConnectionDTO.getConnectionDriver())) {
	            existsSqlString = QueryConstants.IS_SCHEMA_EXIST_POSTGRES;
	        } else {
	            existsSqlString = QueryConstants.IS_SCHEMA_EXIST;
	        }

	        paramsObj.addValue(MessageConstants.SCHEMA_NAME, schemaString);
	        Boolean result = namedParameterJdbcTemplate.queryForObject(
	        	    existsSqlString, paramsObj, Boolean.class);
	        	LOGGER.info("Schema exists result from DB: {}", result);
	        	isSchemaExist = Boolean.TRUE.equals(result);
	    } catch (DataAccessException exception) {
	        LOGGER.error("An error occurred while checking if the schema exists", exception);
	        throw new DataAccessResourceFailureException("An error occurred while checking if the schema exists", exception);
	    }
	    LOGGER.debug("Schema Exist dao method data: {}", isSchemaExist);
	    return isSchemaExist;
	}


	/**
	 * In this method it validate the table in the schema
	 * 
	 * @param schemaString
	 * @param tableList
	 * @return
	 */
	@Override
	public Boolean isValidTable(String schemaString, Set<String> tableList,
			DatabaseConnectionDTO databaseConnectionDTO) {
		LOGGER.info("isValid Table Dao");
		boolean isValidTable = false;
		try {
			MapSqlParameterSource parametersObj = new MapSqlParameterSource();
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = getConnection(databaseConnectionDTO);
			String queryString = QueryConstants.IS_VALID_TABLE;
			parametersObj.addValue(MessageConstants.TABLE_NAME, tableList);
			parametersObj.addValue(MessageConstants.SCHEMA_NAME, schemaString);
			Integer countInt = namedParameterJdbcTemplate.queryForObject(queryString, parametersObj, Integer.class);
			isValidTable = countInt != null && countInt == tableList.size();
		} catch (DataAccessException exception) {
			LOGGER.error("An error occurred while checking if the isValid Table", exception);
			throw new DataAccessResourceFailureException("An error occurred while checking if the isValid Table");
		}
		LOGGER.debug("is valid table dao:{}", isValidTable);
		return isValidTable;
	}

	/**
	 * In this method validate the column by using schema and table name using
	 * column list
	 * 
	 * @param columnsList
	 * @param tableList
	 * @param schemaString
	 * @return
	 */
	@Override
	public Boolean isValidColumns(Set<String> columnsList, Set<String> tableList, String schemaString,
			DatabaseConnectionDTO databaseConnectionDTO) {
		LOGGER.info("Is Valid Columns Dao");
		boolean isValidColumn = false;
		try {
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = getConnection(databaseConnectionDTO);
			MapSqlParameterSource parametersObj = new MapSqlParameterSource();
			String queryString = QueryConstants.IS_VALID_COLUMN;
			parametersObj.addValue("columns", columnsList);
			parametersObj.addValue(MessageConstants.TABLE_NAME, tableList);
			parametersObj.addValue(MessageConstants.SCHEMA_NAME, schemaString);
			Integer countInt = namedParameterJdbcTemplate.queryForObject(queryString, parametersObj, Integer.class);
			// If the count not equal to null and colunt the column size and if the both
			// condition is okay the returns
			isValidColumn = countInt != null && countInt == columnsList.size();
		} catch (DataAccessException exception) {
			LOGGER.error("An error occurred while checking if the isValid TableDetailPOJO");
			throw new DataAccessResourceFailureException(
					"An error occurred while checking if the isValid TableDetailPOJO", exception);
		}
		LOGGER.debug("is Valid column dao :{}", isValidColumn);
		return isValidColumn;
	}

	/**
	 * This method will return the column And TableName of the database
	 * 
	 * @param schemaString
	 * @return
	 */
	@Override
	public List<ColumnDetailsDTO> fetchColumnDetails(String schemaString, DatabaseConnectionDTO databaseConnectionDTO) {
		LOGGER.info("fetch column details dao");
		List<ColumnDetailsDTO> columnList;
		try {
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = getConnection(databaseConnectionDTO);
			// Query to get column names and data types for each table
			String sqlString;
			if ("postgresql".equalsIgnoreCase(databaseConnectionDTO.getConnectionDriver())) {
				sqlString = QueryConstants.SCHEMA_DETAILS_POSTGRES;
			} else {
				sqlString = QueryConstants.SCHEMA_DETAIL;
			}
			MapSqlParameterSource paramsObj = new MapSqlParameterSource();
			paramsObj.addValue(MessageConstants.SCHEMA_NAME, schemaString);
			columnList = namedParameterJdbcTemplate.query(sqlString, paramsObj,
					new BeanPropertyRowMapper<>(ColumnDetailsDTO.class));
		} catch (DataAccessException exception) {
			LOGGER.error("An error occurred while fetch TableDetailPOJO Details");
			throw new DataAccessResourceFailureException("An error occurred while fetch TableDetailPOJO Details",
					exception);
		}
		LOGGER.debug("TableDetailPOJO and datatype dao:{}", columnList);
		return columnList;

	}

	/**
	 * This method will get the query in parameter and execute
	 * 
	 * @param queryString
	 * @return
	 */
	@Override
	public List<Map<String, Object>> fetchResultData(String queryString, DatabaseConnectionDTO databaseConnectionDTO) {
		LOGGER.info("fetch Result Data Dao");
		List<Map<String, Object>> responseList = new ArrayList<>();
		try {
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = getConnection(databaseConnectionDTO);
			MapSqlParameterSource params = new MapSqlParameterSource();
			responseList = namedParameterJdbcTemplate.queryForList(queryString, params);
		} catch (DataAccessException exception) {
			LOGGER.error("An error occurred while fetch Result Data dao.");
			throw new DataAccessResourceFailureException("An error occurred while fetch Result Data.", exception);
		}
		LOGGER.debug("fetch Result Data dao:{}", responseList);
		return responseList;
	}

	@Override
	public List<CountRowDTO> countQuery(String countQueryString, DatabaseConnectionDTO databaseConnectionDTO) {
		List<CountRowDTO> count;
		try {
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = getConnection(databaseConnectionDTO);
			SqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			count = namedParameterJdbcTemplate.query(countQueryString, sqlParameterSource,
					BeanPropertyRowMapper.newInstance(CountRowDTO.class));
		} catch (Exception exception) {
			LOGGER.error("An error occurred while fetch count query dao");
			throw new DataAccessResourceFailureException("An error occurred while fetch count query dao.", exception);
		}
		return count;
	}

	/**
	 * Get the data type of the column in where clause
	 * 
	 * @param schemaString
	 * @param tableList
	 * @param columnList
	 * @return
	 */
	@Override
	public List<ColumnDatatypeDTO> getDataType(String schemaString, Set<String> tableList, Set<String> columnList,
			DatabaseConnectionDTO databaseConnectionDTO) {
		LOGGER.info("get Datatype dao");
		List<ColumnDatatypeDTO> columnDetailsList;
		try {
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = getConnection(databaseConnectionDTO);
			MapSqlParameterSource paramsObj = new MapSqlParameterSource();
			String sqlString = QueryConstants.GET_DATATYPE;
			paramsObj.addValue(MessageConstants.SCHEMA_NAME, schemaString);
			paramsObj.addValue(MessageConstants.TABLE_NAME, tableList);
			paramsObj.addValue("columns", columnList);
			columnDetailsList = namedParameterJdbcTemplate.query(sqlString, paramsObj,
					new BeanPropertyRowMapper<>(ColumnDatatypeDTO.class));
		} catch (DataAccessException exception) {
			LOGGER.error("An error occurred while getting the Datatype");
			throw new DataAccessResourceFailureException("An error occurred while getting the Datatype", exception);
		}
		LOGGER.debug("get DataType dao: {}", columnDetailsList);
		return columnDetailsList;
	}

	@Override
	public List<FetchTableDetailsDTO> fetchTableDetails(Set<String> tableList, String schemaString,
			DatabaseConnectionDTO databaseConnectionDTO) {
		List<FetchTableDetailsDTO> fetchTableDetails;
		try {
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = getConnection(databaseConnectionDTO);
			MapSqlParameterSource paramsObj = new MapSqlParameterSource();
			String sqlString = QueryConstants.FETCH_TABLE_DETAILS;
			paramsObj.addValue(MessageConstants.SCHEMA_NAME, schemaString);
			paramsObj.addValue(MessageConstants.TABLE_NAME, tableList);
			fetchTableDetails = namedParameterJdbcTemplate.query(sqlString, paramsObj,
					new BeanPropertyRowMapper<>(FetchTableDetailsDTO.class));
		} catch (Exception exception) {
			LOGGER.error("An error occurred while fetch Table Details");
			throw new DataAccessResourceFailureException("An error occurred while fetch Table Details", exception);
		}
		LOGGER.debug("get DataType dao: {}", fetchTableDetails);
		return fetchTableDetails;
	}
	private String buildJdbcUrl(String driver, String host, int port, String dbName) {
	    if ("mysql".equalsIgnoreCase(driver)) {
	        return "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	    } else if ("postgres".equalsIgnoreCase(driver) || "postgresql".equalsIgnoreCase(driver)) {
	        return "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
	    } else {
	        throw new IllegalArgumentException("Unsupported driver: " + driver);
	    }
	}


	/**
	 * This method is used to register the database details in query builder db and
	 * return the connectionId (UUID)
	 */
	@Override
	public String getDatabaseConnection(DbConnectionRequestPOJO dbConnectionRequestPojo) {
		String response = null;
		try {
			//CONNECTION TO THE DATABASE
			String jdbcUrl1 = buildJdbcUrl(
				    dbConnectionRequestPojo.getConnectionDriver(),
				    dbConnectionRequestPojo.getConnectionHost(),
				    dbConnectionRequestPojo.getConnectionPort(),
				    dbConnectionRequestPojo.getDatabaseName()
				);
				DataSource dynamicDataSource= new DynamicDataSource(
				    jdbcUrl1,
				    dbConnectionRequestPojo.getDatabaseUser(),
				    dbConnectionRequestPojo.getDatabasePassword()
				);
				//CONNECTON TO LOCAL MYSQL DATABASE
				DataSource dynamicDataSourceSql=new DynamicDataSource(jdbcUrl,user,password);

			NamedParameterJdbcTemplate parameterJdbcTemplate = new NamedParameterJdbcTemplate(dynamicDataSourceSql);
			String sql = "INSERT INTO db_connection(connection_id,connection_host,connection_db,connection_port,connection_driver,connection_user,connection_password,connection_schemaname) VALUES(:id,:host,:database,:port,:driver,:user,:password,:schemaname)";
			UUID uuid = UUID.randomUUID();
			MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", uuid.toString())
					.addValue("host", dbConnectionRequestPojo.getConnectionHost())
					.addValue("database", dbConnectionRequestPojo.getDatabaseName())
					.addValue("port", dbConnectionRequestPojo.getConnectionPort())
					.addValue("driver", dbConnectionRequestPojo.getConnectionDriver())
					.addValue("user", dbConnectionRequestPojo.getDatabaseUser())
					.addValue("password", dbConnectionRequestPojo.getDatabasePassword())
			        .addValue("schemaname",dbConnectionRequestPojo.getSchemaName());
			int valid = parameterJdbcTemplate.update(sql, params);
			if (valid > 0) {
				response = uuid.toString();
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while get Database connection in dao layer");
			throw new DataAccessResourceFailureException(
					"An error occurred while getting database connection in dao layer", exception);
		}
		LOGGER.debug("get connection dao: {},", response);
		return response;
	}

	/**
	 * This method is used to fetch the database connection details using
	 * connectionid(UUID)
	 */
	@Override
	public DatabaseConnectionDTO fetchdatabaseConnection(String connectionId) {
		DatabaseConnectionDTO databaseConnectionDTO = null;
		try {
			DataSource dynamicDataSource = new DynamicDataSource(jdbcUrl, user, password);
			NamedParameterJdbcTemplate parameterJdbcTemplate = new NamedParameterJdbcTemplate(dynamicDataSource);
			String sql = "select * from db_connection where connection_id=:connectionId";
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			mapSqlParameterSource.addValue("connectionId", connectionId);
			databaseConnectionDTO = parameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource,
					BeanPropertyRowMapper.newInstance(DatabaseConnectionDTO.class));
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error("Fetch database connection is not valid");
			throw new EmptyResultDataAccessException("fetch database connection is not valid", 0);
		} catch (Exception exception) {
			LOGGER.error("An error occurred while fetch Database connection in dao layer");
			throw new DataAccessResourceFailureException(
					"An error occurred while fetch database connection in dao layer", exception);
		}
		return databaseConnectionDTO;
	}

	/**
	 * This method is used to get the connection source using from
	 * databaseconnectiondto
	 * 
	 * @param databaseConnectionDTO
	 * @return
	 */
	private NamedParameterJdbcTemplate getConnection(DatabaseConnectionDTO databaseConnectionDTO) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate;
		try {
			String jdbcUrls = "jdbc:" + databaseConnectionDTO.getConnectionDriver() + "://"
					+ databaseConnectionDTO.getConnectionHost() + ":" + databaseConnectionDTO.getConnectionPort() + "/"
					+ databaseConnectionDTO.getConnectionDb();
			DataSource dynamicDataSource = new DynamicDataSource(jdbcUrls, databaseConnectionDTO.getConnectionUser(),
					databaseConnectionDTO.getConnectionPassword());
			namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dynamicDataSource);
			
		} catch (Exception exception) {
			LOGGER.error("An error occurred while getting connection in dao");
			throw new DataAccessResourceFailureException("An error occurred while getting connection in dao layer",
					exception);
		}
		return namedParameterJdbcTemplate;

	}

	/**
	 * This method is used to get thet database connection for local connection
	 * maintains query builder database
	 */
	@Override
	public Connection getlocalConnection(DbConnectionRequestPOJO dbConnectionRequestPojo) {
		Connection connection = null;
		try {
			String urlJdbc = QueryConstants.JDBC + ":" + dbConnectionRequestPojo.getConnectionDriver() + "://"
					+ dbConnectionRequestPojo.getConnectionHost() + ":" + dbConnectionRequestPojo.getConnectionPort()
					+ "/" + dbConnectionRequestPojo.getDatabaseName();
			DataSource dynamicDataSource = new DynamicDataSource(urlJdbc, dbConnectionRequestPojo.getDatabaseUser(),
					dbConnectionRequestPojo.getDatabasePassword());
			connection = dynamicDataSource.getConnection();
		} catch (SQLException exception) {
			LOGGER.error("An error occurred while get local database connection in dao layer");
			throw new DataAccessResourceFailureException(
					"An error occurred while get local database connection in dao layer", exception);
		}
		return connection;
	}
}



