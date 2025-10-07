package com.bank.cards.changebalance;

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
public class ChangeBalanceController {

    private final ChangeBalanceService changeBalanceService;

    @PostMapping("/deposit")
    public Mono<ResponseEntity<ChangeBalanceResponseDto>> deposit(@Valid @RequestBody ChangeBalanceRequestDto dto) {
        return changeBalanceService.deposit(dto)
                .thenReturn(ResponseEntity.ok().body(new ChangeBalanceResponseDto("Success!!")))
                .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().body(new ChangeBalanceResponseDto(ex.getMessage()))));
    }

    @PostMapping("/withdraw")
    public Mono<ResponseEntity<ChangeBalanceResponseDto>> withdraw(@Valid @RequestBody ChangeBalanceRequestDto dto) {
        return changeBalanceService.withdraw(dto)
                .thenReturn(ResponseEntity.ok().body(new ChangeBalanceResponseDto("Success!!")))
                .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().body(new ChangeBalanceResponseDto(ex.getMessage()))));
    }

}
