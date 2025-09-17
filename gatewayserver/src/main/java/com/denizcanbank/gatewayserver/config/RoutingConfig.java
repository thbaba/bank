package com.denizcanbank.gatewayserver.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Configuration
public class RoutingConfig {

    @Bean
    public RouteLocator accountRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/api/account/**")
                        .or()
                        .path("/api/account")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE)
                        .filters(
                                gatewayRewritePathFilterFactory().apply("account")
                                        .andThen(gatewayAddRequestHeadersFilter())
                        )
                        .uri("lb://ACCOUNTS")
                )
                .build();
    }

    @Bean
    public RouteLocator cardRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/api/card")
                        .or()
                        .path("/api/card/**")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE)
                        .filters(gatewayRewritePathFilterFactory().apply("card")
                                .andThen(gatewayAddRequestHeadersFilter())
                        )
                        .uri("lb://CARDS")
                )
                .build();
    }

    @Bean
    public Function<GatewayFilterSpec, GatewayFilterSpec> gatewayAddRequestHeadersFilter() {
        return f -> f.addRequestHeadersIfNotPresent(
                String.format("denizcanbank-correlation-id:%s", UUID.randomUUID().toString())
        );
    }

    @Bean
    public Function<String, Function<GatewayFilterSpec, GatewayFilterSpec>> gatewayRewritePathFilterFactory() {
        return endPoint -> (f -> f.rewritePath(
                String.format("/api/%s(?<separator>/?)(?<pathVariable>.*)", endPoint),
                "/api${separator}${pathVariable}"
        ));
    }


    @Bean
    public GlobalFilter responseTraceFilter() {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    String id = exchange.getRequest().getHeaders().getFirst("denizcanbank-correlation-id");
                    exchange.getResponse().getHeaders().add("denizcanbank-correlation-id", id);
                }));
    }

}
