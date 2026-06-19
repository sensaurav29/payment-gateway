package com.personal.razorpay.merchant.service.impl;

import com.personal.razorpay.common.exceptions.ResourceNotFoundException;
import com.personal.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.personal.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.personal.razorpay.merchant.entity.ApiKey;
import com.personal.razorpay.merchant.entity.Merchant;
import com.personal.razorpay.merchant.repository.ApiKeyRepository;
import com.personal.razorpay.merchant.repository.MerchantRepository;
import com.personal.razorpay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final MerchantRepository merchantRepository;


    @Override
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "rzp_" + request.environment().name().toUpperCase() + "big_random_string";
        String rawSecret =  "big_random_string";

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret)
                .environment(request.environment())
                .enabled(true)
                .build();
        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), keyId, rawSecret, request.environment().toString());
    }
}
