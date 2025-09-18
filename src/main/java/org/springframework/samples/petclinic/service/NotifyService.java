package org.springframework.samples.petclinic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {

    private static final Logger log = LoggerFactory.getLogger(NotifyService.class);

    public void sendVisitReminder(Integer visitId) {
        log.info("Notifying owner about visit with id: {}", visitId);
        // Add logic here
    }
}