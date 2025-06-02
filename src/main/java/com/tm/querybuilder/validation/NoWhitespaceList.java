package com.tm.querybuilder.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = NoWhitespaceListValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoWhitespaceList {
	String message() default "String list cannot contain only whitespace";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}