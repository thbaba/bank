package com.denizcanbank.accounts.application.domain.repository;

import com.denizcanbank.accounts.application.domain.exception.ImproperPayloadException;
import com.denizcanbank.accounts.application.domain.valueObject.Payload;

public interface IPayloadJsonConverter {

    String convertToJson(Payload payload) throws ImproperPayloadException;

}
