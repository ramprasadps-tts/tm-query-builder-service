package com.tm.querybuilder.controller;

import java.util.ArrayList;
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
import com.tm.querybuilder.dto.ColumnDetailsDTO;
import com.tm.querybuilder.enums.KeyColumn;
import com.tm.querybuilder.pojo.ForeignKeysPOJO;
import com.tm.querybuilder.pojo.TableDetailPOJO;
import com.tm.querybuilder.pojo.request.SchemaRequestPOJO;
import com.tm.querybuilder.pojo.response.QueryBuilderResponsePOJO;
import com.tm.querybuilder.service.QueryBuilderService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/columndetails")
public class ColumnDetailController {

	private final QueryBuilderService queryBuilderService;

	public ColumnDetailController(QueryBuilderService queryBuilderService) {
		this.queryBuilderService = queryBuilderService;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ColumnDetailController.class);

	/**
	 * By using schema name get the list of tables and column and its datatype. In
	 * this API it will check the schema is valid and for the schema it should have
	 * atleast one Table in the schema to get the details of table.
	 * 
	 * @param schemaPojo
	 * @return QueryBuilderResponsePOJO
	 */
	@PostMapping("/fetchColumnDetails")
	public QueryBuilderResponsePOJO fetchColumnDetails(@Valid @RequestBody SchemaRequestPOJO schemaPojo) {
		LOGGER.info("fetch TableDetailPOJO Details Api:");
		QueryBuilderResponsePOJO queryBuilderResponsePojo = new QueryBuilderResponsePOJO();

		try {
			if (!Boolean.TRUE.equals(queryBuilderService.isValidConnection(schemaPojo.getConnectionId()))) {
				LOGGER.error(MessageConstants.NOT_VALID_CONNECTION);
				return queryBuilderResponsePojo.response(MessageConstants.NOT_VALID_CONNECTION, false);
			}

			if (!Boolean.TRUE.equals(queryBuilderService.isSchemaExist(schemaPojo.getConnectionId()))) {
				LOGGER.error("Not Valid schema");
				return queryBuilderResponsePojo.response(MessageConstants.NOT_VALID_SCHEMA, false);
			}

			LOGGER.info("schema is valid and fetching the details for tables");
			List<ColumnDetailsDTO> columnDetailList = queryBuilderService
					.fetchColumnDetails(schemaPojo.getConnectionId());
			Map<String, TableDetailPOJO> tablesMap = buildTablesMap(columnDetailList);
			queryBuilderResponsePojo.response("Table Details of the Schema", tablesMap, true);
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage());
			queryBuilderResponsePojo.errorResponse(MessageConstants.BAD_REQUEST);
		}

		return queryBuilderResponsePojo;
	}

	private Map<String, TableDetailPOJO> buildTablesMap(List<ColumnDetailsDTO> columnDetailList) {
		Map<String, TableDetailPOJO> tablesMap = new HashMap<>();
		for (ColumnDetailsDTO columnDetails : columnDetailList) {
			TableDetailPOJO tableDetailPojo = new TableDetailPOJO();
			if (tablesMap.containsKey(columnDetails.getTableName())) {
				tableDetailPojo = tablesMap.get(columnDetails.getTableName());
				Map<String, String> column = tableDetailPojo.getColumn();
				column.put(columnDetails.getColumnName(), columnDetails.getDataType());
			} else {
				Map<String, String> column = new HashMap<>();
				column.put(columnDetails.getColumnName(), columnDetails.getDataType());
				tableDetailPojo.setColumn(column);
				tablesMap.put(columnDetails.getTableName(), tableDetailPojo);
			}
			if (KeyColumn.PRI.equals(columnDetails.getColumnKey())) {
				tableDetailPojo.setPrimarykey(columnDetails.getColumnName());
			} else if (KeyColumn.MUL.equals(columnDetails.getColumnKey())) {
				addForeignKey(tableDetailPojo, columnDetails);
			}
		}
		return tablesMap;
	}

	private void addForeignKey(TableDetailPOJO tableDetailPojo, ColumnDetailsDTO columnDetails) {
		List<ForeignKeysPOJO> foreignKeyList = new ArrayList<>();
		ForeignKeysPOJO foreignKeys = new ForeignKeysPOJO();
		foreignKeys.setColumnName(columnDetails.getColumnName());
		foreignKeys.setReferencecolumn(columnDetails.getReferenceColumn());
		foreignKeys.setReferenceTable(columnDetails.getReferenceTable());
		foreignKeyList.add(foreignKeys);
		tableDetailPojo.setForeignKeys(foreignKeyList);
	}

}
