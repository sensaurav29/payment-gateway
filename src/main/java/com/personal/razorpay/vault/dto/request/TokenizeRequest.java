package com.personal.razorpay.vault.dto.request;

import com.personal.razorpay.vault.validations.ExpiryYear;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.LuhnCheck;

import java.util.UUID;

public record TokenizeRequest (

    @NotBlank(message = "PAN is required")
    @LuhnCheck(message = "Invalid Card number")
    @Pattern(regexp = "^[0-9]{13,19}$", message = "Invalid Card number")
    String pan,

    @NotBlank(message = "CVV is required")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "Invalid CVV")
    String cvv,

    @NotNull(message = "Expiry month is required")
    @Min(value = 1, message = "Expiry month must be between 1 and 12")
    @Max(value = 12, message = "Expiry month must be between 1 and 12")
    Integer expiryMonth,

    @NotNull(message = "Expiry year is required")
    @ExpiryYear
    Integer expiryYear,

    UUID customerId
){
}
