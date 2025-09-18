package org.springframework.samples.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import io.camunda.zeebe.spring.client.annotation.Deployment;

@SpringBootApplication
@Deployment(resources = {
	"classpath*:/processes/**/*.bpmn",
	"classpath*:/form/**/*.form"
})
public class PetClinicApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}
}
