package com.shabushabu.javashop.instruments.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.shabushabu.javashop.instruments.model.Instrument;
import com.shabushabu.javashop.instruments.repositories.InstrumentRepository;

import javax.annotation.PostConstruct;

@Component
public class DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);

    private InstrumentRepository instrumentRepository;

    @Autowired
    protected DataGenerator(InstrumentRepository instrumentsRepository) {
        this.instrumentRepository = instrumentsRepository;
    }
/*
    @PostConstruct
    @Transactional
    public void init() {
        LOGGER.info("Generating synthetic data for demonstration purposes...");

        instrumentsRepository.save(new Instrument("1", "12345678", 5));
        instrumentsRepository.save(new Instrument("2", "34567890", 2));
        instrumentsRepository.save(new Instrument("3", "54326745", 999));
        instrumentsRepository.save(new Instrument("4", "93847614", 0));
        instrumentsRepository.save(new Instrument("5", "11856388", 1));

        LOGGER.info("... data generation complete");
    }
 */
}
