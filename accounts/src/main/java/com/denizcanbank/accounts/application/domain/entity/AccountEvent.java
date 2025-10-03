package com.denizcanbank.accounts.application.domain.entity;

import com.denizcanbank.accounts.application.domain.valueObject.EventName;
import com.denizcanbank.accounts.application.domain.valueObject.EventStatus;
import com.denizcanbank.accounts.application.domain.valueObject.ID;
import com.denizcanbank.accounts.application.domain.valueObject.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountEvent {
    private ID id;

    private EventName name;

    private ID aggregateId;

    private Payload payload;

    private EventStatus status;
}
