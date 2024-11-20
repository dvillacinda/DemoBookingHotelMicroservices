package com.dv.microservices.information.dto;

import jakarta.validation.constraints.NotBlank;

public record PhotoRequest(
    @NotBlank(message = "url can't be blank")
    String url 
) {

}
