package com.ivchenko.mpve.validation;

import com.ivchenko.mpve.validation.annotation.PropertiesPattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.engine.messageinterpolation.util.InterpolationHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

/**
 * Generally, copy of {@link org.hibernate.validator.internal.constraintvalidators.bv.PatternValidator}
 */
@Component
@RequiredArgsConstructor
public class PropertiesPatternValidator implements ConstraintValidator<PropertiesPattern, CharSequence> {
    private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());

    private java.util.regex.Pattern pattern;
    private String escapedRegexp;

    /**
     * Autowired properties configured by Spring
     */
    private final MessageSource messageSource;

    @Override
    public void initialize(PropertiesPattern parameters) {
        Pattern.Flag[] flags = parameters.flags();
        int intFlag = 0;
        for (Pattern.Flag flag : flags) {
            intFlag = intFlag | flag.getValue();
        }

        String regex = this.messageSource.getMessage(parameters.regexp(), null, null);
        try {
            pattern = java.util.regex.Pattern.compile(regex, intFlag);
        } catch (PatternSyntaxException e) {
            throw LOG.getInvalidRegularExpressionException(e);
        }

        escapedRegexp = InterpolationHelper.escapeMessageParameter(regex);
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
            constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class).addMessageParameter("regexp", escapedRegexp);
        }

        Matcher m = pattern.matcher(value);
        return m.matches();
    }
}
