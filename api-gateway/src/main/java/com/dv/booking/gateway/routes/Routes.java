package com.dv.booking.gateway.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

import java.net.URI;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;

@Configuration
public class Routes {
        // Services URL

        @Bean
        public RouterFunction<ServerResponse> informationService() {
                return GatewayRouterFunctions.route("information_service")
                                .route(
                                                RequestPredicates.path("/api/info/**"),
                                                HandlerFunctions.http("http://localhost:8084"))
                                .filter(CircuitBreakerFilterFunctions
                                                .circuitBreaker("informationServiceCircuitBreaker",
                                                                URI.create("forward:/fallbackRoute")))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> reservationService() {
                return GatewayRouterFunctions.route("reservation_service")
                                .route(
                                                RequestPredicates.path("/api/reservation/**"),
                                                HandlerFunctions.http("http://localhost:8081"))
                                .filter(CircuitBreakerFilterFunctions
                                                .circuitBreaker("reservationServiceCircuitBreaker",
                                                                URI.create("forward:/fallbackRoute")))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> roomService() {
                return GatewayRouterFunctions.route("room_service")
                                .route(
                                                RequestPredicates.path("/api/room/**"),
                                                HandlerFunctions.http("http://localhost:8082"))
                                .filter(CircuitBreakerFilterFunctions
                                                .circuitBreaker("roomServiceCircuitBreaker",
                                                                URI.create("forward:/fallbackRoute")))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> userService() {
                return GatewayRouterFunctions.route("user_service")
                                .route(
                                                RequestPredicates.path("/api/user/**"),
                                                HandlerFunctions.http("http://localhost:8080"))
                                .filter(CircuitBreakerFilterFunctions
                                                .circuitBreaker("userServiceCircuitBreaker",
                                                                URI.create("forward:/fallbackRoute")))
                                .build();
        }

        // Swagger URL

        @Bean
        public RouterFunction<ServerResponse> informationServiceSwaggerRoute() {
                return GatewayRouterFunctions.route("information_service_swagger")
                                .route(
                                                RequestPredicates.path("/aggregate/information-service/v3/api-doc"),
                                                HandlerFunctions.http("http://localhost:8084"))
                                .filter(CircuitBreakerFilterFunctions
                                                .circuitBreaker("informationServiceSwaggerCircuitBreaker",
                                                                URI.create("forward:/fallbackRoute")))
                                .filter(setPath("/api-docs"))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> reservationServiceSwaggerRoute() {
                return GatewayRouterFunctions.route("reservation_service_swagger")
                                .route(
                                                RequestPredicates.path("/aggregate/reservation-service/v3/api-doc"),
                                                HandlerFunctions.http("http://localhost:8081"))
                                .filter(CircuitBreakerFilterFunctions
                                                .circuitBreaker("reservationServiceSwaggerCircuitBreaker",
                                                                URI.create("forward:/fallbackRoute")))
                                .filter(setPath("/api-docs"))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> roomServiceSwaggerRoute() {
                return GatewayRouterFunctions.route("room_service_swagger")
                                .route(
                                                RequestPredicates.path("/aggregate/room-service/v3/api-doc"),
                                                HandlerFunctions.http("http://localhost:8082"))
                                .filter(CircuitBreakerFilterFunctions
                                                .circuitBreaker("roomServiceSwaggerCircuitBreaker",
                                                                URI.create("forward:/fallbackRoute")))
                                .filter(setPath("/api-docs"))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> userServiceSwaggerRoute() {
                return GatewayRouterFunctions.route("user_service_swagger")
                                .route(
                                                RequestPredicates.path("/aggregate/user-service/v3/api-doc"),
                                                HandlerFunctions.http("http://localhost:8080"))
                                .filter(CircuitBreakerFilterFunctions
                                                .circuitBreaker("userServiceSwaggerCircuitBreaker",
                                                                URI.create("forward:/fallbackRoute")))
                                .filter(setPath("/api-docs"))
                                .build();
        }

        //Fallback 

        @Bean
        public RouterFunction<ServerResponse> fallbackRoute() {
                return route("fallbackRoute")
                                .GET("/fallbackRoute",
                                                request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                                                .body("Service Unavailable, place try again later"))
                                .build();
        }

}
