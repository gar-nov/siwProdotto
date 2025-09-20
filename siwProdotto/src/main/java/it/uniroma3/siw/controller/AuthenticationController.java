package it.uniroma3.siw.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.validator.CredentialsValidator;
import it.uniroma3.siw.validator.UserValidator;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
	   @Autowired
	    private CredentialsService credentialsService;

	    @Autowired
	    private UserValidator userValidator;

	    @Autowired
	    private CredentialsValidator credentialsValidator;
	

	    @GetMapping("/")
	    public String home() {
	        return "redirect:/home"; // reindirizza alla home vera con i prodotti
	    }
	    
	    // Mostra il form di registrazione
	    @GetMapping("/register")
	    public String showRegisterForm(Model model) {
	        model.addAttribute("user", new User());
	        model.addAttribute("credentials", new Credentials());
	        return "formRegisterUser";
	    }

	    
	    @PostMapping("/register")
	    public String registerUser(@ModelAttribute("credentials") Credentials credentials,
	                               BindingResult credentialsBindingResult,
	                               @ModelAttribute("user") User user,
	                               BindingResult userBindingResult,
	                               Model model) {

	        this.userValidator.validate(user, userBindingResult);
	        this.credentialsValidator.validate(credentials, credentialsBindingResult);

	        if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
	            credentials.setUser(user);
	            credentialsService.saveCredentials(credentials);
	            return "formLogin";
	        }

	        return "formRegisterUser";
	    }
	    
	    // Mostra il form di login
	    @GetMapping("/login")
	    public String showLoginForm(Model model) {
	        return "formLogin";
	    }

	    
	    
	    @GetMapping("/success")
	    public String getTheRightPath(Model model) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        // Controlla se l'utente è autenticato
	        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
	            return "redirect:/login";
	        }

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

	        // Puoi comunque passare le info utente, se ti servono nella pagina index
	        model.addAttribute("username", userDetails.getUsername());
	        model.addAttribute("roles", authentication.getAuthorities());

	        // Tutti gli utenti, anche admin, vanno sulla stessa pagina
	        return "index";  
	    }

	

}
