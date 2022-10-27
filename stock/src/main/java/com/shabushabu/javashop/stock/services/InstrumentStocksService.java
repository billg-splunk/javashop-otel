package com.shabushabu.javashop.stock.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shabushabu.javashop.stock.exceptions.StockNotFoundException;
import com.shabushabu.javashop.stock.model.Stock;
import com.shabushabu.javashop.stock.repositories.InstrumentStocksRepository;

@Service
public class InstrumentStocksService {
	
	

	    private InstrumentStocksRepository stockRepository;

	    @Autowired
	    public InstrumentStocksService(InstrumentStocksRepository stockRepository) {
	        this.stockRepository = stockRepository;
	    }

	    public List<Stock> getStocks() {
	        return StreamSupport.stream(stockRepository.findAll().spliterator(), false)
	                .collect(Collectors.toList());
	    }
	 
	    public Stock getStock(String productId) throws StockNotFoundException {
	        return stockRepository.findById(productId)
	                .orElseThrow(() -> new StockNotFoundException("Stock not found with productId: " + productId));
	    }
	
}
/*
    @Autowired
    private StockRepo stockRepo;

   @Autowired
   private ProductRepo productRepo;
    
    @Autowired
    private InstrumentRepo instrumentRepo;

    public List<Stock> getInstrumentStocks() {
        Map<String, InstrumentDTO> instrumentsDTO = instrumentRepo.getinstrumentDTOs();
        Map<String, StockDTO> stockDTOMap = stockRepo.getInstrumentStockDTOs();

        // Merge instrumentDTOs and stockDTOs to a List of Instruments and "similar sales" 
        return instrumentsDTO.values().stream()
                .map(instrumentDTO -> {
                    StockDTO stockDTO = stockDTOMap.get(instrumentDTO.getID());
                    if (stockDTO == null) {
                        stockDTO = StockDTO.DEFAULT_STOCK_DTO;
                    }
                    return new Instrument(instrumentDTO.getID(),  
                    		instrumentDTO.getTitle(), instrumentDTO.getPrice(), instrumentDTO.getInstrumentType(),
                    		instrumentDTO.getCondition(), instrumentDTO.getSellerType(), instrumentDTO.getPublishedDate(), 
                    		String.valueOf(stockDTO.getAmountAvailable()));
                 })
                .collect(Collectors.toList());
    }

*/
	  
	  
	  
/*
@Autowired
private InstrumentRepo instrumentRepo;

public List<Instrument> getInstruments() {
    Map<String, InstrumentDTO> instrumentsDTO = instrumentRepo.getinstrumentDTOs();
    Map<String, StockDTO> stockDTOMap = stockRepo.getInstrumentStockDTOs();

    // Merge instrumentDTOs and stockDTOs to a List of Instruments and "similar sales" 
    return instrumentsDTO.values().stream()
            .map(instrumentDTO -> {
                StockDTO stockDTO = stockDTOMap.get(instrumentDTO.getID());
                if (stockDTO == null) {
                    stockDTO = StockDTO.DEFAULT_STOCK_DTO;
                }
                return new Instrument(instrumentDTO.getID(),  
                		instrumentDTO.getTitle(), instrumentDTO.getPrice(), instrumentDTO.getInstrumentType(),
                		instrumentDTO.getCondition(), instrumentDTO.getSellerType(), instrumentDTO.getPublishedDate(), 
                		String.valueOf(stockDTO.getAmountAvailable()));
             })
            .collect(Collectors.toList());
}
*/
	  

