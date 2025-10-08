package com.bank.cardseventpublisher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("events")
public class Event {
    private Integer id;
    private String name;
    private String topic;
    private Integer key;
    private String payload;
    private String status;
    private LocalDateTime created_at;
}