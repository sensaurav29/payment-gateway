package com.personal.razorpay.merchant.service.impl;
import com.personal.razorpay.common.enums.MerchantStatus;
import com.personal.razorpay.common.enums.UserRole;
import com.personal.razorpay.common.exceptions.DuplicateResourceException;
import com.personal.razorpay.common.exceptions.ResourceNotFoundException;
import com.personal.razorpay.merchant.dto.request.LoginRequest;
import com.personal.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.personal.razorpay.merchant.dto.response.LoginResponse;
import com.personal.razorpay.merchant.dto.response.MerchantResponse;
import com.personal.razorpay.merchant.entity.AppUser;
import com.personal.razorpay.merchant.entity.Merchant;
import com.personal.razorpay.merchant.mapper.MerchantMapper;
import com.personal.razorpay.merchant.repository.AppUserRepository;
import com.personal.razorpay.merchant.repository.MerchantRepository;
import com.personal.razorpay.merchant.security.JwtUtil;
import com.personal.razorpay.merchant.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {
        if (merchantRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("DUPLICATE_MERCHANT", "Merchant with email " + request.email() + " already exists");
        }

        Merchant merchant = merchantMapper.toEntityFromSignupRequest(request);
        merchant.setStatus(MerchantStatus.PENDING_KYC);
        
        merchant = merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .name(request.name())
                .email(request.email())
                .merchant(merchant)
                .passwordHash(passwordEncoder.encode(request.password())) // TODO: Need to encrypt the password using bcrypt and store the hash here
                .role(UserRole.OWNER)
                .build();

        appUserRepository.save(appUser);


        return merchantMapper.toResponse(merchant);

    }

    @Override
    public LoginResponse login(@Valid LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AppUser appUser = appUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.email()));

        String token = jwtUtil.getAccessToken(request.email(), appUser.getMerchant().getId(), appUser.getRole().name());
        return new LoginResponse(token);
    }
}
