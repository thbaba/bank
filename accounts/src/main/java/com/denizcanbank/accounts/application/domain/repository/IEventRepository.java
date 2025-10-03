package com.denizcanbank.accounts.application.domain.repository;

import com.denizcanbank.accounts.application.domain.entity.Account;
import com.denizcanbank.accounts.application.domain.entity.AccountEvent;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IEventRepository {

    Mono<Void> saveAccountRegistrationEvent(AccountEvent account);

}
