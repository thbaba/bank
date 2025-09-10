package com.denizcanbank.accounts.application.domain.service;

import com.denizcanbank.accounts.application.domain.entity.Account;
import com.denizcanbank.accounts.application.domain.exception.InvalidAccountInformationException;

public class AccountConsistencyService {

    public void checkConsistency(Account account1,  Account account2) throws InvalidAccountInformationException{
        if(!account1.accountNumber().equals(account2.accountNumber())){
            throw new InvalidAccountInformationException("Account numbers do not match");
        } else if(!account1.securityNumber().equals(account2.securityNumber())){
            throw new InvalidAccountInformationException("Security numbers do not match");
        }
    }

}
