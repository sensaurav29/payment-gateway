package com.personal.razorpay.merchant.dto.response;

import java.util.UUID;

public record ApiKeyCreateResponse(
        UUID id,
        String keyId,
        String keySecret,
        String environment
) {
}
