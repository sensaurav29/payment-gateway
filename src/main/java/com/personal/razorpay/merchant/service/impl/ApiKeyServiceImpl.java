package com.personal.razorpay.merchant.service.impl;

import com.personal.razorpay.common.exceptions.ResourceNotFoundException;
import com.personal.razorpay.common.util.RandomizeUtil;
import com.personal.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.personal.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.personal.razorpay.merchant.dto.response.ApiKeysGetResponse;
import com.personal.razorpay.merchant.entity.ApiKey;
import com.personal.razorpay.merchant.entity.Merchant;
import com.personal.razorpay.merchant.repository.ApiKeyRepository;
import com.personal.razorpay.merchant.repository.MerchantRepository;
import com.personal.razorpay.merchant.service.ApiKeyService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final MerchantRepository merchantRepository;


    @Override
    @Transactional
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "rzp_" + request.environment().name().toLowerCase()+ "_" + RandomizeUtil.randomBase64(24);
        String rawSecret =  RandomizeUtil.randomBase64(42);

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

    @Override
    public List<ApiKeysGetResponse> listByMerchant(UUID merchantId) {
        return apiKeyRepository.findByMerchant_Id(merchantId)
                .stream()
                .map(apiKey -> new ApiKeysGetResponse(
                        apiKey.getId(),
                        apiKey.getKeyId(),
                        apiKey.getEnvironment(),
                        apiKey.getEnabled(),
                        apiKey.getLastUsedAt(),
                        null
                ))
                .collect(Collectors.toList());

    }

    @Override

    @Transactional
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("Api Key", keyId));

        // Soft delete only and other methods before initiating a transaction will verify if the key is enabled and then proceed
        apiKey.setEnabled(false);
    }

    @Override
    @Transactional
    public ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("Api Key", keyId));

        String newRawSecret = RandomizeUtil.randomBase64(42);
        apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());
        apiKey.setKeySecretHash(newRawSecret);
        apiKey.setRotatedAt(LocalDateTime.now());
        apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24)); // 24 hours grace period
        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), apiKey.getKeyId(), newRawSecret, apiKey.getEnvironment().toString());

    }
}
