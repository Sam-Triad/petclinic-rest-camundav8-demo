package org.springframework.samples.petclinic.worker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record VisitVariables(Integer visitId, String outcome) {
}
