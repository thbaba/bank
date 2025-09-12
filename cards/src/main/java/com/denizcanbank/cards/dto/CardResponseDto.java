package com.denizcanbank.cards.dto;

public record CardResponseDto(String cardID, String accountID, Float limit, Float used, Float available) {
}
