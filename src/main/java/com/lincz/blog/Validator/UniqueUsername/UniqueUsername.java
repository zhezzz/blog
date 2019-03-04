package com.lincz.blog.Validator.UniqueUsername;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUsernameValidator.class)
public @interface UniqueUsername {

	String message() default "用户名重复，请更换用户名再尝试";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
