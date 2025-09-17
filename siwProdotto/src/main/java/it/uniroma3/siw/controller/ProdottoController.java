package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.service.CategoriaService;
import it.uniroma3.siw.service.ProdottoService;


@Controller
public class ProdottoController {
	public static final int NUM_OF_PRODUCTS = 4; 

	@Autowired
	private ProdottoService prodottoService;
	
	
	@Autowired
	private CategoriaService categoriaService;
	
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
}
