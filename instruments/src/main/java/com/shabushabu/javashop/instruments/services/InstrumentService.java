package com.shabushabu.javashop.instruments.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shabushabu.javashop.instruments.exceptions.InstrumentNotFoundException;
import com.shabushabu.javashop.instruments.model.Instrument;
import com.shabushabu.javashop.instruments.repositories.InstrumentRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class InstrumentService {

    private InstrumentRepository instrumentRepo;

    @Autowired
    public InstrumentService(InstrumentRepository instrumentRepo) {
        this.instrumentRepo = instrumentRepo;
    }

    public List<Instrument> getInstruments() {
        return StreamSupport.stream(instrumentRepo.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
/*
    public Instrument getInstrument(String productId) throws InstrumentNotFoundException {
        return instrumentRepo.findById(productId)
                .orElseThrow(() -> new InstrumentNotFoundException("Instrument not found with productId: " + productId));
    }
*/
}
