package com.sparta.nanglangeats.global.common.annotation.validator;

import org.springframework.stereotype.Component;

import com.sparta.nanglangeats.global.common.annotation.Username;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UsernameValidator implements ConstraintValidator<Username, String> {

    private static final int MIN_SIZE = 4;
    private static final int MAX_SIZE = 10;
    private static final String REGEX_USERNAME = "^[a-z0-9]{" + MIN_SIZE + "," + MAX_SIZE + "}$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        boolean isValidUsername = email.matches(REGEX_USERNAME);
        if (!isValidUsername) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("아이디를 확인해 주세요.").addConstraintViolation();
        }
        return isValidUsername;
    }
}
