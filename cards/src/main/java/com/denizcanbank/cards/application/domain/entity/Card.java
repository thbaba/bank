package com.denizcanbank.cards.application.domain.entity;

import com.denizcanbank.cards.application.domain.exception.InvalidDepositException;
import com.denizcanbank.cards.application.domain.exception.InvalidLimitException;
import com.denizcanbank.cards.application.domain.exception.InvalidWithdrawException;
import com.denizcanbank.cards.application.domain.valueObject.Money;
import com.denizcanbank.cards.application.domain.valueObject.ID;


public class Card {

    private ID cardID;

    private ID accountID;

    private Money totalLimit;

    private Money amountUsed;
    
    private Money availableAmount;

    private Card(ID cardID, ID accountID, Money totalLimit, Money amountUsed) {
        this.cardID = cardID;
        this.accountID = accountID;
        this.totalLimit = totalLimit;
        this.amountUsed = amountUsed;
        this.availableAmount = totalLimit.decrease(amountUsed);
    }

    public void deposit(Money money) {
        if(money.amount() > this.amountUsed.amount()) {
            throw new InvalidDepositException("Can not deposit more than debt");
        }
        amountUsed = amountUsed.decrease(money);
        availableAmount = totalLimit.decrease(amountUsed);
    }
    
    public void withdraw(Money money) {
        if(money.amount() > availableAmount.amount()) {
            throw new InvalidWithdrawException("Can not withdraw more than available");
        }
        amountUsed = amountUsed.increase(money);
        availableAmount = totalLimit.decrease(amountUsed);
    }

    public void cardID(ID cardID) {
        this.cardID = cardID;
    }

    public void accountID(ID accountID) {
        this.accountID = accountID;
    }

    public void totalLimit(Money totalLimit) {
        if(totalLimit.amount() < amountUsed.amount()) {
            throw new InvalidLimitException("Can not set limit less than used amount");
        }
        this.totalLimit = totalLimit;
        availableAmount = totalLimit.decrease(amountUsed);
    }

    public ID cardID() {
        return cardID;
    }

    public ID accountID() {
        return accountID;
    }

    public Money totalLimit() {
        return totalLimit;
    }

    public Money amountUsed() {
        return amountUsed;
    }

    public Money availableAmount() {
        return availableAmount;
    }
    
    public static final class CardBuilder {
        private ID cardID;
        private ID accountID;
        private Money totalLimit;
        private Money amountUsed;

        private CardBuilder() {}

        public CardBuilder cardID(ID cardID) {
            this.cardID = cardID;
            return this;
        }

        public CardBuilder accountID(ID accountID) {
            this.accountID = accountID;
            return this;
        }

        public CardBuilder totalLimit(Money totalLimit) {
            this.totalLimit = totalLimit;
            return this;
        }

        public CardBuilder amountUsed(Money amountUsed) {
            this.amountUsed = amountUsed;
            return this;
        }

        public Card build() {
            if (amountUsed.amount() > totalLimit.amount()) {
                throw new InvalidWithdrawException("Amount used cannot be greater than total limit");
            }
            return new Card(cardID, accountID, totalLimit, amountUsed);
        }
    }

    public static CardBuilder builder() {
        return new CardBuilder();
    }
}
