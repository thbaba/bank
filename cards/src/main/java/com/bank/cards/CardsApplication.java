package com.bank.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories
public class CardsApplication {

    /*
    * TODO: Needs a refactor for repetitive works and
    * TODO: to clean package structure I need to consider separation of concerns.
    * */

    public static void main(String[] args) {
        SpringApplication.run(CardsApplication.class, args);
    }

}
