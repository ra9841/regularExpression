package com.rabin.practice.project.restApi.intel.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CustomerNameValidator.class)
public @interface ValidateCustomerName {

    public String message()default "invalid username: username name should start from r";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
