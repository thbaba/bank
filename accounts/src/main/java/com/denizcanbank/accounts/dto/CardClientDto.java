package com.denizcanbank.accounts.dto;

public record CardClientDto(String cardID, String accountID, Float limit, Float used, Float available) {
}
