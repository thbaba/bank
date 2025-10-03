package com.denizcanbank.accounts.repository;

import com.denizcanbank.accounts.application.domain.exception.ImproperPayloadException;
import com.denizcanbank.accounts.application.domain.repository.IPayloadJsonConverter;
import com.denizcanbank.accounts.application.domain.valueObject.Payload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayloadJsonConverter implements IPayloadJsonConverter {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToJson(Payload payload) throws ImproperPayloadException {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new ImproperPayloadException(e.getMessage(), payload);
        }
    }
}
