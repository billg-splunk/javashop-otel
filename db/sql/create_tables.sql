DROP TABLE IF EXISTS InstrumentStocks;
CREATE TABLE IF NOT EXISTS InstrumentStocks(
   ID       VARCHAR(4) NOT NULL PRIMARY KEY
  ,Quantity VARCHAR(8)
);

DROP TABLE IF EXISTS InstrumentsForSale;
CREATE TABLE IF NOT EXISTS InstrumentsForSale(
   ID              VARCHAR(50) NOT NULL PRIMARY KEY
  ,Title           VARCHAR(140) NOT NULL
  ,Sub_title       VARCHAR(58) NOT NULL
  ,Price           VARCHAR(12) NOT NULL
  ,Instrument_Type VARCHAR(29) NOT NULL
  ,Condition       VARCHAR(4) NOT NULL
  ,Location        VARCHAR(33) NOT NULL
  ,Post_URL        VARCHAR(114) NOT NULL
  ,Seller_type     VARCHAR(14) NOT NULL
  ,published_date  VARCHAR(14) NOT NULL
);
