package com.dv.microservices.reservation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "services")
@Getter
@Setter
public class ConfigProperties {
    private String roomUrl; 
}
