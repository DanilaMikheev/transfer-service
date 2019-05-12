package com.fintech.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author d.mikheev 08.05.19
 */
public class AccountIdValidator implements ConstraintValidator<AccountId, String> {

    private static final Pattern pattern = Pattern.compile("^[0-9]{20}[0-9F]?$");
    private static final Pattern individualPatternRu = Pattern.compile("^40817[0-9]{15}$");

    @Override
    public void initialize(AccountId data) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().equals(""))
            return false;

        return individualPatternRu.matcher(value.trim()).matches();
    }
}
