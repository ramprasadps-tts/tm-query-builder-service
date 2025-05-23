package com.tm.querybuilder.condition;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.querybuilder.constant.DataTypeConstants;
import com.tm.querybuilder.enums.Condition;
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
            this.aggregateColumns = aggregates.stream()
                .map(AggregateFunctionPOJO::getColumnName)
                .collect(Collectors.toSet());
        }
    }

    public String fetchCondition(List<ConditionGroupPOJO> conditionGroupList, Map<String, Object> columnDataTypeMap) {
        LOGGER.info("Building condition string");
        StringBuilder conditionBuilder = new StringBuilder();
        try {
            for (int groupIndex = 0; groupIndex < conditionGroupList.size(); groupIndex++) {
                ConditionGroupPOJO conditionGroup = conditionGroupList.get(groupIndex);
                conditionBuilder.append("(");

                List<ConditionPOJO> conditionList = conditionGroup.getConditionList();
                for (int i = 0; i < conditionList.size(); i++) {
                    ConditionPOJO cond = conditionList.get(i);
                    String columnName = cond.getColumn();

                    // Use aggregate if applicable
                    if (aggregateColumns != null && aggregateColumns.contains(columnName)) {
                        columnName = "COUNT(" + columnName + ")";
                    }

                    conditionBuilder.append(columnName)
                        .append(" ")
                        .append(cond.getCondition().getOperator());

                    Object columnType = columnDataTypeMap.get(cond.getColumn());

                    // BETWEEN Handling
                    if (DataTypeConstants.getBetweenData().contains(columnType) && Condition.BETWEEN.equals(cond.getCondition())) {
                        ObjectMapper mapper = new ObjectMapper();
                        ValuesPOJO value = mapper.readValue(mapper.writeValueAsString(cond.getValue()), ValuesPOJO.class);
                        conditionBuilder.append(" '").append(value.getFrom()).append("' AND '").append(value.getTo()).append("'");

                    } else if (DataTypeConstants.getNumData().contains(columnType) && Condition.BETWEEN.equals(cond.getCondition())) {
                        ObjectMapper mapper = new ObjectMapper();
                        ValuesPOJO value = mapper.readValue(mapper.writeValueAsString(cond.getValue()), ValuesPOJO.class);
                        conditionBuilder.append(" ").append(value.getFrom()).append(" AND ").append(value.getTo());

                    // IN/NOT IN Handling
                    } else if (Condition.IN.equals(cond.getCondition()) || Condition.NOTIN.equals(cond.getCondition())) {
                        if (DataTypeConstants.getNumData().contains(columnType)) {
                            List<Integer> list = (List<Integer>) cond.getValue();
                            String intString = list.stream().map(Object::toString).collect(Collectors.joining(","));
                            conditionBuilder.append(" (").append(intString).append(")");
                        } else {
                            List<String> list = (List<String>) cond.getValue();
                            String strList = list.stream().collect(Collectors.joining("','", "'", "'"));
                            conditionBuilder.append(" (").append(strList).append(")");
                        }

                    // LIKE, STARTWITH, ENDWITH Handling
                    } else if (Condition.LIKE.equals(cond.getCondition()) ||
                               Condition.STARTWITH.equals(cond.getCondition()) ||
                               Condition.ENDWITH.equals(cond.getCondition())) {
                        switch (cond.getCondition()) {
                            case STARTWITH:
                                conditionBuilder.append(" '").append(cond.getValue()).append("%'");
                                break;
                            case ENDWITH:
                                conditionBuilder.append(" '%").append(cond.getValue()).append("'");
                                break;
                            case LIKE:
                                conditionBuilder.append(" '%").append(cond.getValue()).append("%'");
                                break;
                            default:
                        }

                    // String vs Numeric Handling
                    } else if (DataTypeConstants.getOperatorString().contains(columnType)) {
                        conditionBuilder.append(" '").append(cond.getValue()).append("'");
                    } else {
                        conditionBuilder.append(" ").append(cond.getValue());
                    }

                    // Append logical condition between conditions
                    if (i < conditionList.size() - 1 && cond.getLogicalCondition() != null) {
                        conditionBuilder.append(" ").append(cond.getLogicalCondition()).append(" ");
                    }
                }

                conditionBuilder.append(")");

                // Append group logical condition between groups
                if (groupIndex < conditionGroupList.size() - 1 && conditionGroup.getLogicalCondition() != null) {
                    conditionBuilder.append(" ").append(conditionGroup.getLogicalCondition().name()).append(" ");
                }
            }
        } catch (Exception exception) {
            LOGGER.error("An error occurred while building where/having condition.");
            throw new DataAccessResourceFailureException("An error occurred while building condition.", exception);
        }
        LOGGER.debug("Condition SQL: {}", conditionBuilder);
        return conditionBuilder.toString();
    }
}

