package com.denizcanbank.cards.exception;

import com.denizcanbank.cards.dto.ErrorResponseDto;
import com.denizcanbank.cards.application.domain.exception.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class GlobalExceptionHandler {
    
    public Mono<ServerResponse> cardAlreadyCreatedExceptionHandler(CardAlreadyCreatedException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(
                new ErrorResponseDto("Card Already Created", ex.getMessage(), LocalDateTime.now())
        );
    }
    
    public Mono<ServerResponse> cardNotFoundExceptionHandler(CardNotFoundException ex) {
        return ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(
                new ErrorResponseDto("Card Not Found", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> invalidDepositExceptionHandler(InvalidDepositException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(
                new ErrorResponseDto("Invalid Deposit", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> invalidIDExceptionHandler(InvalidIDException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(
                new ErrorResponseDto("Invalid ID", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> invalidLimitExceptionHandler(InvalidLimitException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(
                new ErrorResponseDto("Invalid Limit", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> dataAccessExceptionHandler(DataAccessException ex) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(
                new ErrorResponseDto("Database Error", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> invalidWithdrawExceptionHandler(InvalidWithdrawException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(
                new ErrorResponseDto("Invalid Withdraw", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> negativeAmountExceptionHandler(NegativeAmountException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(
                new ErrorResponseDto("Negative Amount", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> genericExceptionHandler(Exception ex) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(
                new ErrorResponseDto("Internal Server Error", ex.getMessage(), LocalDateTime.now())
        );
    }
}
