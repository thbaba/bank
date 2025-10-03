package com.denizcanbank.accounts.application.domain.valueObject;

import com.denizcanbank.accounts.application.domain.exception.ImproperPayloadException;
import com.denizcanbank.accounts.application.domain.repository.IPayloadJsonConverter;

public record Payload(
        String event,
        String accountId,
        String securityNumber,
        String accountNumber,
        String accountType
) {

    public String convertToJson(IPayloadJsonConverter converter) throws ImproperPayloadException {
        return converter.convertToJson(this);
    }

}
