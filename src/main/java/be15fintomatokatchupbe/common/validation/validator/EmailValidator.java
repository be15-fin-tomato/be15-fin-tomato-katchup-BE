package be15fintomatokatchupbe.common.validation.validator;

import be15fintomatokatchupbe.common.validation.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$");

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && EMAIL_REGEX.matcher(email).matches();
    }
}