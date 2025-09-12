package com.denizcanbank.cards.application.domain.valueObject;

import com.denizcanbank.cards.application.domain.exception.NegativeAmountException;

public class Money {

    private float amount;

    private Money(float amount) {
        this.amount = amount;
    }

    public static Money of(float amount) {
        if(amount < 0) {
            throw new NegativeAmountException("Amount can not be negative");
        }
        return new Money(amount);
    }

    public float amount() {
        return amount;
    }

    public Money increase(Money money) {
        return Money.of(money.amount() + this.amount());
    }

    public Money decrease(Money money) {
        return Money.of(this.amount() - money.amount());
    }

}
