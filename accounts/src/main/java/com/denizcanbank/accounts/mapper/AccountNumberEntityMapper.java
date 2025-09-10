package com.denizcanbank.accounts.mapper;

import com.denizcanbank.accounts.application.domain.valueObject.AccountNumber;
import com.denizcanbank.accounts.dto.AccountNumberRequestDto;
import org.springframework.stereotype.Component;

@Component
public class AccountNumberEntityMapper implements EntityMapper<AccountNumber, AccountNumberRequestDto> {

    @Override
    public AccountNumber toEntity(AccountNumberRequestDto dto) {
        return AccountNumber.of(dto.accountNumber());
    }
}
