package com.dv.booking.gateway.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> informationService() {
        return GatewayRouterFunctions.route("information_service")
                .route(
                        RequestPredicates.path("/api/info/**"),
                        HandlerFunctions.http("http://localhost:8084"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> reservationService() {
        return GatewayRouterFunctions.route("reservation_service")
                .route(
                        RequestPredicates.path("/api/reservation/**"),
                        HandlerFunctions.http("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> roomService() {
        return GatewayRouterFunctions.route("room_service")
                .route(
                        RequestPredicates.path("/api/room/**"),
                        HandlerFunctions.http("http://localhost:8082"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userService() {
        return GatewayRouterFunctions.route("user_service")
                .route(
                        RequestPredicates.path("/api/user/**"),
                        HandlerFunctions.http("http://localhost:8080"))
                .build();
    }

}
