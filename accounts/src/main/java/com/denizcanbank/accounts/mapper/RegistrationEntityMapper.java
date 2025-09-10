package com.denizcanbank.accounts.mapper;

import com.denizcanbank.accounts.application.domain.entity.Account;
import com.denizcanbank.accounts.application.domain.valueObject.AccountType;
import com.denizcanbank.accounts.application.domain.valueObject.SecurityNumber;
import com.denizcanbank.accounts.dto.AccountRegistrationRequestDto;
import org.springframework.stereotype.Component;

@Component
public class RegistrationEntityMapper implements EntityMapper<Account, AccountRegistrationRequestDto> {

    public Account toEntity(AccountRegistrationRequestDto dto) {
        SecurityNumber securityNumber = SecurityNumber.of(dto.securityNumber());
        AccountType accountType = AccountType.valueOf(dto.accountType());

        return Account.builder()
                .securityNumber(securityNumber)
                .accountType(accountType)
                .build();
    }

}
