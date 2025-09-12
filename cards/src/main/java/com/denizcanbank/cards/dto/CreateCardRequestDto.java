package com.denizcanbank.cards.dto;

public record CreateCardRequestDto(String accountID, Float limit, Float used) {
}
