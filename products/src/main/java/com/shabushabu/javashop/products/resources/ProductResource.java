package com.shabushabu.javashop.products.resources;

import com.google.inject.Inject;
import com.shabushabu.javashop.products.services.ProductService;
import com.shabushabu.javashop.products.model.Product;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

import java.util.Optional;
import java.util.Random;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    private ProductService productService;

    @Inject
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GET
    public Response getAllProducts(@DefaultValue("California") @QueryParam("location") String location) {
    	  
    	// STEP X: All we know right now is somehwere in this function, latency was introduced.
    	

    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	if (location.equals("Colorado"))
    		myCoolFunction_Colorado();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();
    	myCoolFunction();
    	myCoolFunction();
	   
	    myCoolFunction();
    	myCoolFunction();
    	
    	myCoolFunction();	  
			
        return Response.status(200)
                .entity(productService.getAllProducts())
                .build();
    }
  
    private void myCoolFunction() {
      // Generate a FAST sleep of 0 time !
      int sleepy = 0;
      try{
      Thread.sleep(sleepy);
      } catch (Exception e){}
    }

    private void myCoolFunction_Colorado() {
      // Generate a SLOW sleep of Random time !
      Random random = new Random();
      int sleepy = random.nextInt(5000 - 3000) + 3000;
      try{
    	  Thread.sleep(sleepy);
      } catch (Exception e){}
    }

    @GET
    @Path("{id}")
    public Response getProduct(@PathParam("id") String id) {
        Optional<Product> result = productService.getProduct(id);

        if (result.isPresent()) {
            return Response.status(Response.Status.OK)
                    .entity(result.get())
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .build();
        }
    }
}
