package com.tm.querybuilder.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	@Autowired
	private QueryBuilderService queryBuilderService;

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
			if (Boolean.TRUE.equals(queryBuilderService.isValidConnection(schemaPojo.getConnectionId()))) {
				if (Boolean.TRUE.equals(queryBuilderService.isSchemaExist(schemaPojo.getConnectionId()))) {
					LOGGER.info("schema is valid and fetching the details for tables");
					List<ColumnDetailsDTO> columnDetailList = queryBuilderService
							.fetchColumnDetails(schemaPojo.getConnectionId());
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
							tableDetailPojo = tablesMap.get(columnDetails.getTableName());
							tableDetailPojo.setPrimarykey(columnDetails.getColumnName());
						} else if (KeyColumn.MUL.equals(columnDetails.getColumnKey())) {
							List<ForeignKeysPOJO> foreignKeyList = new ArrayList<>();
							ForeignKeysPOJO foreignKeys = new ForeignKeysPOJO();
							foreignKeys.setColumnName(columnDetails.getColumnName());
							foreignKeys.setReferencecolumn(columnDetails.getReferenceColumn());
							foreignKeys.setReferenceTable(columnDetails.getReferenceTable());
							tableDetailPojo = tablesMap.get(columnDetails.getTableName());
							foreignKeyList.add(foreignKeys);
							tableDetailPojo.setForeignKeys(foreignKeyList);
						}
					}
					queryBuilderResponsePojo.response("Table Details of the Schema", tablesMap, true);
				} else {
					LOGGER.error("Not Valid schema");
					queryBuilderResponsePojo.response(MessageConstants.NOT_VALID_SCHEMA, false);
				}
			} else {
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
