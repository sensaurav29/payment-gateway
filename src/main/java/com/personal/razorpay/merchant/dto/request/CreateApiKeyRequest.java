package com.personal.razorpay.merchant.dto.request;


import com.personal.razorpay.common.enums.Environment;
import jakarta.validation.constraints.NotNull;

public record CreateApiKeyRequest (
        @NotNull(message = "Environment is required")
        Environment environment
){
}
