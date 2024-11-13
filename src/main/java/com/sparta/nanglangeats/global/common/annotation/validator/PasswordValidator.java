package com.sparta.nanglangeats.global.common.annotation.validator;

import java.text.MessageFormat;

import org.springframework.stereotype.Component;

import com.sparta.nanglangeats.global.common.annotation.Password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final int MIN_SIZE = 8;
    private static final int MAX_SIZE = 15;
    private static final String REGEX_PASSWORD = "^[A-Za-z\\d!@#$%^&*()_+~`\\-={}\\[\\]:;\"'<>?,./]{" + MIN_SIZE + "," + MAX_SIZE + "}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        boolean isValidPassword = password.matches(REGEX_PASSWORD);
        if (!isValidPassword) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            MessageFormat.format("{0}자 이상의 {1}자 이하의 숫자, 대/소문자, 특수문자를 포함한 비밀번호를 입력해주세요", MIN_SIZE, MAX_SIZE))
                    .addConstraintViolation();
        }
        return isValidPassword;
    }
}
