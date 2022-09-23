package com.ameritas.viafilehandler.rabbitmq.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ViaFileHandlerProducerApp {

	public static void main(String[] args) {
		SpringApplication.run(ViaFileHandlerProducerApp.class, args);
	}

}
