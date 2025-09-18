package org.springframework.samples.petclinic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChargeService {

    private static final Logger log = LoggerFactory.getLogger(ChargeService.class);

    public ChargeService() {
    }

    public void chargeForVisit(Integer visitId) {
        log.info("Credit card charged for visit {}", visitId);
    }
}