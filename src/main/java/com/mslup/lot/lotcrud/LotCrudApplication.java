package com.mslup.lot.lotcrud;

import com.mslup.lot.lotcrud.model.Flight;
import com.mslup.lot.lotcrud.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class LotCrudApplication {
	public static void main(String[] args) {
		SpringApplication.run(LotCrudApplication.class, args);
	}
}
