package com.denizcanbank.accounts.application.domain.service;

import com.denizcanbank.accounts.application.domain.entity.Account;

public class AccountComparisonService {

    public boolean equalsWithoutID(Account account1, Account account2) {
        return account1.securityNumber().equals(account2.securityNumber())
                && account1.accountType().equals(account2.accountType());
    }

}
