package com.rabin.practice.project.restApi.intel.project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomerNameValidator implements ConstraintValidator<ValidateCustomerName,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean result;
        if(value != null && value.startsWith("r")){
            result= true;

        }else{
            result=false;
        }
        return result;
    }
}
