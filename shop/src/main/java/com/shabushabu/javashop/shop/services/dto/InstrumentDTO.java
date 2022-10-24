package com.shabushabu.javashop.shop.services.dto;


public class InstrumentDTO {

	
    private String m_id;
    private String m_title;
    private String m_sub_title;
    private String m_price;
    private String m_instrument_type;
    private String m_condition;
    private String m_post_URL;
    private String m_seller_type;
    private String m_published_date;

    
    public static final InstrumentDTO DEFAULT_INSTRUMENT_DTO = new InstrumentDTO("-199", "default",
    		"default" , "00.00",  "NO INSTRUMENT TYPE", "NO CONDITION", "NO URL", "NO SELLER", "NO DATE");
    

    public InstrumentDTO() {
    }

    public InstrumentDTO(String id, String title, String sub_title, String price, String instument_type, String condition, 
		String post_URL, String seller_type, String published_date) {
	 	m_id= id;
	    m_title = title;
	    m_sub_title = sub_title;
	    m_price = price;
	    m_condition = condition;
	    m_post_URL = post_URL;
	    m_seller_type = seller_type;
	    m_published_date = published_date;
    }
    
    public String getID() {
        return m_id;
    }

    public void setID(String id) {
        m_id = id;
    }
    
 
    public String getTitle() {
        return m_title;
    }

    public void setTitle(String title) {
        m_title = title;
    }
    
    public String getSubTitle() {
        return m_sub_title;
    }

    public void setSubTitle(String sub_title) {
        m_sub_title = sub_title;
    }
    
    public String getPrice() {
        return m_price;
    }

    public void setPrice(String price) {
        m_price = price;
    }
    
    public String getInstrumentType() {
        return m_instrument_type;
    }

    public void setInstrumentType(String type) {
        m_instrument_type = type;
    }
    
    public String getCondition() {
        return m_condition;
    }

    public void setCondition(String condition) {
        m_condition = condition;
    }
    
    public String getPublishedDate() {
        return m_published_date;
    }

    public void setPublishedDate(String published_date) {
        m_published_date = published_date;
    }
    
    public String getPostUrl() {
        return m_post_URL;
    }

    public void setPostURL(String post_URL) {
        m_post_URL = post_URL;
    }
    
    public String getSellerType() {
        return m_seller_type;
    }

    public void setSellerType(String seller_type) {
        m_seller_type = seller_type;
    }

   

  /*  @Override
    public String toString() {
        //return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
  */
}


