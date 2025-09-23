package it.uniroma3.siw.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProdottoService;
import it.uniroma3.siw.validator.CommentoValidator;

@Controller
public class CommentoController {
	@Autowired
	private ProdottoService prodottoService;
	
	@Autowired
	private CommentoService commentoService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private CommentoValidator commentoValidator;

	@GetMapping("/prodotto/{id}/commenti")
	public String getCommentiProdotto(@PathVariable("id") Long id, Model model) {
	    // Recupera il prodotto
	    Prodotto prodotto = prodottoService.findById(id);
	    model.addAttribute("prodotto", prodotto);

	    // Recupera tutti i commenti relativi a quel prodotto
	    List<Commento> commenti = commentoService.findByProdotto(prodotto);
	    model.addAttribute("commenti", commenti);
	    model.addAttribute("commentiVuoti", commenti.isEmpty());

	    // Oggetto vuoto per il form
	    model.addAttribute("commento", new Commento());

	    // Utente loggato (se serve per gestire autore/modifica)
	    User currentUser = credentialsService.getCurrentUser();
	    if (currentUser != null) {
	        model.addAttribute("currentUserId", currentUser.getId());
	    }

	    return "commenti"; // nome della pagina Thymeleaf (commenti.html)
	}
	
	@PostMapping("/prodotto/{id}/commenti")
	public String addCommento(@PathVariable("id") Long id,
	                          @ModelAttribute("commento") Commento commento,
	                          BindingResult bindingResult,
	                          Model model) {
	    
	    // Recupera prodotto e utente corrente
	    Prodotto prodotto = prodottoService.findById(id);
	    User currentUser = credentialsService.getCurrentUser();

	    // Validazione personalizzata
	    commentoValidator.validate(commento, bindingResult);

	    if (bindingResult.hasErrors()) {
	        // In caso di errori, torna alla pagina con form + commenti
	        model.addAttribute("prodotto", prodotto);
	        List<Commento> commenti = commentoService.findByProdotto(prodotto);
	        model.addAttribute("commenti", commenti);
	        model.addAttribute("commentiVuoti", commenti.isEmpty());
	        if (currentUser != null)
	            model.addAttribute("currentUserId", currentUser.getId());
	        return "commenti"; // commenti.html
	    }

	    // Salvataggio commento
	    if (currentUser != null) {
	    	 commento.setUser(currentUser);
	         commento.setProdotto(prodotto);
	         commento.setData(LocalDateTime.now()); // opzionale
	    	 commentoService.save(commento);
	    }

	    return "redirect:/prodotto/" + id + "/commenti";
	}
	
	@GetMapping("/commenti/{id}/modifica")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
	    Commento commento = commentoService.findById(id);
	    User currentUser = credentialsService.getCurrentUser();

	    // Sicurezza: solo utente del commento può modificare
	    if (currentUser == null || !commento.getUser().getId().equals(currentUser.getId())) {
	    	 return "unauthorized";
	    }

	    model.addAttribute("commento", commento);
	    return "commentoEditForm"; // pagina con textarea già precompilata
	}

	@PostMapping("/commenti/edit/{id}")
	public String editCommento(@PathVariable Long id,
	                           @ModelAttribute("commento") Commento commentoModificato,
	                           BindingResult bindingResult,
	                           Model model) {
	    Commento commentoOriginale = commentoService.findById(id);
	    User currentUser = credentialsService.getCurrentUser();

	    // sicurezza
	    if (!commentoOriginale.getUser().equals(currentUser)) {
	        return "unauthorized";
	    }

	    // validazione
	    commentoValidator.validate(commentoModificato, bindingResult);
	    if (bindingResult.hasErrors()) {
	        // IMPORTANTE: rimetti il prodotto nel commento (serve per il th:href)
	        commentoOriginale.setTesto(commentoModificato.getTesto()); // per mostrare testo aggiornato
	        model.addAttribute("commento", commentoOriginale);
	        return "commentoEditForm";
	    }

	    commentoOriginale.setTesto(commentoModificato.getTesto());
	    commentoOriginale.setData(LocalDateTime.now());
	    commentoService.save(commentoOriginale);

	    return "redirect:/prodotto/" + commentoOriginale.getProdotto().getId() + "/commenti";
	}

	


}
