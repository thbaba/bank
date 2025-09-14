package com.denizcanbank.cards.config;

import com.denizcanbank.cards.application.domain.exception.*;
import com.denizcanbank.cards.exception.GlobalExceptionHandler;
import com.denizcanbank.cards.handler.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AppConfig {



    @Bean
    public RouterFunction<ServerResponse> router(Handler handler, GlobalExceptionHandler exceptionHandler) {
        return RouterFunctions.nest(RequestPredicates.path("/api"),
                RouterFunctions.route()
                        .POST("card", handler::createHandler)
                        .GET("card/{cardID}", handler::readHandler)
                        .PUT("card",  handler::updateHandler)
                        .DELETE("card/{cardID}", handler::deleteHandler)
                        .filter((req, next) -> next.handle(req)
                                .onErrorResume(CardAlreadyCreatedException.class,  exceptionHandler::cardAlreadyCreatedExceptionHandler)
                                .onErrorResume(CardNotFoundException.class,  exceptionHandler::cardNotFoundExceptionHandler)
                                .onErrorResume(InvalidDepositException.class,  exceptionHandler::invalidDepositExceptionHandler)
                                .onErrorResume(InvalidIDException.class,  exceptionHandler::invalidIDExceptionHandler)
                                .onErrorResume(InvalidLimitException.class, exceptionHandler::invalidLimitExceptionHandler)
                                .onErrorResume(InvalidWithdrawException.class, exceptionHandler::invalidWithdrawExceptionHandler)
                                .onErrorResume(NegativeAmountException.class,  exceptionHandler::negativeAmountExceptionHandler)
                                .onErrorResume(DataAccessException.class, exceptionHandler::dataAccessExceptionHandler)
                                .onErrorResume(Exception.class, exceptionHandler::genericExceptionHandler)
                        )
                        .build()
        );
    }

}
