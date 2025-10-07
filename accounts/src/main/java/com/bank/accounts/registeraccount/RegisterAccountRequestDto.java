package com.bank.accounts.registeraccount;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegisterAccountRequestDto(
        @NotBlank(message = "Invalid security number.")
        @Length(min = 11, max = 11, message = "Invalid security number.")
        String securityNumber
) {
}
