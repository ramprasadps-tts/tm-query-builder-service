package com.tm.querybuilder.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ObjectNoWhiteSpaceValidator implements ConstraintValidator<ObjectNoWhiteSpace, Object> {

	@Override
	public void initialize(ObjectNoWhiteSpace constraintAnnotation) {
		// Initialization, if needed
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null) {
			return true; // Null values are considered valid
		}

		String trimmed = value.toString().trim();
		return !trimmed.isEmpty();// Validate white space
	}
}
