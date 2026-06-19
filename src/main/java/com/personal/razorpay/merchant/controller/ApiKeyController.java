package com.personal.razorpay.merchant.controller;

import com.personal.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.personal.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.personal.razorpay.merchant.entity.ApiKey;
import com.personal.razorpay.merchant.service.ApiKeyService;
import com.personal.razorpay.merchant.service.impl.ApiKeyServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {
    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> createApiKey(@PathVariable UUID merchantId,
                                                             @Valid @RequestBody CreateApiKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                apiKeyService.create(merchantId, request)
        );

    }
}
