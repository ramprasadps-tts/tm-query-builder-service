package com.tm.querybuilder.validation;

public class EmptyNotNull {
	 
	private EmptyNotNull() {
		super();
	}

	public static boolean isValidInput(Object input) {
		return input != null && !input.toString().isEmpty();
	}

}
