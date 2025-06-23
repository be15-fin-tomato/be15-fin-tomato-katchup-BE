package be15fintomatokatchupbe.common.validation.validator;

import be15fintomatokatchupbe.common.validation.ValidPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(01[0-9]|02|0[3-9][0-9])-(\\d{3,4})-(\\d{4})$"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && PHONE_PATTERN.matcher(value).matches();
    }
}