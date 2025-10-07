package com.bank.cards.registercard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class RegisterCardController {

    private final RegisterCardService registerCardService;

    @PostMapping
    public Mono<ResponseEntity<RegisterCardResponseDto>> register(@Valid @RequestBody RegisterCardRequestDto request) {
        return registerCardService.registerCard(request.securityNumber(), request.limit())
                .then(Mono.just(ResponseEntity.ok(new RegisterCardResponseDto("Successfully created!"))))
                .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().body(new RegisterCardResponseDto(ex.getMessage()))));
    }

}
