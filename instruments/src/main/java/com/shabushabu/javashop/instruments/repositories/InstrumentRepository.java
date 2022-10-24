package com.shabushabu.javashop.instruments.repositories;

import org.springframework.data.repository.CrudRepository;
import com.shabushabu.javashop.instruments.model.Instrument;

public interface InstrumentRepository extends CrudRepository<Instrument, String> {
}
