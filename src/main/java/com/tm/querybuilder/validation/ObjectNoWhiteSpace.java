package com.tm.querybuilder.validation;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ObjectNoWhiteSpaceValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectNoWhiteSpace {
    String message() default "Whitespace validation failed for object";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}