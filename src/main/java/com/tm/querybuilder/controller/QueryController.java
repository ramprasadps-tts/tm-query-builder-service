package com.tm.querybuilder.controller;

import java.util.HashMap;
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
@RequestMapping(value = "/query")
public class QueryController {

	private final QueryBuilderService queryBuilderService;

	public QueryController(QueryBuilderService queryBuilderService) {
		this.queryBuilderService = queryBuilderService;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryController.class);

	@PostMapping("/fetchQuery")
	public QueryBuilderResponsePOJO fetchQuery(@Valid @RequestBody QueryBuilderRequestPOJO queryBuilderRequestPojo) {
		LOGGER.info("fetch Query Api");
		QueryBuilderResponsePOJO queryBuilderResponsePojo = new QueryBuilderResponsePOJO();
		FilterDataPOJO filterData = queryBuilderRequestPojo.getRequestData();
		String connectionId = queryBuilderRequestPojo.getConnectionId();

		try {
			if (!isConnectionValid(connectionId)) {
				queryBuilderResponsePojo.response(MessageConstants.NOT_VALID_CONNECTION, false);
				return queryBuilderResponsePojo;
			}

			if (!isSchemaValid(connectionId)) {
				queryBuilderResponsePojo.response(MessageConstants.NOT_VALID_SCHEMA, false);
				return queryBuilderResponsePojo;
			}

			if (!isTableAndColumnValid(filterData, connectionId)) {
				queryBuilderResponsePojo.response(MessageConstants.NOT_VALID_TABLECOLUMN, false);
				return queryBuilderResponsePojo;
			}

			if (isJoinInvalid(filterData, connectionId)) {
				queryBuilderResponsePojo.response("Both the join Columns are primary key", null, false);
				return queryBuilderResponsePojo;
			}

			LOGGER.info(MessageConstants.VALID_TABLECOLUMN);
			Map<String, String> responseMap = new HashMap<>();
			responseMap.put("query", queryBuilderService.fetchQuery(connectionId, filterData).get("selectQuery"));
			queryBuilderResponsePojo.response("Data for the Request", responseMap, true);

		} catch (Exception exception) {
			LOGGER.error(exception.getMessage());
			queryBuilderResponsePojo.errorResponse(MessageConstants.BAD_REQUEST);
		}

		return queryBuilderResponsePojo;
	}

	private boolean isConnectionValid(String connectionId) {
		boolean valid = Boolean.TRUE.equals(queryBuilderService.isValidConnection(connectionId));
		if (!valid) {
			LOGGER.error(MessageConstants.NOT_VALID_CONNECTION);
		}
		return valid;
	}

	private boolean isSchemaValid(String connectionId) {
		boolean valid = Boolean.TRUE.equals(queryBuilderService.isSchemaExist(connectionId));
		if (!valid) {
			LOGGER.error(MessageConstants.NOT_VALID_SCHEMA);
		}
		return valid;
	}

	private boolean isTableAndColumnValid(FilterDataPOJO filterData, String connectionId) {
		boolean valid = Boolean.TRUE.equals(
				queryBuilderService.isValidTable(filterData.getTableName(), filterData.getJoinData(), connectionId)
						&& queryBuilderService.isValidColumns(filterData, connectionId));
		if (!valid) {
			LOGGER.error(MessageConstants.NOT_VALID_TABLECOLUMN);
		}
		return valid;
	}

	private boolean isJoinInvalid(FilterDataPOJO filterData, String connectionId) {
		if (EmptyNotNull.isValidInput(filterData.getJoinData())
				&& Boolean.FALSE.equals(filterData.getJoinData().getIsPrimaryKey())) {
			boolean isValidCondition = queryBuilderService.joinConditionValidator(filterData, connectionId);
			return Boolean.FALSE.equals(isValidCondition);
		}
		return false;
	}
}
