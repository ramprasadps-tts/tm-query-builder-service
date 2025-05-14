package com.tm.querybuilder.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoWhitespaceListValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoWhitespaceList {
	String message() default "String list cannot contain only whitespace";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}