CREATE TABLE ItemsForSale(
   Title           VARCHAR(81) NOT NULL
  ,Sub_title       VARCHAR(58) NOT NULL
  ,Price           VARCHAR(12) NOT NULL
  ,Instrument_Type VARCHAR(29) NOT NULL
  ,Condition       VARCHAR(4) NOT NULL
  ,Location        VARCHAR(33) NOT NULL
  ,Description     VARCHAR(5502) NOT NULL
  ,Post_URL        VARCHAR(114) NOT NULL PRIMARY KEY
  ,Seller_name     VARCHAR(74) NOT NULL
  ,Seller_type     VARCHAR(14) NOT NULL
  ,published_date  VARCHAR(14) NOT NULL
);


