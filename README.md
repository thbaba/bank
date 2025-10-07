# Bank

Exploration repository for Webflux and Microservices.

#### `Webflux` and `Project Reactor`
```
Flux.just("Project Reactor is a beast!", "I am learning...")
    .delayElements(Duration.ofSeconds(1))
        .subscribe(System.out::println);
```

#### Spring `Config Server` with `Github Configuration Source`
- It creates coupling.

#### Client side `Service Discovery` with `Eureka`
- Removed
- TODO: Try to use Service Side Service Discovery with Kubernetes

#### `Transactional Outbox Pattern` with Kafka
- Native implementation for Outbox Pattern
- Reactive chain is used
- Account produce events and that just AccountCreated event, so it's idempotent naturally. No need to check if that event consumed in cards microservices
- TODO: Debezium can ve used to read from WAL and in PostgreSQL there is a function to write to WAL so no need to insert or delete to/from a table
- TODO: Add test

#### `Dead Letter Queue` added
- `Dead Letter Queue` added to cards for events that notice "The Account Is Created"
- If cards can not write to `DQL` so it crashes without commiting

#### `Spring Cloud Gateway`
- TODO: Try to use nginx with kubernetes

#### `Circuit Limiter`, `Retry` and `RateLimiter` with `Resilience4J`
- TODO: Add RateLimiter with Bucket4J
