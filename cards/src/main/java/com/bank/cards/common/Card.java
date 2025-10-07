package com.bank.cards.common;

import com.bank.cards.registercard.InsufficientFundsException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("cards")
public class Card {

    @Id
    @Column("Id")
    private Integer id;

    @Column("account_id")
    private Integer accountId;

    @Column("card_limit")
    private Float limit;

    @Column("balance")
    private Float balance;

    public void deposit(Float amount) {
        balance = balance + amount;
    }

    public void withdraw(Float amount) {
        Float nextBalance = balance - amount;

        if(nextBalance < -1 * limit) {
            throw new InsufficientFundsException("Insufficient Funds");
        } else {
            balance = nextBalance;
        }
    }

}
