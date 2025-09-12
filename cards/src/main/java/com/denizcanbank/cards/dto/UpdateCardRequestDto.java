package com.denizcanbank.cards.dto;

public record UpdateCardRequestDto(String cardID, Float limit, Float used) {
}
