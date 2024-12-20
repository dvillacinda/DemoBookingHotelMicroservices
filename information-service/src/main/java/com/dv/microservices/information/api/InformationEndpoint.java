package com.dv.microservices.information.api;

import java.util.List;

import com.dv.microservices.information.model.Information;
import com.dv.microservices.information.service.InformationService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;

import lombok.RequiredArgsConstructor;

@Endpoint
@RequiredArgsConstructor
@AnonymousAllowed
public class InformationEndpoint {
    private final InformationService informationService;

    public List<Information> getAll(){
        return informationService.getAllInformations();
    }

}
