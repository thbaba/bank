package com.bank.cards.registeraccount;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EventReader {

    private final ReactiveKafkaListener kafkaListener;

    private final INewAccountRepository accountRepository;

    private final ObjectMapper objectMapper;

    public Mono<Void> read() {
        return kafkaListener.receive()
                .flatMap(this::process)
                .then(kafkaListener.commit());
    }

    private Mono<Void> process(ConsumerRecord<Integer, String> record) {
        try {
            NewAccount newAccount = objectMapper.readValue(record.value(), NewAccount.class);
            return accountRepository.exists(newAccount.id()).flatMap(exists -> {
                if (!exists) {
                    return accountRepository.register(newAccount.id(), newAccount.securityNumber());
                }else {
                    return Mono.empty();
                }
            }).onErrorMap(ex -> new NewAccountEventProcessingException("Account#" + newAccount.id() + " | " + ex.getMessage()));
        } catch (JsonProcessingException e) {
            throw new NewAccountEventProcessingException(e.getMessage());
        }
    }

}
