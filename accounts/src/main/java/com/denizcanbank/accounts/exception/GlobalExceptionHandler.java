package com.denizcanbank.accounts.exception;

import com.denizcanbank.accounts.application.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class GlobalExceptionHandler {

    public Mono<ServerResponse> accountAlreadyRegisteredExceptionHandler(
            AccountAlreadyRegisteredException ex
    ) {
        return ServerResponse.badRequest().bodyValue(
                new ServerErrorResponseDto("Account Already Registred", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> noSuchAccountExceptionHandler(NoSuchAccountException ex) {
        return ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(
                new ServerErrorResponseDto("Account Not Found", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> internalErrorExceptionHandler(InternalErrorException ex) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(
                new ServerErrorResponseDto("Internal Error", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> invalidAccountInformationException(InvalidAccountInformationException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(
                new ServerErrorResponseDto("Invalid Account Information", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> invalidAccountNumberException(InvalidAccountNumberException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(
                new ServerErrorResponseDto("Invalid Account Number", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> invalidIDException(InvalidIDException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(
                new ServerErrorResponseDto("Invalid Account ID", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> invalidSecurityNumberException(InvalidSecurityNumberException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(
                new ServerErrorResponseDto("Invalid Account Security Number", ex.getMessage(), LocalDateTime.now())
        );
    }

    public Mono<ServerResponse> globalExceptionHandler(Exception ex) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(
                new ServerErrorResponseDto("Internal Error", "Something goes wrong... " + ex.getMessage(), LocalDateTime.now())
        );
    }

}
