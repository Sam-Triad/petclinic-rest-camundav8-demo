package org.springframework.samples.petclinic.worker;

import org.springframework.samples.petclinic.service.ChargeService;
import org.springframework.samples.petclinic.service.NotifyService;
import org.springframework.stereotype.Component;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;

@Component
public class VisitWorkers {

    private final NotifyService notifyService;
    private final ChargeService chargeService;

    public VisitWorkers(NotifyService notifyService, ChargeService chargeService) {
        this.notifyService = notifyService;
        this.chargeService = chargeService;
    }

    @JobWorker(type = "notify-owner")
    public void remindOwnerOfVisit(@VariablesAsType VisitVariables variables) {
        notifyService.sendVisitReminder(variables.visitId());
    }

    @JobWorker(type = "charge-visit")
    public VisitVariables chargeForVisit(@VariablesAsType VisitVariables variables) {
        chargeService.chargeForVisit(variables.visitId(), variables.checkedIn(), variables.visitNotes());
        return new VisitVariables(variables.visitId(), variables.checkedIn(), variables.visitNotes(), "Visit Charged");
    }
}
