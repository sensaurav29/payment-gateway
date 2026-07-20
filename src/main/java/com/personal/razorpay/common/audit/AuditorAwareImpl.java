package com.personal.razorpay.common.audit;

import com.personal.razorpay.merchant.security.MerchantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final MerchantContext merchantContext;

    @Override
    public Optional<String> getCurrentAuditor() {

        try{
            String keyId = merchantContext.getKeyId();
            if(keyId != null && !keyId.isBlank()){
                return Optional.of(merchantContext.getKeyId());
            }

            if(merchantContext.getMerchantId() != null){
                return Optional.of(merchantContext.getKeyId());
            }
        }catch (Exception ignored){

        }


        return Optional.of("SYSTEM");
    }
}
