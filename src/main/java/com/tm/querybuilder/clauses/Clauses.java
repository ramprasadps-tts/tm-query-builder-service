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
import com.tm.querybuilder.pojo.AggregateFunctionPOJO;
import com.tm.querybuilder.pojo.ConditionGroupPOJO;
import com.tm.querybuilder.pojo.FilterDataPOJO;
import com.tm.querybuilder.pojo.GroupByPOJO;
import com.tm.querybuilder.pojo.JoinConditionPOJO;
import com.tm.querybuilder.pojo.JoinDataPOJO;
import com.tm.querybuilder.validation.EmptyNotNull;

public class Clauses {

	private static final Logger LOGGER = LoggerFactory.getLogger(Clauses.class);

	public String whereCondition(FilterDataPOJO filterData, Map<String, Object> datatype) {
		ConditionBuilder condition = new ConditionBuilder();
		StringBuilder querBuilder = new StringBuilder();
		try {
			querBuilder.append(QueryConstants.WHERE)
					.append(condition.fetchCondition(filterData.getWhereData().getConditionData(), datatype));
		} catch (Exception exception) {
			LOGGER.error("An error occurred while building WHERE condition.");
			throw new DataAccessResourceFailureException("An error occurred while building WHERE condition.",
					exception);
		}
		LOGGER.debug("WHERE Conditions : {}", querBuilder);
		return querBuilder.toString();
	}

	public String getOnCondition(List<JoinDataPOJO> joinDataList, String schemaString) {
		LOGGER.info("Build ON condition using string builder method");
		StringBuilder conditionBuilder = new StringBuilder();
		try {
			for (JoinDataPOJO joinData : joinDataList) {
				conditionBuilder.append(" ").append(joinData.getJoinType().getOperator()).append(" ")
						.append(schemaString).append(".").append(joinData.getJoinTableName()).append(" ");
				if (!QueryConstants.CROSSJOIN.equals(joinData.getJoinType().getOperator())
						&& !CollectionUtils.isEmpty(joinData.getJoinCondition())) {
					conditionBuilder.append("ON");
					for (JoinConditionPOJO joinConditionDto : joinData.getJoinCondition()) {
						conditionBuilder.append(" (").append(joinConditionDto.getLsColumn()).append(" ")
								.append(joinConditionDto.getCondition().getOperator()).append(" ")
								.append(joinConditionDto.getRsColumn()).append(")");
						if (EmptyNotNull.isValidInput(joinConditionDto.getLogicalCondition())) {
							conditionBuilder.append(" ").append(joinConditionDto.getLogicalCondition());
						}
					}
				}
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while building JOIN condition.");
			throw new DataAccessResourceFailureException("An error occurred while building JOIN condition.", exception);
		}
		LOGGER.debug("ON condition: {}", conditionBuilder);
		return conditionBuilder.toString();
	}

	public String groupBy(GroupByPOJO groupBy, List<String> columnsList) {
		LOGGER.info("Building GROUP BY clause");
		StringBuilder groupByBuilder = new StringBuilder();
		try {
			Set<String> columnList = new HashSet<>();
			if (!CollectionUtils.isEmpty(groupBy.getColumnList())) {
				columnList.addAll(groupBy.getColumnList());
			}
			columnList.addAll(columnsList);
			groupByBuilder.append(QueryConstants.GROUPBY).append(String.join(",", columnList));
		} catch (Exception exception) {
			LOGGER.error("An error occurred while building GROUP BY clause.");
			throw new DataAccessResourceFailureException("An error occurred while building GROUP BY clause.",
					exception);
		}
		return groupByBuilder.toString();
	}

	/**
	 * Build the HAVING clause using aggregates
	 *
	 * @param conditionGroup the HAVING conditions
	 * @param datatype       the column data types
	 * @param aggregates     the aggregate functions used in SELECT
	 * @return a valid HAVING clause
	 */
	public String having(List<ConditionGroupPOJO> conditionGroup, Map<String, Object> datatype,
			List<AggregateFunctionPOJO> aggregates) {
		ConditionBuilder condition = new ConditionBuilder(aggregates); // Pass aggregate functions
		StringBuilder havingBuilder = new StringBuilder();
		try {
			havingBuilder.append(QueryConstants.HAVING).append(condition.fetchCondition(conditionGroup, datatype));
		} catch (Exception exception) {
			LOGGER.error("An error occurred while building HAVING clause.");
			throw new DataAccessResourceFailureException("An error occurred while building HAVING clause.", exception);
		}
		return havingBuilder.toString();
	}
}
