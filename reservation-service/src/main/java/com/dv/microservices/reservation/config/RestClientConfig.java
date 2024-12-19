package com.dv.microservices.reservation.config;

import java.time.Duration;

import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.dv.microservices.reservation.client.RoomClient;

import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final ConfigProperties configProperties;
    private final ObservationRegistry observationRegistry; 

    @Bean
    public RoomClient roomClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(configProperties.getRoom().getUrl())
                .requestFactory(getClientHttpRequestFactory())
                .observationRegistry(observationRegistry)
                .build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(RoomClient.class);
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        var clientHttpRequestFactory = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(3))
                .withReadTimeout(Duration.ofSeconds(3));
        return ClientHttpRequestFactories.get(clientHttpRequestFactory);
    }
}