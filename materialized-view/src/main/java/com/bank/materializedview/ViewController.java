package com.bank.materializedview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ViewController {

    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/card/{id}")
    public Mono<Card> getCard(@PathVariable Integer id) {
        return cardRepository.getCard(id);
    }

}
