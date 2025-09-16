package org.springframework.samples.petclinic.worker;

import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Component;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;

@Component
public class VisitWorkers {

    private static final Logger log = LoggerFactory.getLogger(VisitWorkers.class);
    private final ClinicService clinicService;

    public VisitWorkers(ClinicService clinicService) {
        this.clinicService = clinicService;
    }
    
    @JobWorker(type = "notify-owner")
    public void remindOwnerOfVisit(@VariablesAsType VisitVariables variables) {
        log.info("Notifying owner about visit with id: {}", variables.visitId());
        // Notify user logic would go here
    }

    @JobWorker(type = "charge-visit")
    public VisitVariables chargeForVisit(@VariablesAsType VisitVariables variables) {
        log.info("Charging for visit with id: {}", variables.visitId());
        // Charge credit card logic would go here
        return new VisitVariables(variables.visitId(), "Visit Charged");
    }
}
