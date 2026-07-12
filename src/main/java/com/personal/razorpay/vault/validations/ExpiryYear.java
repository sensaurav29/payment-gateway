package com.personal.razorpay.vault.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { ExpiryYearValidator.class })
public @interface ExpiryYear {
    String message() default "Expiry Year cannot be in Past";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
