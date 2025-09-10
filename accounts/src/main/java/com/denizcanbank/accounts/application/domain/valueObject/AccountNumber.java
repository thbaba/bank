package com.denizcanbank.accounts.application.domain.valueObject;

import com.denizcanbank.accounts.application.domain.exception.InvalidAccountNumberException;

import java.util.Objects;

public class AccountNumber {

    private final String accountNumber;

    private AccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public static AccountNumber of(String accountNumber) throws InvalidAccountNumberException {
        if(!validate(accountNumber))
            throw new InvalidAccountNumberException("Account number length should be 7 digits");
        return new AccountNumber(accountNumber);
    }

    private static boolean validate(String accountNumber) {
        return Objects.nonNull(accountNumber) && accountNumber.length() == 7;
    }

    @Override
    public String toString() {
        return accountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountNumber that = (AccountNumber) o;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

}
