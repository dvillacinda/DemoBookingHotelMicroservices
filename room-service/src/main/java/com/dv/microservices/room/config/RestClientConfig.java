package com.dv.microservices.room.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.dv.microservices.room.client.InformationClient;

@Configuration
public class RestClientConfig {
    @Value("${information.url}")
    private String informationUrl;

    @Bean
    public InformationClient roomClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(informationUrl)
                .build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(InformationClient.class);
    }

}
