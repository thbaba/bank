package com.denizcanbank.accounts.exception;

import java.time.LocalDateTime;

public record ServerErrorResponseDto(
        String title,
        String message,
        LocalDateTime time
) {
}
