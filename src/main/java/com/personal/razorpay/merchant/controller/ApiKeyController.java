package com.personal.razorpay.merchant.controller;

import com.personal.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.personal.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.personal.razorpay.merchant.dto.response.ApiKeysGetResponse;
import com.personal.razorpay.merchant.entity.ApiKey;
import com.personal.razorpay.merchant.security.MerchantContext;
import com.personal.razorpay.merchant.service.ApiKeyService;
import com.personal.razorpay.merchant.service.impl.ApiKeyServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {
    private final ApiKeyService apiKeyService;
    private final MerchantContext merchantContext;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> createApiKey(@Valid @RequestBody CreateApiKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                apiKeyService.create(merchantContext.getMerchantId(), request)
        );
    }

    @GetMapping
    public ResponseEntity<List<ApiKeysGetResponse>> listByMerchant(){
        return ResponseEntity.ok(apiKeyService.listByMerchant(merchantContext.getMerchantId()));
    }

    @DeleteMapping(path = "/keyId")
    public ResponseEntity<Void> revoke( @PathVariable UUID keyId){
        apiKeyService.revoke(merchantContext.getMerchantId(), keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{keyId}")
    public ResponseEntity<ApiKeyCreateResponse> rotateKey(  @PathVariable UUID keyId) {
        return ResponseEntity.ok(apiKeyService.rotate(merchantContext.getMerchantId(), keyId));
    }
}
