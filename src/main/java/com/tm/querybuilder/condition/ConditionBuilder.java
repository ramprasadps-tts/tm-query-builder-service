package com.tm.querybuilder.condition;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.querybuilder.constant.DataTypeConstants;
import com.tm.querybuilder.enums.Condition;
import com.tm.querybuilder.pojo.ConditionGroupPOJO;
import com.tm.querybuilder.pojo.ConditionPOJO;
import com.tm.querybuilder.pojo.ValuesPOJO;

public class ConditionBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConditionBuilder.class);

	/**
	 * Build the condition by using condition list and datatype of columns for where
	 * clause conditon and having clause.
	 * 
	 * @param filterData
	 * @param datatypeMap
	 * @return
	 */
	public String fetchCondition(List<ConditionGroupPOJO> conditionGroupList, Map<String, Object> columnDataTypeMap) {
		LOGGER.info("building where condition method");
		StringBuilder conditionBuilder = new StringBuilder();
		try {
			for (ConditionGroupPOJO conditionGroup : conditionGroupList) {
				conditionBuilder.append("(");
				for (ConditionPOJO conditionList : conditionGroup.getConditionList()) {
					conditionBuilder.append(conditionList.getColumn()).append(" ")
							.append(conditionList.getCondition().getOperator());
					if (DataTypeConstants.getBetweenData().contains(columnDataTypeMap.get(conditionList.getColumn()))
							&& Condition.BETWEEN.equals(conditionList.getCondition())) {
						ObjectMapper mapper = new ObjectMapper();
						ValuesPOJO value = mapper.readValue(mapper.writeValueAsString(conditionList.getValue()),
								ValuesPOJO.class);
						conditionBuilder.append(" '").append(value.getFrom()).append("' ").append("AND ").append("'")
								.append(value.getTo()).append("'");
					} else if (DataTypeConstants.getNumData().contains(columnDataTypeMap.get(conditionList.getColumn()))
							&& Condition.BETWEEN.equals(conditionList.getCondition())) {
						ObjectMapper mapper = new ObjectMapper();
						ValuesPOJO value = mapper.readValue(mapper.writeValueAsString(conditionList.getValue()),
								ValuesPOJO.class);
						conditionBuilder.append(" ").append(value.getFrom()).append(" ").append("AND ")
								.append(value.getTo()).append(" ");
					} else if (Condition.IN.equals(conditionList.getCondition())
							|| Condition.NOTIN.equals(conditionList.getCondition())) {
						Object value;
						if (DataTypeConstants.getNumData().contains(columnDataTypeMap.get(conditionList.getColumn()))) {
							List<Integer> list = (List<Integer>) conditionList.getValue();
							String intString = list.stream().map(Object::toString).collect(Collectors.joining(","));
							value = intString;
						} else {
							List<String> list = (List<String>) conditionList.getValue();
							value = list.stream().collect(Collectors.joining("','", "'", "'"));
						}
						conditionBuilder.append(" (").append(value).append(")");
					} else if (Condition.LIKE.equals(conditionList.getCondition())
							|| Condition.STARTWITH.equals(conditionList.getCondition())
							|| Condition.ENDWITH.equals(conditionList.getCondition())) {
						switch (conditionList.getCondition()) {
						case STARTWITH:
							conditionBuilder.append(" '").append(conditionList.getValue()).append("%").append("'");
							break;
						case ENDWITH:
							conditionBuilder.append(" '").append("%").append(conditionList.getValue()).append("'");
							break;
						case LIKE:
							conditionBuilder.append(" '").append("%").append(conditionList.getValue()).append("%")
									.append("'");
							break;
						default:
						}
					}
					// check whether the column data type is a part of operater list to add single
					// quotes in prefix and suffix
					else if (DataTypeConstants.getOperatorString()
							.contains(columnDataTypeMap.get(conditionList.getColumn()))) {
						conditionBuilder.append("'").append(conditionList.getValue()).append("'");
					} else {
						conditionBuilder.append(conditionList.getValue());
					}
					// Append condition to the where group list if the condition has value
					// ConditionBuilder will be null if it is the last item of the list.
					if (conditionList.getLogicalCondition() != null) {
						conditionBuilder.append(" ").append(conditionList.getLogicalCondition()).append(" ");
					}
				}
				conditionBuilder.append(")");
				// Append condition to the where list if the condition has value
				// ConditionBuilder will be null if it is the last item of the list
				if (conditionGroup.getLogicalCondition() != null) {
					conditionBuilder.append(" ").append(conditionGroup.getLogicalCondition().name()).append(" ");
				}
			}
		} catch (Exception exception) {
			LOGGER.error("An error occurred while building where condition. ");
			throw new DataAccessResourceFailureException("An error occurred while building where condition .",
					exception);
		}
		LOGGER.debug("where ConditionBuilder:{}", conditionBuilder);
		return conditionBuilder.toString();
	}
}
