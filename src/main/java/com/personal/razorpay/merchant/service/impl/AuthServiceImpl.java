package com.personal.razorpay.merchant.service.impl;
import com.personal.razorpay.common.enums.MerchantStatus;
import com.personal.razorpay.common.enums.UserRole;
import com.personal.razorpay.common.exceptions.DuplicateResourceException;
import com.personal.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.personal.razorpay.merchant.dto.response.MerchantResponse;
import com.personal.razorpay.merchant.entity.AppUser;
import com.personal.razorpay.merchant.entity.Merchant;
import com.personal.razorpay.merchant.mapper.MerchantMapper;
import com.personal.razorpay.merchant.repository.AppUserRepository;
import com.personal.razorpay.merchant.repository.MerchantRepository;
import com.personal.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

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
                .passwordHash(request.password()) // TODO: Need to encrypt the password using bcrypt and store the hash here
                .role(UserRole.OWNER)
                .build();

        appUserRepository.save(appUser);


        return merchantMapper.toResponse(merchant);

    }
}
