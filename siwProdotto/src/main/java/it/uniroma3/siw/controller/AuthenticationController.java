package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
	
	

	    @GetMapping("/")
	    public String home() {
	        return "redirect:/home"; // reindirizza alla home vera con i prodotti
	    }
	

}
