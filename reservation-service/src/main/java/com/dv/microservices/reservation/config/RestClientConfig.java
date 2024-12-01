package com.dv.microservices.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.dv.microservices.reservation.client.RoomClient;

@Configuration
public class RestClientConfig {

    private final ConfigProperties configProperties;

    public RestClientConfig(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Bean
    public RoomClient roomClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(configProperties.getRoom().getUrl())
                .build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(RoomClient.class);
    }

}
