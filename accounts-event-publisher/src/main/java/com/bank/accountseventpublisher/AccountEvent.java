package com.bank.accountseventpublisher;

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
public class AccountEvent {
    @Id
    @Column("id")
    private Integer id;

    @Column("topic")
    private String topic;

    @Column("key")
    private Integer key;

    @Column("payload")
    private String payload;

    @Column("status")
    private String status;
}
