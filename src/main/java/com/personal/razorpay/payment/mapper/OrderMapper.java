package com.personal.razorpay.payment.mapper;

import com.personal.razorpay.payment.dto.response.OrderResponse;
import com.personal.razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toResponse(OrderRecord order);
}
