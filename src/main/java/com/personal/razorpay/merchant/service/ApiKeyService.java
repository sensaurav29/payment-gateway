package com.personal.razorpay.merchant.service;

import com.personal.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.personal.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.personal.razorpay.merchant.dto.response.ApiKeysGetResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse create(UUID merchantId, @Valid CreateApiKeyRequest request);

    List<ApiKeysGetResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}
