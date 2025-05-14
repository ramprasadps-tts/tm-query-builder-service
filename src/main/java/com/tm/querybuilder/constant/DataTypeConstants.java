package com.tm.querybuilder.constant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.tm.querybuilder.enums.DataType;

public class DataTypeConstants {

	private DataTypeConstants() {
		super();
	}

	private static final Set<String> OPERATOR_STRING = new HashSet<>(
			Arrays.asList(DataType.VARCHAR.getOperator(), DataType.CHAR.getOperator(), DataType.ENUM.getOperator(),
					DataType.TEXT.getOperator(), DataType.DATE.getOperator(), DataType.TIMESTAMP.getOperator(),
					DataType.TIME.getOperator(), DataType.YEAR.getOperator()));

	public static Set<String> getOperatorString() {
		return new HashSet<>(OPERATOR_STRING); // Return a copy to ensure immutability
	}

	private static final Set<String> BETWEEN_DATATYPE = new HashSet<>(Arrays.asList(DataType.DATE.getOperator(),
			DataType.TIME.getOperator(), DataType.TIMESTAMP.getOperator(), DataType.YEAR.getOperator()));

	public static final Set<String> getBetweenData() {
		return new HashSet<>(BETWEEN_DATATYPE);
	}

	private static final Set<String> NUM_DATATYPE = new HashSet<>(
			Arrays.asList(DataType.INT.getOperator(), DataType.FLOAT.getOperator(), DataType.DOUBLE.getOperator(),
					DataType.LONG.getOperator(), DataType.BIGINT.getOperator(), DataType.TINYINT.getOperator(),
					DataType.SMALLINT.getOperator(), DataType.DECIMAL.getOperator(), DataType.MEDIUMINT.getOperator()));

	public static final Set<String> getNumData() {
		return new HashSet<>(NUM_DATATYPE);
	}

	private static final Set<String> IN_DATATYPE = new HashSet<>(
			Arrays.asList(DataType.VARCHAR.getOperator(), DataType.CHAR.getOperator(), DataType.ENUM.getOperator(),
					DataType.TEXT.getOperator(), DataType.DATE.getOperator(), DataType.TIME.getOperator(),
					DataType.TIMESTAMP.getOperator(), DataType.YEAR.getOperator(), DataType.INT.getOperator(),
					DataType.FLOAT.getOperator(), DataType.DOUBLE.getOperator(), DataType.LONG.getOperator(),
					DataType.BIGINT.getOperator(), DataType.TINYINT.getOperator(), DataType.SMALLINT.getOperator(),
					DataType.DECIMAL.getOperator(), DataType.MEDIUMINT.getOperator()));

	public static final Set<String> getInDatatype() {
		return new HashSet<>(IN_DATATYPE);
	}

}
