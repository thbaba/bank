package com.bank.cards.changebalance;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ChangeBalanceRequestDto(
        @NotNull Integer cardId,
        @NotNull @Positive Float amount
) {
}
