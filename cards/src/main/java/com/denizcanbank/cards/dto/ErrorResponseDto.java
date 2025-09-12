package com.denizcanbank.cards.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(String title, String error, LocalDateTime time) {
}
