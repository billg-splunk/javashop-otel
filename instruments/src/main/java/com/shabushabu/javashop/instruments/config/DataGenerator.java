package com.shabushabu.javashop.instruments.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;


import javax.sql.DataSource;

@Component
public class DataGenerator {

    @Autowired
    private DataSource dataSource;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() {
    	ResourceDatabasePopulator resourceDatabasePopulator = 
            		new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("sql/instruments-new.sql"));
        resourceDatabasePopulator.execute(dataSource);
        
        resourceDatabasePopulator = 
        		new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("sql/stocks-new.sql"));
        resourceDatabasePopulator.execute(dataSource);
        
    }
}

/*
@Component
public class DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);

    private StockRepository stockRepository;

    @Autowired
    protected DataGenerator(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @PostConstruct
    @Transactional
    public void init() {
        LOGGER.info("Generating synthetic data for demonstration purposes...");

        stockRepository.save(new Stock("1", "12345678", 5));
        stockRepository.save(new Stock("2", "34567890", 2));
        stockRepository.save(new Stock("3", "54326745", 999));
        stockRepository.save(new Stock("4", "93847614", 0));
        stockRepository.save(new Stock("5", "11856388", 1));

        LOGGER.info("... data generation complete");
    }
}*/


