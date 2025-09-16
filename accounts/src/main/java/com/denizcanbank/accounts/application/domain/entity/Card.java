package com.denizcanbank.accounts.application.domain.entity;

import com.denizcanbank.accounts.application.domain.valueObject.ID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Card {

    private ID cardID;

    private ID accountID;

    private Float totalLimit;

    private Float amountUsed;

    private Float availableAmount;
}
