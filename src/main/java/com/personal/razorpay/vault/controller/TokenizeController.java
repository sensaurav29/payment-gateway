package com.personal.razorpay.vault.controller;

import com.personal.razorpay.vault.dto.request.TokenizeRequest;
import com.personal.razorpay.vault.dto.response.TokenizeResponse;
import com.personal.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/vault")
public class TokenizeController {

    private final VaultService vauiltService;
    UUID merchantId = UUID.fromString("64606a7e-8782-4f99-b4f6-9436670fdad4"); // TODO: use API KEY and secret for securely verifying the merchantId;


    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@RequestBody TokenizeRequest tokenizeRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vauiltService.tokenize(tokenizeRequest, merchantId));
    }
}
