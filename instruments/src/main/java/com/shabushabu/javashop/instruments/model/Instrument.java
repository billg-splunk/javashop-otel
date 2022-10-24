package com.shabushabu.javashop.instruments.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "InstrumentsForSale")
public class Instrument {

	@Column(name = "ID", nullable = false)
    private String m_id;
    
    
    @Column(name = "Title", nullable = false)
    private String m_title;
    
    @Column(name = "Sub_title", nullable = false)
    private String m_sub_title;
    
    @Column(name = "Price", nullable = false)
    private String m_price;
    
    @Column(name = "Instrument_type", nullable = false)
    private String m_instrument_type;
    
    @Column(name = "Condition", nullable = false)
    private String m_condition;
    
    @Column(name = "Post_URL", nullable = false)
    private String m_post_URL;
    
    @Column(name = "Seller Type", nullable = false)
    private String m_seller_type;
    
    @Column(name = "published_date", nullable = false)
    private String m_published_date;

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


