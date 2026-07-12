package com.personal.razorpay.vault.service.impl;

import com.personal.razorpay.common.entity.Money;
import com.personal.razorpay.common.enums.CardBrand;
import com.personal.razorpay.common.exceptions.ResourceNotFoundException;
import com.personal.razorpay.common.util.RandomizeUtil;
import com.personal.razorpay.payment.processor.PaymentProcessorRouter;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.personal.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.personal.razorpay.vault.config.VaultEncryptionConfig;
import com.personal.razorpay.vault.dto.request.TokenizeRequest;
import com.personal.razorpay.vault.dto.response.TokenizeResponse;
import com.personal.razorpay.vault.entity.CardToken;
import com.personal.razorpay.vault.entity.VaultCard;
import com.personal.razorpay.vault.repository.CardTokenRepository;
import com.personal.razorpay.vault.repository.VaultCardRepository;
import com.personal.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final CardTokenRepository cardTokenRepository;
    private final VaultCardRepository vaultCardRepository;
    private final BytesEncryptor dekEncryptor;
    private final PaymentProcessorRouter paymentProcessorRouter;

    @Override
    public TokenizeResponse tokenize(TokenizeRequest tokenizeRequest, UUID merchantId) {

        String lastFour = tokenizeRequest.pan().
                substring(tokenizeRequest.pan().length() - 4);
        String bin = tokenizeRequest.pan().substring(0, 6);
        CardBrand cardBrand = detectBrand(tokenizeRequest.pan());

        byte[] dek = KeyGenerators.secureRandom(32).generateKey();
        byte[] encryptedPan = VaultEncryptionConfig.panEncryptor(dek)
                .encrypt(tokenizeRequest.pan().getBytes(StandardCharsets.UTF_8));

        byte[] encryptedDek = dekEncryptor.encrypt(dek);

        VaultCard vaultCard = VaultCard.builder()
                .brand(cardBrand)
                .expiryMonth(tokenizeRequest.expiryMonth().toString())
                .expiryYear(tokenizeRequest.expiryYear().toString())
                .encryptedDek(encryptedDek)
                .encryptedPan(encryptedPan)
                .build();

        vaultCardRepository.save(vaultCard);

        String token = "tok_" + RandomizeUtil.randomBase64(32);

        cardTokenRepository.save(CardToken.builder()
                .merchant(merchantId)
                .token(token)
                .vaultCard(vaultCard)
                .customer(tokenizeRequest.customerId())
                .build());

        return new TokenizeResponse(token, lastFour,cardBrand,
                tokenizeRequest.expiryMonth(), tokenizeRequest.expiryYear());
    }

    @Override
    public PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails) {

        CardToken cardToken = cardTokenRepository.findByTokenAndRevokedAtIsNull(token)
                .orElseThrow(() -> new ResourceNotFoundException("CardToken", token));

        VaultCard vaultCard = cardToken.getVaultCard();
        byte[] panBytes = null;

      try {
          byte[] dek = dekEncryptor.decrypt(vaultCard.getEncryptedDek());
          panBytes = VaultEncryptionConfig.panEncryptor(dek).decrypt(vaultCard.getEncryptedPan());

          String pan = new String(panBytes, StandardCharsets.UTF_8);
          String expiry = vaultCard.getExpiryMonth() + "/" + vaultCard.getExpiryYear();

          PaymentProcessorRequest request = PaymentProcessorRequest.
                  card(paymentId, pan, expiry, amount, methodDetails);

          PaymentProcessorResponse response = paymentProcessorRouter.charge(request);

          log.info("Vault charge registered{}****", token.substring(0,4));
          return response;
      }catch (Exception e){
            log.warn("Vault charge failed for token={}****", token, e);
            return new PaymentProcessorResponse.Failure("VAULT_CHARGE_FAILED", e.getMessage());
      }
      finally {
         if(panBytes != null) Arrays.fill(panBytes, (byte)0);
      }
    }

    private CardBrand detectBrand(String pan) {
        if (pan.startsWith("4")) return CardBrand.VISA;
        if (pan.startsWith("5")) return CardBrand.MASTERCARD;
        if (pan.startsWith("34") || pan.startsWith("37")) return CardBrand.AMEX;
        return CardBrand.RUPAY;
    }
}
