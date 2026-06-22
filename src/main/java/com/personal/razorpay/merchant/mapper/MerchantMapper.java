package com.personal.razorpay.merchant.mapper;

import com.personal.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.personal.razorpay.merchant.dto.response.MerchantResponse;
import com.personal.razorpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {

    Merchant toEntityFromSignupRequest(MerchantSignupRequest merchantSignupRequest);

    MerchantResponse toResponse(Merchant merchant);
}
