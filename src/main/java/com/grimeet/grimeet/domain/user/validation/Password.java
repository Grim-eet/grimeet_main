package com.grimeet.grimeet.domain.user.validation;
import jakarta.validation.Constraint;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {

    String message() default "비밀번호가 유효하지 않습니다.";

    String patternMessage() default "비밀번호는 영문 대/소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.";
    String lengthMessage() default "비밀번호는 {minLength}자 이상 {maxLength}자 이하여야 합니다.";

    int minLength() default 8;
    int maxLength() default 20;

    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}