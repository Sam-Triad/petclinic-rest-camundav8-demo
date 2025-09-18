package org.springframework.samples.petclinic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChargeService {

    private static final Logger log = LoggerFactory.getLogger(ChargeService.class);

    private final ClinicService clinicService;

    public ChargeService(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    public void chargeForVisit(Integer visitId, Boolean checkedIn, String visitNotes) {
        log.info("Charging for visit with id: {}", visitId);

        if (checkedIn != null && checkedIn) {
            var visit = clinicService.findVisitById(visitId);
            if (visit != null && visitNotes != null) {
                visit.setDescription(visit.getDescription() + ":\n" + visitNotes);
                clinicService.saveVisit(visit);
                log.info("Saved notes for visit {}: {}", visitId, visitNotes);
            }
        }
        // Logic here
        log.info("Credit card charged for visit {}", visitId);
    }
}