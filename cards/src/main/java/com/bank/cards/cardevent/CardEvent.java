package com.bank.cards.cardevent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("events")
public class CardEvent {
    @Id
    @Column("id")
    private Integer id;
    private String topic;
    private Integer key;
    private String payload;
    private String status;
}
