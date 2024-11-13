package com.sparta.nanglangeats.global.common.annotation.validator;

import org.springframework.stereotype.Component;

import com.sparta.nanglangeats.global.common.annotation.Email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class EmailValidator implements ConstraintValidator<Email, String> {

    private static final String REGEX_EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        boolean isValidEmail = email.matches(REGEX_EMAIL);
        if (!isValidEmail) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("이메일을 확인해 주세요.").addConstraintViolation();
        }
        return isValidEmail;
    }
}
