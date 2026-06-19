package com.personal.razorpay.merchant.dto.request;

import com.personal.razorpay.common.enums.BusinessType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MerchantSignupRequest(


        @NotNull(message = "Name is required")
        @Size(max = 50, message = "Name should not be more than 50 characters")
        String name,

        @Email(message = "Email should be valid")
        @NotNull(message = "Email is required")
        String email,

        @NotNull(message = "Password is required")
        @Size(min = 8, message = "Password should be at least 8 characters long")
        String password,

        @Size(max = 50, message = "Business name should not be more than 50 characters")
        String businessName,

        // This will be validated as it is an enum and any value not specified in the enum will simply be discarded and saved in the db as null
        BusinessType businessType

) {
}
