package com.tm.querybuilder.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tm.querybuilder.constant.MessageConstants;
import com.tm.querybuilder.pojo.FilterDataPOJO;
import com.tm.querybuilder.pojo.request.QueryBuilderRequestPOJO;
import com.tm.querybuilder.pojo.response.QueryBuilderResponsePOJO;
import com.tm.querybuilder.service.QueryBuilderService;
import com.tm.querybuilder.validation.EmptyNotNull;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/data")
public class DataController {

	private final QueryBuilderService queryBuilderService;

	public DataController(QueryBuilderService queryBuilderService) {
		this.queryBuilderService = queryBuilderService;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(DataController.class);

	/**
	 * Before Execution In this API it will validate the schema exist for the schema
	 * the table and its column should be match the it allow to build query by using
	 * build method and the by using this it will execute depend on the request
	 * select query with and without where clause.
	 * 
	 * @param queryBuilderRequestPojo
	 * @return QueryBuilderResponsePOJO
	 */
	@PostMapping("/fetchResultData")
	public QueryBuilderResponsePOJO fetchResultData(
			@Valid @RequestBody QueryBuilderRequestPOJO queryBuilderRequestPojo) {
		LOGGER.info("fetch Result Data Api");
		QueryBuilderResponsePOJO queryBuilderResponsePojo = new QueryBuilderResponsePOJO();
		Map<String, Object> responseMap = new HashMap<>();
		boolean isValidCondition = true;

		try {
			FilterDataPOJO filterData = queryBuilderRequestPojo.getRequestData();
			String connectionId = queryBuilderRequestPojo.getConnectionId();

			if (!isConnectionValid(connectionId, queryBuilderResponsePojo))
				return queryBuilderResponsePojo;
			if (!isSchemaValid(connectionId, queryBuilderResponsePojo))
				return queryBuilderResponsePojo;
			if (!isTableAndColumnsValid(filterData, connectionId, queryBuilderResponsePojo))
				return queryBuilderResponsePojo;

			if (EmptyNotNull.isValidInput(filterData.getJoinData())
					&& Boolean.FALSE.equals(filterData.getJoinData().getIsPrimaryKey())) {
				isValidCondition = queryBuilderService.joinConditionValidator(filterData, connectionId);
			}

			if (Boolean.FALSE.equals(isValidCondition)) {
				queryBuilderResponsePojo.response("Both the join Columns are primary key", null, false);
				return queryBuilderResponsePojo;
			}

			Map<String, String> queryMap = queryBuilderService.fetchQuery(connectionId, filterData);
			List<Map<String, Object>> responseList = queryBuilderService.fetchResultData(queryMap.get("selectQuery"),
					queryMap.get("countQuery"), connectionId);

			if (responseList.isEmpty()) {
				LOGGER.error("Result No data Found for the Request Data:");
				queryBuilderResponsePojo.response(MessageConstants.NO_DATA, false);
			} else {
				responseMap.put("filterResponse", responseList);
				queryBuilderResponsePojo.response("Data for the Request", responseMap, true);
			}
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage());
			queryBuilderResponsePojo.errorResponse(MessageConstants.BAD_REQUEST);
		}
		return queryBuilderResponsePojo;
	}

	private boolean isConnectionValid(String connectionId, QueryBuilderResponsePOJO queryBuilderResponsePojo) {
		if (!Boolean.TRUE.equals(queryBuilderService.isValidConnection(connectionId))) {
			LOGGER.error(MessageConstants.NOT_VALID_CONNECTION);
			queryBuilderResponsePojo.response(MessageConstants.NOT_VALID_CONNECTION, false);
			return false;
		}
		return true;
	}

	private boolean isSchemaValid(String connectionId, QueryBuilderResponsePOJO queryBuilderResponsePojo) {
		if (!Boolean.TRUE.equals(queryBuilderService.isSchemaExist(connectionId))) {
			LOGGER.error(MessageConstants.NOT_VALID_SCHEMA);
			queryBuilderResponsePojo.response(MessageConstants.NOT_VALID_SCHEMA, false);
			return false;
		}
		return true;
	}

	private boolean isTableAndColumnsValid(FilterDataPOJO filterData, String connectionId,
			QueryBuilderResponsePOJO queryBuilderResponsePojo) {
		boolean validColumns = queryBuilderService.isValidColumns(filterData, connectionId);
		boolean validTable = queryBuilderService.isValidTable(filterData.getTableName(), filterData.getJoinData(),
				connectionId);
		if (!Boolean.TRUE.equals(validColumns && validTable)) {
			LOGGER.error(MessageConstants.NOT_VALID_TABLECOLUMN);
			queryBuilderResponsePojo.response(MessageConstants.NOT_VALID_TABLECOLUMN, false);
			return false;
		}
		return true;
	}

}
