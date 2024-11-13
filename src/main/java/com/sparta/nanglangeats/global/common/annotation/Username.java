package com.sparta.nanglangeats.global.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sparta.nanglangeats.global.common.annotation.validator.UsernameValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface Username {

	String message() default "아이디를 확인해 주세요.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
