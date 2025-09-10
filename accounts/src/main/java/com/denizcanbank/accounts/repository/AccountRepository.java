package com.denizcanbank.accounts.repository;

import com.denizcanbank.accounts.application.domain.entity.Account;
import com.denizcanbank.accounts.application.domain.repository.IAccountRepository;
import com.denizcanbank.accounts.application.domain.valueObject.AccountNumber;
import com.denizcanbank.accounts.application.domain.valueObject.AccountType;
import com.denizcanbank.accounts.application.domain.valueObject.ID;
import com.denizcanbank.accounts.application.domain.valueObject.SecurityNumber;
import com.denizcanbank.accounts.config.SqlQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountRepository implements IAccountRepository {

    private final DatabaseClient client;

    private final SqlQueries sqlQueries;

    @Override
    public Mono<Account> registerAccount(Account account) {
        return client.sql(sqlQueries.insert)
                .bind("securityNumber", account.securityNumber().toString())
                .bind("accountType", account.accountType().toString())
                .map((row, meta) -> Account.builder()
                        .accountID(ID.of(row.get("account_id", UUID.class)))
                        .accountType(AccountType.valueOf(row.get("account_type", String.class)))
                        .accountNumber(AccountNumber.of(row.get("account_number", String.class)))
                        .securityNumber(SecurityNumber.of(row.get("security_number", String.class)))
                        .build())
                .one();
    }

    @Override
    public Mono<Account> updateAccount(Account account) {
        return client.sql(sqlQueries.update)
                .bind("accountType", account.accountType().toString())
                .bind("accountNumber", account.accountNumber().toString())
                .map((row, meta) -> Account.builder()
                        .accountID(ID.of(row.get("account_id", UUID.class)))
                        .accountType(AccountType.valueOf(row.get("account_type", String.class)))
                        .accountNumber(AccountNumber.of(row.get("account_number", String.class)))
                        .securityNumber(SecurityNumber.of(row.get("security_number", String.class)))
                        .build())
                .one();
    }

    @Override
    public Mono<Account> fetchAccount(AccountNumber accountNumber) {
        return client.sql(sqlQueries.selectAccountNumber)
                .bind("accountNumber", accountNumber.toString())
                .map((row, meta) -> Account.builder()
                        .accountID(ID.of(row.get("account_id", UUID.class)))
                        .accountType(AccountType.valueOf(row.get("account_type", String.class)))
                        .accountNumber(AccountNumber.of(row.get("account_number", String.class)))
                        .securityNumber(SecurityNumber.of(row.get("security_number", String.class)))
                        .build())
                .one();
    }

    @Override
    public Flux<Account> fetchAccounts(SecurityNumber securityNumber) {
        return client.sql(sqlQueries.selectSecurityNumber)
                .bind("securityNumber", securityNumber.toString())
                .map((row, meta) -> Account.builder()
                        .accountID(ID.of(row.get("account_id", UUID.class)))
                        .accountType(AccountType.valueOf(row.get("account_type", String.class)))
                        .accountNumber(AccountNumber.of(row.get("account_number", String.class)))
                        .securityNumber(SecurityNumber.of(row.get("security_number", String.class)))
                        .build())
                .all();
    }

    @Override
    public Mono<Void> deleteAccount(AccountNumber accountNumber) {
        return client.sql(sqlQueries.deleteAccountNumber)
                .bind("accountNumber", accountNumber.toString())
                .then();
    }

    @Override
    public Mono<Void> deleteAccounts(SecurityNumber securityNumber) {
        return client.sql(sqlQueries.deleteSecurityNumber)
                .bind("securityNumber", securityNumber.toString())
                .then();
    }
}
