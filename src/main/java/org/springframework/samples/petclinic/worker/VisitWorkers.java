package org.springframework.samples.petclinic.worker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.service.ChargeService;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.NotifyService;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.CustomHeaders;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;

@Component
public class VisitWorkers {

    private static final Logger log = LoggerFactory.getLogger(VisitWorkers.class);

    private final NotifyService notifyService;
    private final ChargeService chargeService;
    private final ClinicService clinicService;

    public VisitWorkers(NotifyService notifyService, ChargeService chargeService, ClinicService clinicService) {
        this.notifyService = notifyService;
        this.chargeService = chargeService;
        this.clinicService = clinicService;
    }

    @JobWorker(type = "persist-vet-notes")
    public void persistVetNotes(@VariablesAsType VisitVariables variables) {
        var visit = clinicService.findVisitById(variables.visitId());
        visit.setVisitNotes(variables.visitNotes());

        clinicService.saveVisit(visit);
    }

    @JobWorker(type = "notify-owner")
    public void sendUserNotification(@CustomHeaders Map<String, String> headers, @VariablesAsType VisitVariables variables) {
        var message = headers.get("message");
        var visit = clinicService.findVisitById(variables.visitId());
        var owner = visit.getPet().getOwner();

        notifyService.sendUserNotification(message, owner.getFirstName() + " " + owner.getLastName());
    }

    @JobWorker(type = "save-notes")
    public void saveVisitNotes(@VariablesAsType VisitVariables variables) {
        if (variables.visitNotes() != null && !variables.visitNotes().isBlank()) {
            var visit = clinicService.findVisitById(variables.visitId());
            if (visit != null) {
                visit.setVisitNotes(variables.visitNotes());
                clinicService.saveVisit(visit);
                log.info("Saved notes for visit {}: {}", variables.visitId(), variables.visitNotes());
            }
        }
    }

    @JobWorker(type = "charge-visit")
    public VisitVariables chargeForVisit(@VariablesAsType VisitVariables variables) {
        chargeService.chargeForVisit(variables.visitId());
        return new VisitVariables(variables.visitId(), variables.checkedIn(), variables.visitNotes(), "Visit Charged");
    }
}
