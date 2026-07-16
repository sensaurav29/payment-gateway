package com.personal.razorpay.merchant.service;

import com.personal.razorpay.merchant.dto.request.LoginRequest;
import com.personal.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.personal.razorpay.merchant.dto.response.LoginResponse;
import com.personal.razorpay.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {
    MerchantResponse signup(MerchantSignupRequest request);

    LoginResponse login(LoginRequest request) ;
}