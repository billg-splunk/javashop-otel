package com.shabushabu.javashop.instruments;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;


import com.shabushabu.javashop.instruments.model.Instrument;
import com.shabushabu.javashop.instruments.repositories.InstrumentRepository;

@SpringBootApplication
public class InstrumentsApplication {
	 
	@Autowired private InstrumentRepository repository; 
	 private final Logger logger = LoggerFactory.getLogger(InstrumentsApplication.class);
	 
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        Iterable<Instrument> instruments = this.repository.findAll(); 
        logger.info("Number of instruments: " + ((Collection<?>) instruments).size());
        logger.info("Number of instruments: " + ((Collection<?>) instruments).size());
        logger.info("Number of instruments: " + ((Collection<?>) instruments).size());
        logger.info("Number of instruments: " + ((Collection<?>) instruments).size());
        logger.info("Number of instruments: " + ((Collection<?>) instruments).size());
        logger.info("Number of instruments: " + ((Collection<?>) instruments).size());
    }
	    
    public static void main(String[] args) {
        SpringApplication.run( InstrumentsApplication.class, args);
    }
}
