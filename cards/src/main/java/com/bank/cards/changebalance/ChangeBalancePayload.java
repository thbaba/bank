package com.bank.cards.changebalance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record ChangeBalancePayload(
        Integer account_id,
        Float amount
) {

    public String toJson(ObjectMapper mapper) {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(e.getMessage());
        }
    }

}
