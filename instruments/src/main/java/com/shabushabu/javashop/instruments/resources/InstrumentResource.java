package com.shabushabu.javashop.instruments.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.shabushabu.javashop.instruments.exceptions.InstrumentNotFoundException;
import com.shabushabu.javashop.instruments.model.Instrument;
import com.shabushabu.javashop.instruments.services.InstrumentService;

import java.util.List;

@RestController
@RequestMapping("/instruments")
public class InstrumentResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstrumentResource.class);

    @Autowired
    private InstrumentService instrumentService;

    @RequestMapping()
    public List<Instrument> getInstruments() {
        LOGGER.info("getInstruments (All)");
        return instrumentService.getInstruments();
    }

    /*
    @RequestMapping("{productId}")
    public Instrument getInstrument(@PathVariable("productId") String productId) throws InstrumentNotFoundException {
        LOGGER.info("getInstrument with productId: {}", productId);
        return instrumentService.getInstrument(productId);
    }
*/
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleInstrumentNotFound(InstrumentNotFoundException snfe) {
    }
}
