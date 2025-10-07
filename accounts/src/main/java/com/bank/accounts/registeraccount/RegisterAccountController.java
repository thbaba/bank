package com.bank.accounts.registeraccount;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegisterAccountController {

    private final IRegisterAccountUseCase service;

    @PostMapping("/account")
    public Mono<ResponseEntity<RegisterAccountResponseDto>> registerAccount(@Valid @RequestBody RegisterAccountRequestDto request) {
        return service.registerAccount(request.securityNumber())
                .map(account -> String.format("Account %d created successfully!", account.getId()))
                .map(RegisterAccountResponseDto::new)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.CREATED))
                .onErrorResume(Exception.class, ex -> Mono.just(ex.getMessage())
                        .map(RegisterAccountResponseDto::new)
                        .map(dto -> new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST))
                );
    }

}
