package com.denizcanbank.accounts.application.domain.entity;

import com.denizcanbank.accounts.application.domain.valueObject.AccountNumber;
import com.denizcanbank.accounts.application.domain.valueObject.AccountType;
import com.denizcanbank.accounts.application.domain.valueObject.ID;
import com.denizcanbank.accounts.application.domain.valueObject.SecurityNumber;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode
public class Account {

    private ID accountID;

    private SecurityNumber securityNumber;

    private AccountNumber accountNumber;

    private AccountType accountType;

    public ID id() {
        return accountID;
    }

    public SecurityNumber securityNumber() {
        return securityNumber;
    }

    public AccountNumber accountNumber() {
        return accountNumber;
    }

    public AccountType accountType() {
        return accountType;
    }

    public void id(ID accountID) {
        this.accountID = accountID;
    }

    public void securityNumber(SecurityNumber securityNumber) {
        this.securityNumber = securityNumber;
    }

    public void accountNumber(AccountNumber accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void accountType(AccountType accountType) {
        this.accountType = accountType;
    }

}
