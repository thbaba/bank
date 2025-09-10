package com.denizcanbank.accounts.mapper;

import com.denizcanbank.accounts.application.domain.entity.Account;
import com.denizcanbank.accounts.application.domain.valueObject.AccountNumber;
import com.denizcanbank.accounts.application.domain.valueObject.AccountType;
import com.denizcanbank.accounts.application.domain.valueObject.SecurityNumber;
import com.denizcanbank.accounts.dto.AccountUpdateRequestDto;
import org.springframework.stereotype.Component;

@Component
public class UpdateMapper implements EntityMapper<Account, AccountUpdateRequestDto> {

    @Override
    public Account toEntity(AccountUpdateRequestDto dto) {
        AccountNumber accountNumber = AccountNumber.of(dto.accountNumber());
        SecurityNumber securityNumber = SecurityNumber.of(dto.securityNumber());
        AccountType accountType = AccountType.valueOf(dto.accountType());

        return Account.builder()
                .accountNumber(accountNumber)
                .securityNumber(securityNumber)
                .accountType(accountType)
                .build();
    }
}
