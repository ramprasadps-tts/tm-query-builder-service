package com.tm.querybuilder.controller;

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
import com.tm.querybuilder.pojo.request.DbConnectionRequestPOJO;
import com.tm.querybuilder.pojo.response.QueryBuilderResponsePOJO;
import com.tm.querybuilder.service.QueryBuilderService;
import com.tm.querybuilder.validation.EmptyNotNull;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/database")
public class DatabaseConnectionController {

	@Autowired
	private QueryBuilderService queryBuilderService;

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnectionController.class);

	@PostMapping("/connection")
	public QueryBuilderResponsePOJO getDatabaseConnection(
			@Valid @RequestBody DbConnectionRequestPOJO dbConnectionRequestPojo) {
		QueryBuilderResponsePOJO queryBuilderResponsePojo = new QueryBuilderResponsePOJO();
		try {
			String response = queryBuilderService.getDatabaseConnection(dbConnectionRequestPojo);
			if (EmptyNotNull.isValidInput(response)) {
				queryBuilderResponsePojo.response("Connected Database", response, true);
			} else {
				queryBuilderResponsePojo.errorResponse("Not a Valid connection");
			}
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage());
			queryBuilderResponsePojo.errorResponse(MessageConstants.BAD_REQUEST);
		}
		return queryBuilderResponsePojo;
	}

}
