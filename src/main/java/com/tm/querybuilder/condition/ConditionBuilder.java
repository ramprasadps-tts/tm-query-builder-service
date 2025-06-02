package com.tm.querybuilder.condition;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.querybuilder.constant.DataTypeConstants;
import com.tm.querybuilder.enums.Condition;
import com.tm.querybuilder.exception.ConditionBuildException;
import com.tm.querybuilder.pojo.AggregateFunctionPOJO;
import com.tm.querybuilder.pojo.ConditionGroupPOJO;
import com.tm.querybuilder.pojo.ConditionPOJO;
import com.tm.querybuilder.pojo.ValuesPOJO;

public class ConditionBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConditionBuilder.class);
	private Set<String> aggregateColumns;

	public ConditionBuilder() {
	}

	public ConditionBuilder(List<AggregateFunctionPOJO> aggregates) {
		if (aggregates != null) {
			this.aggregateColumns = aggregates.stream().map(AggregateFunctionPOJO::getColumnName)
					.collect(Collectors.toSet());
		}
	}

	public String fetchCondition(List<ConditionGroupPOJO> conditionGroupList, Map<String, Object> columnDataTypeMap) {
		LOGGER.info("Building condition string");
		StringBuilder conditionBuilder = new StringBuilder();
		try {
			for (int groupIndex = 0; groupIndex < conditionGroupList.size(); groupIndex++) {
				appendConditionGroup(conditionGroupList, columnDataTypeMap, conditionBuilder, groupIndex);
			}
		} catch (Exception exception) {
			String errorContext = "An error occurred while building condition for condition groups: "
					+ conditionGroupList;
			throw new ConditionBuildException(errorContext, exception);
		}

		LOGGER.debug("Condition SQL: {}", conditionBuilder);
		return conditionBuilder.toString();
	}

	private void appendConditionGroup(List<ConditionGroupPOJO> groups, Map<String, Object> typeMap,
			StringBuilder builder, int index) throws ConditionBuildException {
		ConditionGroupPOJO group = groups.get(index);
		builder.append("(");
		List<ConditionPOJO> conditionList = group.getConditionList();

		for (int i = 0; i < conditionList.size(); i++) {
			ConditionPOJO cond = conditionList.get(i);
			appendCondition(cond, typeMap, builder);

			if (i < conditionList.size() - 1 && cond.getLogicalCondition() != null) {
				builder.append(" ").append(cond.getLogicalCondition()).append(" ");
			}
		}

		builder.append(")");
		if (index < groups.size() - 1 && group.getLogicalCondition() != null) {
			builder.append(" ").append(group.getLogicalCondition().name()).append(" ");
		}
	}

	private void appendCondition(ConditionPOJO cond, Map<String, Object> typeMap, StringBuilder builder)
			throws ConditionBuildException {
		String columnName = getAggregateColumn(cond.getColumn());
		builder.append(columnName).append(" ").append(cond.getCondition().getOperator());

		Object columnType = typeMap.get(cond.getColumn());
		Condition condition = cond.getCondition();

		try {
			if (Condition.BETWEEN.equals(condition)) {
				appendBetweenCondition(cond, columnType, builder);
			} else if (Condition.IN.equals(condition) || Condition.NOTIN.equals(condition)) {
				appendInCondition(cond, columnType, builder);
			} else if (isLikeCondition(condition)) {
				appendLikeCondition(cond, builder);
			} else {
				appendSimpleCondition(cond, columnType, builder);
			}
		} catch (Exception e) {
			throw new ConditionBuildException("Error processing condition: " + cond, e);
		}
	}

	private void appendBetweenCondition(ConditionPOJO cond, Object columnType, StringBuilder builder)
			throws ConditionBuildException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ValuesPOJO value = mapper.readValue(mapper.writeValueAsString(cond.getValue()), ValuesPOJO.class);

			if (DataTypeConstants.getBetweenData().contains(columnType)) {
				builder.append(" '").append(value.getFrom()).append("' AND '").append(value.getTo()).append("'");
			} else {
				builder.append(" ").append(value.getFrom()).append(" AND ").append(value.getTo());
			}
		} catch (Exception e) {
			throw new ConditionBuildException("Failed to parse BETWEEN values for condition: " + cond, e);
		}
	}

	private void appendInCondition(ConditionPOJO cond, Object columnType, StringBuilder builder) {
		if (DataTypeConstants.getNumData().contains(columnType)) {
			@SuppressWarnings("unchecked")
			List<Integer> list = (List<Integer>) cond.getValue();
			builder.append(" (").append(list.stream().map(Object::toString).collect(Collectors.joining(",")))
					.append(")");
		} else {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) cond.getValue();
			builder.append(" (").append(list.stream().collect(Collectors.joining("','", "'", "'"))).append(")");
		}
	}

	private void appendLikeCondition(ConditionPOJO cond, StringBuilder builder) {
		String value = String.valueOf(cond.getValue());
		switch (cond.getCondition()) {
		case STARTWITH:
			builder.append(" '").append(value).append("%'");
			break;
		case ENDWITH:
			builder.append(" '%").append(value).append("'");
			break;
		case LIKE:
			builder.append(" '%").append(value).append("%'");
			break;
		default:
			break;
		}
	}

	private void appendSimpleCondition(ConditionPOJO cond, Object columnType, StringBuilder builder) {
		if (DataTypeConstants.getOperatorString().contains(columnType)) {
			builder.append(" '").append(cond.getValue()).append("'");
		} else {
			builder.append(" ").append(cond.getValue());
		}
	}

	private boolean isLikeCondition(Condition condition) {
		return Condition.LIKE.equals(condition) || Condition.STARTWITH.equals(condition)
				|| Condition.ENDWITH.equals(condition);
	}

	private String getAggregateColumn(String column) {
		if (aggregateColumns != null && aggregateColumns.contains(column)) {
			return "COUNT(" + column + ")";
		}
		return column;
	}
}
