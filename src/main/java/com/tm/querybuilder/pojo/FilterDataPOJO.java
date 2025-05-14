package com.tm.querybuilder.pojo;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public class FilterDataPOJO {

	@NotBlank(message = "Enter the tableName")
	private String tableName;

//	@NotEmpty(message = "String list cannot be empty")
//	@Size(min = 1, message = "Minimum One column should be selected")
//	@NoWhitespaceList
	private List<String> columnNames;

	@Valid
	private List<OrderByPOJO> orderBy;

	@Valid
	private GroupByPOJO groupBy;

	@Valid
	private WhereConditionPOJO whereData;

	@Valid
	private JoinsPOJO joinData;

	@Valid
	private List<AggregateFunctionPOJO> aggregateFunction;

	private int limit;

	private int pageNo;

	public List<AggregateFunctionPOJO> getAggregateFunction() {
		return aggregateFunction;
	}

	public void setAggregateFunction(List<AggregateFunctionPOJO> aggregateFunction) {
		this.aggregateFunction = aggregateFunction;
	}

	public WhereConditionPOJO getWhereData() {
		return whereData;
	}

	public void setWhereData(WhereConditionPOJO wheredata) {
		this.whereData = wheredata;
	}

	public GroupByPOJO getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(GroupByPOJO groupBy) {
		this.groupBy = groupBy;
	}

	public List<OrderByPOJO> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(List<OrderByPOJO> orderBy) {
		this.orderBy = orderBy;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public JoinsPOJO getJoinData() {
		return joinData;
	}

	public void setJoinData(JoinsPOJO joinData) {
		this.joinData = joinData;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

}
