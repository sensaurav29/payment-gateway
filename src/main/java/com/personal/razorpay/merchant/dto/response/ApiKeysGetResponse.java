package com.personal.razorpay.merchant.dto.response;

import com.personal.razorpay.common.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiKeysGetResponse (
        UUID id,
        String keyId,
        Environment environment,
        boolean enabled,
        LocalDateTime lastUsedAt,
        LocalDateTime createdAt

){
}
