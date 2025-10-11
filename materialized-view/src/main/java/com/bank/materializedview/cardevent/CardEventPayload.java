package com.bank.materializedview.cardevent;

public record CardEventPayload(
        Integer id,
        Integer account_id,
        Float limit,
        Float balance
) {
}
