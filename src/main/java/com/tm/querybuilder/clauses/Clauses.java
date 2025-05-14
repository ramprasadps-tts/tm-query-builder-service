package com.tm.querybuilder.clauses;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.CollectionUtils;

import com.tm.querybuilder.condition.ConditionBuilder;
import com.tm.querybuilder.constant.QueryConstants;
import com.tm.querybuilder.pojo.ConditionGroupPOJO;
import com.tm.querybuilder.pojo.FilterDataPOJO;
import com.tm.querybuilder.pojo.GroupByPOJO;
import com.tm.querybuilder.pojo.JoinConditionPOJO;
import com.tm.querybuilder.pojo.JoinDataPOJO;
import com.tm.querybuilder.validation.EmptyNotNull;

public class Clauses {

	private static final Logger LOGGER = LoggerFactory.getLogger(Clauses.class);

	/**
	 * Build the where Clause and get the condition by fetch condition method
	 * 
	 * @param filterData
	 * @param datatype
	 * @return
	 */
	public String whereCondition(FilterDataPOJO filterData, Map<String, Object> datatype) {
		ConditionBuilder condition = new ConditionBuilder();
		StringBuilder querBuilder = new StringBuilder();
		try {
			querBuilder.append(QueryConstants.WHERE)
					.append(condition.fetchCondition(filterData.getWhereData().getConditionData(), datatype));
		} catch (Exception exception) {
			LOGGER.error("An error occurred while Where condition.");
			throw new DataAccessResourceFailureException("An error occurred while  query.", exception);
		}
		LOGGER.debug("where Conditions : {}", condition);
		return querBuilder.toString();
	}

	/**
	 * This method build on condition in joins using string builder.
	 * 
	 * @param joinDataList
	 * @param schemaString
	 * @return
	 */
	public String getOnCondition(List<JoinDataPOJO> joinDataList, String schemaString) {
		LOGGER.info("build On condition using string builder method");
		StringBuilder conditionBuilder = new StringBuilder();
		try {
			for (JoinDataPOJO joinData : joinDataList) {
				conditionBuilder.append(" ").append(joinData.getJoinType().getOperator()).append(" ")
						.append(schemaString).append(".").append(joinData.getJoinTableName()).append(" ");
				if (!QueryConstants.CROSSJOIN.equals(joinData.getJoinType().getOperator())
						&& !CollectionUtils.isEmpty(joinData.getJoinCondition())) {
					conditionBuilder.append("ON");
					for (JoinConditionPOJO joinConditionDto : joinData.getJoinCondition()) {
						conditionBuilder.append(" ").append("(").append(joinConditionDto.getLsColumn())
								.append(" ").append(joinConditionDto.getCondition().getOperator()).append(" ")
								.append(joinConditionDto.getRsColumn()).append(")");
						if (EmptyNotNull.isValidInput(joinConditionDto.getLogicalCondition())) {
							conditionBuilder.append(" ").append(joinConditionDto.getLogicalCondition());
						}
					}
				}
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while Getting Join condition query.");
			throw new DataAccessResourceFailureException("An error occurred while getting Join condition query.",
					exception);
		}
		LOGGER.debug("On conditon : {}", conditionBuilder);
		return conditionBuilder.toString();
	}

	/**
	 * Build the Group by by using list of columns in and group by column list
	 * 
	 * 
	 * @param groupBy
	 * @param columnsList
	 * @return
	 */
	public String groupBy(GroupByPOJO groupBy, List<String> columnsList) {
		LOGGER.info("**Group by service**");
		StringBuilder groupByBuilder = new StringBuilder();
		try {
			Set<String> columnList = new HashSet<>();
			if (!CollectionUtils.isEmpty(groupBy.getColumnList())) {
				columnList.addAll(groupBy.getColumnList());
			}
			columnList.addAll(columnsList);
			groupByBuilder.append(QueryConstants.GROUPBY).append(String.join(",", columnList));
		} catch (Exception exception) {
			LOGGER.error("An error occurred while get group by in service.");
			throw new DataAccessResourceFailureException("An error occurred while get group by in service", exception);
		}
		return groupByBuilder.toString();
	}

	/**
	 * Build the having clause
	 * 
	 * @param conditionGroup
	 * @param datatype
	 * @return
	 */
	public String having(List<ConditionGroupPOJO> conditionGroup, Map<String, Object> datatype) {
		ConditionBuilder condition = new ConditionBuilder();
		StringBuilder havingBuilder = new StringBuilder();
		try {
			havingBuilder.append(QueryConstants.HAVING).append(condition.fetchCondition(conditionGroup, datatype));
		} catch (Exception exception) {
			LOGGER.error("An error occurred while get having in Clauses");
			throw new DataAccessResourceFailureException("An error occurred while get group by in service", exception);
		}
		return havingBuilder.toString();
	}

}
