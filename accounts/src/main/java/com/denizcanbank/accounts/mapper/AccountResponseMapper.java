package com.denizcanbank.accounts.mapper;

import com.denizcanbank.accounts.application.domain.entity.Account;
import com.denizcanbank.accounts.dto.AccountResponseDto;
import org.springframework.stereotype.Component;

@Component
public class AccountResponseMapper implements DtoMapper<Account, AccountResponseDto> {
    @Override
    public AccountResponseDto toDto(Account entity) {
        return new AccountResponseDto(
                entity.id().toString(),
                entity.securityNumber().toString(),
                entity.accountNumber().toString(),
                entity.accountType().toString()
        );
    }
}
