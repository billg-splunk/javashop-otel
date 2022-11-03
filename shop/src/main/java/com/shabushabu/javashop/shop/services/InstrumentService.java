package com.shabushabu.javashop.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.shabushabu.javashop.shop.model.Product;
import com.shabushabu.javashop.shop.model.Instrument;
import com.shabushabu.javashop.shop.repo.StockRepo;
import com.shabushabu.javashop.shop.repo.InstrumentRepo;
import com.shabushabu.javashop.shop.repo.ProductRepo;
import com.shabushabu.javashop.shop.services.dto.ProductDTO;
import com.shabushabu.javashop.shop.services.dto.StockDTO;
import com.shabushabu.javashop.shop.services.dto.InstrumentDTO;
import com.shabushabu.javashop.shop.exceptions.InvalidLocaleException;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InstrumentService {

    @Autowired
    private StockRepo stockRepo;

   @Autowired
   private ProductRepo productRepo;
    
    @Autowired
    private InstrumentRepo instrumentRepo;

    public List<Instrument> getInstruments() {
        Map<Long, InstrumentDTO> instrumentsDTO = instrumentRepo.getinstrumentDTOs();
        return instrumentsDTO.values().stream()
                .map(instrumentDTO -> {
                	  try {
  						return new Instrument().buildForLocale(instrumentDTO.getId(),  
  								instrumentDTO.getTitle(), instrumentDTO.getPrice(), instrumentDTO.getInstrumentType(),
  								instrumentDTO.getCondition(), instrumentDTO.getSellerType(), instrumentDTO.getPublishedDate());
  					} catch (InvalidLocaleException e) {
  						
  						e.printStackTrace();
  						return  new Instrument().buildForLocale(instrumentDTO.getId(),   instrumentDTO.getPrice(), instrumentDTO.getInstrumentType(),
  								instrumentDTO.getCondition(), instrumentDTO.getSellerType(), instrumentDTO.getPublishedDate());	
  					}
                   })
                  .collect(Collectors.toList());
    }

    public List<Product> getProducts() {
        Map<String, ProductDTO> productDTOs = productRepo.getProductDTOs();
        Map<String, StockDTO> stockDTOMap = stockRepo.getStockDTOs();

        // Merge productDTOs and stockDTOs to a List of Products
        return productDTOs.values().stream()
                .map(productDTO -> {
                    StockDTO stockDTO = stockDTOMap.get(productDTO.getId());
                    if (stockDTO == null) {
                        stockDTO = StockDTO.DEFAULT_STOCK_DTO;
                    }
                    return new Product(productDTO.getId(), stockDTO.getSku(), productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), stockDTO.getAmountAvailable());
                })
                .collect(Collectors.toList());
    }

    public List<Product> getProductsNew() {
        Map<String, ProductDTO> productDTOs = productRepo.getProductDTOsNew();
        Map<String, StockDTO> stockDTOMap = stockRepo.getStockDTOs();

        // Merge productDTOs and stockDTOs to a List of Products
        return productDTOs.values().stream()
                .map(productDTO -> {
                    StockDTO stockDTO = stockDTOMap.get(productDTO.getId());
                    if (stockDTO == null) {
                        stockDTO = StockDTO.DEFAULT_STOCK_DTO;
                    }
                    return new Product(productDTO.getId(), stockDTO.getSku(), productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), stockDTO.getAmountAvailable());
                })
                .collect(Collectors.toList());
    }

    public List<Product> productsNotFound() {
        return Collections.emptyList();
    }
}
