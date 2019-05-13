package com.fintech.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Account validation annotation
 *
 * @author d.mikheev 08.05.19
 */
@Documented
@Constraint(validatedBy = AccountIdValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountId {
    /**
     * Default validation message.
     */
    String message() default "{validator.constraints.AccountId.message}";

    /**
     * Class groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload type.
     */
    Class<? extends Payload>[] payload() default {};

}
