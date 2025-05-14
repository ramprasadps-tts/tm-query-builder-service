package com.tm.querybuilder.controller;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private QueryBuilderService queryBuilderService;

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryController.class);

	/**
	 * Before Buildling query In this API it will validate the schema exist for the
	 * schema the table and its column should be match the it allow to build query
	 * depend on the request select query with and without where clause.
	 * 
	 * @param QueryBuilderRequestPOJO
	 * @return QueryBuilderResponsePOJO
	 */
	@PostMapping("/fetchQuery")
	public QueryBuilderResponsePOJO fetchQuery(@Valid @RequestBody QueryBuilderRequestPOJO queryBuilderRequestPojo) {
		LOGGER.info("fetch Query Api");
		QueryBuilderResponsePOJO queryBuilderResponsePojo = new QueryBuilderResponsePOJO();
		boolean isValidCondition = true;
		try {
			FilterDataPOJO filterData = queryBuilderRequestPojo.getRequestData();
			if (Boolean.TRUE.equals(queryBuilderService.isValidConnection(queryBuilderRequestPojo.getConnectionId()))) {
				if (Boolean.TRUE.equals(queryBuilderService.isSchemaExist(queryBuilderRequestPojo.getConnectionId()))) {
					LOGGER.info("schema is valid, validating table and column details");
					if (Boolean.TRUE.equals(queryBuilderService.isValidTable(filterData.getTableName(),
							filterData.getJoinData(), queryBuilderRequestPojo.getConnectionId())
							&& queryBuilderService.isValidColumns(filterData, queryBuilderRequestPojo.getConnectionId()))) {
						if (EmptyNotNull.isValidInput(filterData.getJoinData())
								&& Boolean.FALSE.equals(filterData.getJoinData().getIsPrimaryKey())) {
							isValidCondition = queryBuilderService.joinConditionValidator(filterData,
									queryBuilderRequestPojo.getConnectionId());
						}
						if (Boolean.FALSE.equals(isValidCondition)) {
							queryBuilderResponsePojo.response("Both the join Columns are primary key", null, false);
						} else {
							LOGGER.info(MessageConstants.VALID_TABLECOLUMN);
							Map<String, String> responseMap = new HashMap<>();
							responseMap.put("query", queryBuilderService
									.fetchQuery(queryBuilderRequestPojo.getConnectionId(), filterData).get("selectQuery"));
							queryBuilderResponsePojo.response("Data for the Request", responseMap, true);
						}
					} else {
						LOGGER.error(MessageConstants.NOT_VALID_TABLECOLUMN);
						queryBuilderResponsePojo.response(MessageConstants.NOT_VALID_TABLECOLUMN, false);
					}
				} else {
					LOGGER.error(MessageConstants.NOT_VALID_SCHEMA);
					queryBuilderResponsePojo.response(MessageConstants.NOT_VALID_SCHEMA, false);
				}	
			}else {
				LOGGER.error(MessageConstants.NOT_VALID_CONNECTION);
				queryBuilderResponsePojo.response(MessageConstants.NOT_VALID_CONNECTION, false);
			}
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage());
			queryBuilderResponsePojo.errorResponse(MessageConstants.BAD_REQUEST);
		}
		return queryBuilderResponsePojo;
	}

}
