package com.denizcanbank.cards.handler;

import com.denizcanbank.cards.application.domain.usecase.CardCrudUseCase;
import com.denizcanbank.cards.application.domain.usecase.ReadCardByAccountIDUseCase;
import com.denizcanbank.cards.dto.CardResponseDto;
import com.denizcanbank.cards.dto.CreateCardRequestDto;
import com.denizcanbank.cards.dto.UpdateCardRequestDto;
import com.denizcanbank.cards.mapper.CardIDMapper;
import com.denizcanbank.cards.mapper.CreateCardMapper;
import com.denizcanbank.cards.mapper.UpdateCardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class Handler {

    private final CardCrudUseCase service;

    private final ReadCardByAccountIDUseCase accountIDService;

    private final CreateCardMapper createMapper;

    private final UpdateCardMapper updateMapper;

    private final CardIDMapper cardIDMapper;

    public Mono<ServerResponse> createHandler(ServerRequest request) {
        return request.bodyToMono(CreateCardRequestDto.class)
                .map(createMapper::toEntity)
                .flatMap(service::create)
                .map(createMapper::toDto)
                .flatMap(r -> ServerResponse.created(
                        URI.create(request.path() + "/" + r.cardID())
                ).bodyValue(r));
    }

    public Mono<ServerResponse> readHandler(ServerRequest request) {
        return Mono.just(request.pathVariable("cardID"))
                .flatMap(service::read)
                .map(cardIDMapper::toDto)
                .flatMap(r -> ServerResponse.ok().bodyValue(r));
    }

    public Mono<ServerResponse> updateHandler(ServerRequest request) {
        return request.bodyToMono(UpdateCardRequestDto.class)
                .map(updateMapper::toEntity)
                .flatMap(service::update)
                .map(updateMapper::toDto)
                .flatMap(r -> ServerResponse.ok().bodyValue(r));
    }

    public Mono<ServerResponse> deleteHandler(ServerRequest request) {
        return Mono.just(request.pathVariable("cardID"))
                .flatMap(service::delete)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> readByAccountIDHandler(ServerRequest request) {
        return Mono.just(request.pathVariable("accountID"))
                .delayElement(Duration.ofSeconds(5))
                .flatMapMany(accountIDService::readByAccountID)
                .map(cardIDMapper::toDto)
                .map(dto -> ServerSentEvent.builder(dto).event("ReadAccountsCards").id(dto.cardID()).build())
                .as(flux -> ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(flux, ServerSentEvent.class));
    }

}
