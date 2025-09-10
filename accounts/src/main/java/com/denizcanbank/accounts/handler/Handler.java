package com.denizcanbank.accounts.handler;

import com.denizcanbank.accounts.application.service.AccountService;
import com.denizcanbank.accounts.dto.*;
import com.denizcanbank.accounts.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class Handler {

    private final AccountService accountService;

    private final RegistrationEntityMapper registrationMapper;

    private final UpdateMapper updateMapper;

    private final AccountNumberEntityMapper accountNumberMapper;

    private final SecurityNumberEntityMapper securityNumberMapper;

    private final AccountResponseMapper accountResponseMapper;

    public Mono<ServerResponse> registerHandler(ServerRequest request) {
        return request.bodyToMono(AccountRegistrationRequestDto.class)
                .map(registrationMapper::toEntity)
                .flatMap(accountService::registerAccount)
                .map(accountResponseMapper::toDto)
                .flatMap(accountResponseDto ->
                        ServerResponse.created(URI.create(request.path() + "/" + accountResponseDto.accountNumber()))
                                .bodyValue(accountResponseDto));
    }

    public Mono<ServerResponse> updateHandler(ServerRequest request) {
        return request.bodyToMono(AccountUpdateRequestDto.class)
                .map(updateMapper::toEntity)
                .flatMap(accountService::updateAccount)
                .map(accountResponseMapper::toDto)
                .flatMap(accountResponseDto ->
                        ServerResponse.ok().bodyValue(accountResponseDto));
    }

    public Mono<ServerResponse> fetchByAccountNumberHandler(ServerRequest request) {
        return Mono.just(request.pathVariable("accountNumber"))
                .map(AccountNumberRequestDto::new)
                .map(accountNumberMapper::toEntity)
                .flatMap(accountService::fetchAccount)
                .map(accountResponseMapper::toDto)
                .flatMap(accountResponseDto ->
                        ServerResponse.ok().bodyValue(accountResponseDto));
    }

    public Mono<ServerResponse> fetchBySecurityNumberHandler(ServerRequest request) {
        return Mono.just(request.pathVariable("securityNumber"))
                .map(SecurityNumberRequestDto::new)
                .map(securityNumberMapper::toEntity)
                .flatMapMany(accountService::fetchAccounts)
                .map(accountResponseMapper::toDto)
                .map(accountResponseDto -> ServerSentEvent.<AccountResponseDto>builder()
                        .event("FetchBySecurityNumber")
                        .id(accountResponseDto.accountID())
                        .data(accountResponseDto).build())
                .as(flux -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(flux, ServerSentEvent.class));
    }

    public Mono<ServerResponse> deleteByAccountNumberHandler(ServerRequest request) {
        return Mono.just(request.pathVariable("accountNumber"))
                .map(AccountNumberRequestDto::new)
                .map(accountNumberMapper::toEntity)
                .flatMap(accountService::deleteAccount)
                .then(Mono.defer(() -> ServerResponse.ok().build()));
    }

    public Mono<ServerResponse> deleteBySecurityNumberHandler(ServerRequest request) {
        return Mono.just(request.pathVariable("securityNumber"))
                .map(SecurityNumberRequestDto::new)
                .map(securityNumberMapper::toEntity)
                .flatMap(accountService::deleteAccounts)
                .then(Mono.defer(() -> ServerResponse.ok().build()));
    }

}
