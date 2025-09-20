package it.uniroma3.siw.controller;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.service.CategoriaService;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProdottoService;


@Controller
public class ProdottoController {
	public static final int NUM_OF_PRODUCTS = 5; 

	@Autowired
	private ProdottoService prodottoService;
	
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private CommentoService commentoService;
	
	
	@Autowired
	private CredentialsService credentialsService;
	
//gestione prodotti in home
	@GetMapping("/home")
	public String getProdottoHome(Model model) {
		 List<Prodotto> prodotti = prodottoService.findFirstNum(NUM_OF_PRODUCTS);
		    System.out.println("PRODOTTI RECUPERATI: " + prodotti.size()); //per il debug
		    model.addAttribute("prodotti", prodotti);
		    return "index";
	}
	
	@GetMapping("/prodotti")
	public String getListaProdotti(Model model) {
	    model.addAttribute("prodotti", prodottoService.findAll());
	    model.addAttribute("categorie", categoriaService.findAll());
	    return "prodotti.html";
	}
	
	@GetMapping("/prodotto/{id}")
	public String getProdottoDetails(@PathVariable("id") Long id, Model model) {
	    Prodotto prodotto = prodottoService.findById(id);
	    model.addAttribute("prodotto", prodotto);

	    // Prodotti simili
	    List<Prodotto> prodottiSimili = prodotto.getProdottiSimili();
        model.addAttribute("prodottiSimili", prodottiSimili);

	    // Commenti recenti (3)
	    List<Commento> commenti = commentoService.findFirstNumByProdotto(3, prodotto);
	    model.addAttribute("commenti", commenti);

	    // ID dell'utente autenticato (per gestire modifica/elimina commenti)

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("currentUserId", credentials.getUser().getId());
        }

	    return "prodottoDetail";
	}

}
