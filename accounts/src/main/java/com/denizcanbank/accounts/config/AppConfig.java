package com.denizcanbank.accounts.config;

import com.denizcanbank.accounts.application.domain.exception.*;
import com.denizcanbank.accounts.application.domain.service.AccountComparisonService;
import com.denizcanbank.accounts.application.domain.service.AccountConsistencyService;
import com.denizcanbank.accounts.exception.GlobalExceptionHandler;
import com.denizcanbank.accounts.handler.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class AppConfig {

    @Bean
    public RouterFunction<ServerResponse> router(Handler handler, GlobalExceptionHandler exceptionHandler) {
        return RouterFunctions.nest(RequestPredicates.path("/api"),
                RouterFunctions.route()
                        .POST("/account", handler::registerHandler)
                        .PATCH("/account", handler::updateHandler)
                        .GET("/account/{securityNumber:[0-9]{11}}", handler::fetchBySecurityNumberHandler)
                        .GET("/account/{accountNumber:[0-9]{7}}", handler::fetchByAccountNumberHandler)
                        .DELETE("/account/{securityNumber:[0-9]{11}}", handler::deleteBySecurityNumberHandler)
                        .DELETE("/account/{accountNumber:[0-9]{7}}", handler::deleteByAccountNumberHandler)
                        .filter((request, next) -> next.handle(request)
                                .onErrorResume(AccountAlreadyRegisteredException.class, exceptionHandler::accountAlreadyRegisteredExceptionHandler)
                                .onErrorResume(InternalErrorException.class, exceptionHandler::internalErrorExceptionHandler)
                                .onErrorResume(InvalidAccountInformationException.class, exceptionHandler::invalidAccountInformationException)
                                .onErrorResume(InvalidAccountNumberException.class, exceptionHandler::invalidAccountNumberException)
                                .onErrorResume(InvalidIDException.class, exceptionHandler::invalidIDException)
                                .onErrorResume(InvalidSecurityNumberException.class, exceptionHandler::invalidSecurityNumberException)
                                .onErrorResume(NoSuchAccountException.class, exceptionHandler::noSuchAccountExceptionHandler)
                                .onErrorResume(Exception.class, exceptionHandler::globalExceptionHandler)
                        )
                        .build()
        );
    }

    @Bean
    public AccountConsistencyService accountConsistencyService() {
        return new AccountConsistencyService();
    }

    @Bean
    public AccountComparisonService accountComparisonService() {
        return new AccountComparisonService();
    }

}
