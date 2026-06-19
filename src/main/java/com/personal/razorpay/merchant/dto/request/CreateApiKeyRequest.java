package com.personal.razorpay.merchant.dto.request;


import com.personal.razorpay.common.enums.Environment;

public record CreateApiKeyRequest (
        Environment environment
){
}
