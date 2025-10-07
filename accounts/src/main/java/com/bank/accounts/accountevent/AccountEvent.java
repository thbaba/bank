package com.bank.accounts.accountevent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountEvent {
    private Integer id;
    private String topic;
    private Integer key;
    private String payload;
    private String status;
}
