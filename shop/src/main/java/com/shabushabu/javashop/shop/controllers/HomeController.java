package com.shabushabu.javashop.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	
	//if ( !model.containsAttribute("User") ) {
		User user = new User();
		user.setLocation(theLocation);
		user.setName(theName);
		model.addAttribute("user", user);
	//}
	
	model.addAttribute("products", productService.getProducts(theLocation));

	model.addAttribute("instruments", instrumentService.getInstruments(theLocation));

      	
	return "index";
    
    } 
    
    @RequestMapping("/healthcheck")
    @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    public String healthCheck() {
        return "HTTP Status OK (CODE 200)\n";
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
