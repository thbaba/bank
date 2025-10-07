package com.bank.accounts.registeraccount;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RegisterAccountRepository implements IRegisterAccountRepository {

    private final DatabaseClient dbClient;

    @Override
    public Mono<Integer> registerAccount(String securityNumber) {
        String query = "INSERT INTO accounts (security_number, balance) VALUES (:securityNumber, :balance) RETURNING id";

        return dbClient.sql(query)
                .bind("securityNumber", securityNumber)
                .bind("balance", 0.0)
                .map((row, _) -> row.get("id", Integer.class))
                .one();
    }

    @Override
    public Mono<Boolean> isAccountExists(String securityNumber) {
        String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE security_number = :securityNumber)";

        return dbClient.sql(query)
                .bind("securityNumber", securityNumber)
                .map((row, _) -> row.get(0, Boolean.class))
                .one();
    }
}
