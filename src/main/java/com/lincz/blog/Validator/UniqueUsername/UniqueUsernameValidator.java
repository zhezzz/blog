package com.lincz.blog.Validator.UniqueUsername;

import com.lincz.blog.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername,String> {

    @Autowired
    private AccountService accountService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (accountService.getAccountByUsername(value) != null){
            return false;
        };
        return true;
    }

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {

    }
}
