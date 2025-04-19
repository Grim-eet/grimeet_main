package com.grimeet.grimeet.domain.user.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface Email {

    String message() default "유효하지 않은 이메일 형식입니다.";

    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
