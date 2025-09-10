package com.denizcanbank.accounts.dto;

public record AccountUpdateRequestDto(
        String accountNumber, String securityNumber, String accountType
) {
}
