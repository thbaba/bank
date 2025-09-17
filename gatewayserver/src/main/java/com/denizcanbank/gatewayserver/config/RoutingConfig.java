package com.denizcanbank.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {

    @Bean
    public RouteLocator accountRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/api/account")
                        .and().method("POST", "PATCH")
                        .filters(f -> f.rewritePath("/api/account", "/api"))
                        .uri("lb://ACCOUNTS")
                )
                .route(p -> p.path("/api/account/**")
                        .and().method("GET", "DELETE")
                        .filters(f -> f.rewritePath("/api/account/(?<pathVariable>.*)", "/api/${pathVariable}"))
                        .uri("lb://ACCOUNTS")
                )
                .build();
    }

    @Bean
    public RouteLocator cardRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/api/card")
                        .and().method("POST", "PUT")
                        .filters(f -> f.rewritePath("/api/card", "/api"))
                        .uri("lb://CARDS")
                )
                .route(p -> p.path("/api/card/**")
                        .and()
                        .method("GET", "DELETE")
                        .filters(f->f.rewritePath("/api/card/(?<pathVar>.*)", "/api/${pathVar}"))
                        .uri("lb://CARDS")
                )
                .build();
    }

}
