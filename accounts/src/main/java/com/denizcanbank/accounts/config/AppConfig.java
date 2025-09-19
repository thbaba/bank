package com.denizcanbank.accounts.config;

import com.denizcanbank.accounts.application.domain.exception.*;
import com.denizcanbank.accounts.application.domain.service.AccountComparisonService;
import com.denizcanbank.accounts.application.domain.service.AccountConsistencyService;
import com.denizcanbank.accounts.dto.AccountRegistrationRequestDto;
import com.denizcanbank.accounts.dto.AccountResponseDto;
import com.denizcanbank.accounts.exception.GlobalExceptionHandler;
import com.denizcanbank.accounts.handler.Handler;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.*;

import java.time.Duration;


@OpenAPIDefinition(
        info = @Info(
                title = "Accounts Microservice API",
                version = "0.0.1",
                description = "Accounts Microservice in Bank Appilcation API Documentation"
        )
)
@Configuration
public class AppConfig {

    @RouterOperations({
            @RouterOperation(
                    path = "/api/account",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = "registerAccount",
                            summary = "Register a new account",
                            requestBody = @RequestBody(
                                    description = "Account details",
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = AccountRegistrationRequestDto.class
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "Account created successfully",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = AccountResponseDto.class
                                                    )
                                            )
                                    )
                            }
                    )
            )
    })
    @Bean
    public RouterFunction<ServerResponse> router(Handler handler, GlobalExceptionHandler exceptionHandler) {
        return RouterFunctions.nest(RequestPredicates.path("/api"),
                RouterFunctions.route()
                        .POST("", handler::registerHandler)
                        .PATCH("", handler::updateHandler)
                        .GET("/{securityNumber:[0-9]{11}}", handler::fetchBySecurityNumberHandler)
                        .GET("/{accountNumber:[0-9]{7}}", handler::fetchByAccountNumberHandler)
                        .DELETE("/{securityNumber:[0-9]{11}}", handler::deleteBySecurityNumberHandler)
                        .DELETE("/{accountNumber:[0-9]{7}}", handler::deleteByAccountNumberHandler)
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
    public WebClient cardWebClient(ReactorLoadBalancerExchangeFilterFunction f) {
        return WebClient.builder().filter(f).baseUrl("http://CARDS").build();
    }

    @Bean
    public AccountConsistencyService accountConsistencyService() {
        return new AccountConsistencyService();
    }

    @Bean
    public AccountComparisonService accountComparisonService() {
        return new AccountComparisonService();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> circuitBreakerFactoryCustomizer() {
        return fac -> fac.configureDefault(
                id -> new Resilience4JConfigBuilder(id)
                        .circuitBreakerConfig(
                                CircuitBreakerConfig.custom()
                                        .minimumNumberOfCalls(2)
                                        .slidingWindowSize(2)
                                        .waitDurationInOpenState(Duration.ofSeconds(5))
                                        .build()
                        )
                        .timeLimiterConfig(
                                TimeLimiterConfig.custom()
                                        .timeoutDuration(Duration.ofSeconds(10))
                                        .build()
                        )
                        .build()
        );
    }

    @Bean
    public ReactiveCircuitBreaker cardsCircuitBreaker(ReactiveResilience4JCircuitBreakerFactory factory) {
        return factory.create("cardsCircuitBreaker");
    }
}
