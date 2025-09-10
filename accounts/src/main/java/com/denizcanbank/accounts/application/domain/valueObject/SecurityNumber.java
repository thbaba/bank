package com.denizcanbank.accounts.application.domain.valueObject;

import com.denizcanbank.accounts.application.domain.exception.InvalidSecurityNumberException;

import java.util.Objects;

public class SecurityNumber {

    private final String securityNumber;

    private SecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }

    public static SecurityNumber of(String securityNumber) {
        if(!validate(securityNumber)) {
            throw new InvalidSecurityNumberException("Security number length should be 11 digit: " + securityNumber);
        }
        return new SecurityNumber(securityNumber);
    }

    private static boolean validate(String securityNumber) {
        return Objects.nonNull(securityNumber) && securityNumber.length() == 11;
    }

    @Override
    public String toString() {
        return securityNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityNumber that = (SecurityNumber) o;
        return securityNumber.equals(that.securityNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(securityNumber);
    }

}
