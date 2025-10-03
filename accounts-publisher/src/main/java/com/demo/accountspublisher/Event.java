package com.demo.accountspublisher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @Column("event_id")
    private UUID id;

    @Column("event_name")
    private String eventName;

    @Column("aggregate_id")
    private UUID aggregateId;

    @Column("payload")
    private String payload;
}
