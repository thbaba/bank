package com.bank.cards.registercard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("accounts")
public class Account {

    /*
    * There is nothing about clean architecture anymore but its a demo right ?
    *
    * */

    @Id
    @Column("Id")
    private Integer id;

    @Column("security_number")
    private String securityNumber;

    @Column("total_limit")
    private Float totalLimit;

    public void setLimit(Float limit) {
        if (limit > 1000000)
            throw new InsufficientFundsException("Limit is greater than 1000000");
        else
            this.totalLimit = limit;
    }

    public void addLimit(Float limit) {
        setLimit(limit + this.totalLimit);
    }

}
