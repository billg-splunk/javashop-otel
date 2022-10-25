package com.shabushabu.javashop.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Random;
//import io.opentelemetry.extension.auto.annotations.WithSpan;

import com.shabushabu.javashop.shop.services.InstrumentService;
import com.shabushabu.javashop.shop.services.ProductService;

@Controller
public class HomeController {
	//private static final Tracer s_tracer = GlobalOpenTelemetry.getTracer("javasshop.tracer");

    @Autowired
    private ProductService productService;

    @Autowired
    private InstrumentService instrumentService;
    /*
   @RequestMapping(value="/", method = RequestMethod.GET)
   public String usingRequestParam(Model model, @RequestParam(value="name", required=false) String thename, @RequestParam(value="location", required=false) String theLocation) {

	 	// Create Span
	   Span span = s_tracer.spanBuilder("usingRequestParam").startSpan();
	   	// Put the span into the current Context
	   try (Scope scope = span.makeCurrent()) {
	     
		 // Set Name tag: This will be our unique way to search for a trace, by specific user at a specifc time in UI.
			span.setAttribute("name",thename);
		// Set Favorite Color tag: This will allow us to see traffic by "favcolor" in UI.
			span.setAttribute("favcolor", thecolor);
        // ORIGINAL CODE
			model.addAttribute("user", new User());
			model.addAttribute("products", productService.getProducts());
		// END ORIGINAL CODE
	   
	        
		} finally {
	          span.end(); 
	   	}

     return "index";
   } 
   */
    
    @RequestMapping(value="/")
    public String usingRequestParam(Model model, @RequestParam(value="name", required=false) String theName, @RequestParam(value="location", required=false) String theLocation) {


	if (null == theName ) {
		theName = "Guest";
	}	
	
	if (null == theLocation ) {
		theLocation="California";
	}

	model.addAttribute("user", new User());

	if (theLocation.equalsIgnoreCase("Colorado")) {
		model.addAttribute("products", productService.getProductsNew());
	}else {
		model.addAttribute("products", productService.getProducts());
	}	
      	
	return "index";
    } 
    
   
    
    @RequestMapping(value="/instruments")
    public String viewInstruments(Model model, @RequestParam(value="name", required=false) String theName, @RequestParam(value="location", required=false) String theLocation) {


	if (null == theName ) {
		theName = "Guest";
	}	
	
	if (null == theLocation ) {
		theLocation="California";
	}

	model.addAttribute("user", new User());

	if (theLocation.equalsIgnoreCase("Chicago")) {
		 model.addAttribute("instruments", instrumentService.getInstruments());
	}else {
		model.addAttribute("products", productService.getProducts());
	}	
      	
	return "index";
    } 
    
  
    
  
   
   @PostMapping("/adduser")
    public String addUser(@ModelAttribute User user, Model model) {
    	 
        // ORIGINAL CODE
			model.addAttribute("products", productService.getProducts());
		// END ORIGINAL CODE
		return "index";
    }
 
}
