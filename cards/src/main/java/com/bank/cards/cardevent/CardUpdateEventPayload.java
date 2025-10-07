package com.bank.cards.cardevent;

import com.bank.cards.changebalance.JsonSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record CardUpdateEventPayload(
        Integer id,
        Integer account_id,
        Float limit,
        Float balance
) {

    public String toJson(ObjectMapper mapper) {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(e.getMessage());
        }
    }

}
