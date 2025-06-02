package com.tm.querybuilder.exception;

public class ConditionBuildException extends RuntimeException {

	public ConditionBuildException(String message) {
		super(message);
	}

	public ConditionBuildException(String message, Throwable cause) {
		super(message, cause);
	}
}
