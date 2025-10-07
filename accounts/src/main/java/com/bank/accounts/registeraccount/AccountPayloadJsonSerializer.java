package com.bank.accounts.registeraccount;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountPayloadJsonSerializer implements IJsonSerializer<AccountPayload> {

    private final ObjectMapper mapper;

    @Override
    public String serialize(AccountPayload accountPayload) throws JsonSerializerException {
        try {
            return mapper.writeValueAsString(accountPayload);
        } catch (JsonProcessingException e) {
            throw new JsonSerializerException(e.getMessage());
        }
    }
}
