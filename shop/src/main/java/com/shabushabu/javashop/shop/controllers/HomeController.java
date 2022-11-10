package com.shabushabu.javashop.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import com.shabushabu.javashop.shop.services.InstrumentService;
import com.shabushabu.javashop.shop.services.ProductService;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private InstrumentService instrumentService;
   
    
    @RequestMapping(value="/")
    public String getProductsAllLocations(Model model, @RequestParam(value="name", required=false) String theName, @RequestParam(value="location", required=false) String theLocation) {


	if (null == theName ) {
		theName = "Guest";
	}	
	
	if (null == theLocation ) {
		theLocation="California";
	}

	model.addAttribute("user", new User());

	
	model.addAttribute("products", productService.getProducts(theLocation));

	
	model.addAttribute("instruments", instrumentService.getInstruments(theLocation));

      	
	return "index";
} 
    
   
    /* @RequestMapping(value="/instruments")
    public String viewInstruments(Model model, @RequestParam(value="name", required=false) String theName, @RequestParam(value="location", required=false) String theLocation) {


	if (null == theName ) {
		theName = "Guest";
	}	
	
	if (null == theLocation ) {
		theLocation="California";
	}

	model.addAttribute("user", new User());
*/
	
  
 /*  
   @PostMapping("/adduser")
    public String addUser(@ModelAttribute User user, Model model) {
    	 
        // ORIGINAL CODE
			model.addAttribute("products", productService.getProducts("Chicago"));
			model.addAttribute("instruments", instrumentService.getInstruments());
		// END ORIGINAL CODE
		return "index";
    }
 */
}
