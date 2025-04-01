package com.grimeet.grimeet.domain.user.validation;
import jakarta.validation.Constraint;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordFormatValidator.class)
@Documented
public @interface PasswordFormat {

    String message() default "비밀번호가 유효하지 않습니다."; // 예외 처리로 삭제 여부 판단

    int minLength() default 8;
    int maxLength() default 20;

    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}