package com.bank.cards.registercard;

public record RegisterCardRequestDto(
        String securityNumber,
        Float limit
) {
}
