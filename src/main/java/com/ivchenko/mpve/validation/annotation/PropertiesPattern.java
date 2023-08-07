package com.ivchenko.mpve.validation.annotation;

import com.ivchenko.mpve.validation.PropertiesPatternValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

/**
 * Generally, copy of {@link Pattern}
 */
@Documented
@Constraint(validatedBy = PropertiesPatternValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertiesPattern {
    String regexp();

    Pattern.Flag[] flags() default { };

    String message() default "{jakarta.validation.constraints.Pattern.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
