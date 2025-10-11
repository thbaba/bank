package com.bank.materializedview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    private Integer cardId;
    private Integer accountId;
    private Float limit;
    private Float balance;
    private LocalDateTime eventCreationTime;

}
