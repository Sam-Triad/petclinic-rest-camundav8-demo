package org.springframework.samples.petclinic.workflow;

import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static io.camunda.zeebe.protocol.Protocol.USER_TASK_JOB_TYPE;
import static io.camunda.zeebe.spring.test.ZeebeTestThreadSupport.waitForProcessInstanceCompleted;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ChargeService;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.NotifyService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;

@SpringBootTest
@ZeebeSpringTest
public class VisitProcessTest {

        @Autowired
        private ZeebeTestEngine zeebeTestEngine;
        @Autowired
        private ZeebeClient zeebe;
        @MockitoBean
        private NotifyService notifyService;
        @MockitoBean
        private ChargeService chargeService;
        @MockitoBean
        private ClinicService clinicService;

        @BeforeEach
        void setup() {
                var owner = new Owner();
                owner.setId(1);
                owner.setFirstName("George");
                owner.setLastName("Franklin");

                var pet = new Pet();
                pet.setOwner(owner);

                var visit = new Visit();
                visit.setId(1);
                visit.setPet(pet);

                when(clinicService.findVisitById(1)).thenReturn(visit);
        }

        @Test
        void testCheckedIn_Arrived() throws Exception {
                // ProcessInstanceEvent processInstance = zeebe.newCreateInstanceCommand()
                // .bpmnProcessId("visit_booking")
                // .latestVersion()
                // .variables(Map.of("visitId", 1))
                // .send()
                // .join();

                // assertThat(processInstance).isWaitingAtElements("Task_Confirm_Visit");
                // verify(notifyService, timeout(2000)).sendUserNotification(anyString(),
                // anyString());

        }

}