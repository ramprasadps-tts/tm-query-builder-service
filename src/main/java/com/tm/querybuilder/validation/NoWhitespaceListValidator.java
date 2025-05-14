package com.tm.querybuilder.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NoWhitespaceListValidator implements ConstraintValidator<NoWhitespaceList, List<String>> {
	@Override
	public boolean isValid(List<String> value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			return true; // Allow empty list
		}

		for (String item : value) {
			if (item == null || item.trim().isEmpty()) {
				return false; // Invalid if item is null or contains only whitespace
			}
		}

		return true; // Valid
	}
}