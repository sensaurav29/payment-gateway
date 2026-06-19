package com.personal.razorpay.merchant.service;

import com.personal.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.personal.razorpay.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {
    MerchantResponse signup(MerchantSignupRequest request);
}
