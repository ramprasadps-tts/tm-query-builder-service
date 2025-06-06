package com.tm.querybuilder.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ObjectNoWhiteSpaceValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectNoWhiteSpace {
	String message() default "Whitespace validation failed for object";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}