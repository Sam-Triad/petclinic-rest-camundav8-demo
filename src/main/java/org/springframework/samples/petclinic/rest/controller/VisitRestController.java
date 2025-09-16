/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.rest.api.VisitsApi;
import org.springframework.samples.petclinic.rest.dto.VisitDto;
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.worker.VisitVariables;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import io.camunda.zeebe.client.ZeebeClient;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vitaliy Fedoriv
 */

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api")
public class VisitRestController implements VisitsApi {

    private final ClinicService clinicService;

    private final VisitMapper visitMapper;

    private final ZeebeClient zeebeClient;

    public VisitRestController(ClinicService clinicService, VisitMapper visitMapper, ZeebeClient zeebeClient) {
        this.clinicService = clinicService;
        this.visitMapper = visitMapper;
        this.zeebeClient = zeebeClient;
    }


    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<List<VisitDto>> listVisits() {
        List<Visit> visits = new ArrayList<>(this.clinicService.findAllVisits());
        if (visits.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ArrayList<>(visitMapper.toVisitsDto(visits)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<VisitDto> getVisit( Integer visitId) {
        Visit visit = this.clinicService.findVisitById(visitId);
        if (visit == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(visitMapper.toVisitDto(visit), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<VisitDto> addVisit(VisitDto visitDto) {
        HttpHeaders headers = new HttpHeaders();
        Visit visit = visitMapper.toVisit(visitDto);
        this.clinicService.saveVisit(visit);

        var zeebeVariables = new VisitVariables(visit.getId(), null, null, null);
        zeebeClient.newCreateInstanceCommand()
            .bpmnProcessId("visit_booking")
            .latestVersion()
            .variables(zeebeVariables)
            .send();

        var updatedVisitDto = visitMapper.toVisitDto(visit);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/visits/{id}").buildAndExpand(visit.getId()).toUri());
        return new ResponseEntity<>(updatedVisitDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<VisitDto> updateVisit(Integer visitId, VisitFieldsDto visitFieldsDto) {
        Visit currentVisit = this.clinicService.findVisitById(visitId);
        if (currentVisit == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentVisit.setDate(visitFieldsDto.getDate());
        currentVisit.setDescription(visitFieldsDto.getDescription());
        this.clinicService.saveVisit(currentVisit);
        return new ResponseEntity<>(visitMapper.toVisitDto(currentVisit), HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Transactional
    @Override
    public ResponseEntity<VisitDto> deleteVisit(Integer visitId) {
        Visit visit = this.clinicService.findVisitById(visitId);
        if (visit == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deleteVisit(visit);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
