package it.uniroma3.siw.controller;

import java.io.IOException;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Categoria;
import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.service.CategoriaService;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProdottoService;
import it.uniroma3.siw.validator.CommentoValidator;
import it.uniroma3.siw.validator.ProdottoValidator;
import jakarta.validation.Valid;
import it.uniroma3.siw.model.User;


@Controller
public class ProdottoController {
	public static final int NUM_OF_PRODUCTS = 5; 
	public static final int NUM_OF_COMMS = 3; 

	@Autowired
	private ProdottoService prodottoService;
	
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private CommentoService commentoService;
	
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private CommentoValidator commentoValidator;
	
	@Autowired
	private ProdottoValidator prodottoValidator;

	
/////////////////////gestione prodotti in home
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
	    List<Commento> commenti = commentoService.findFirstNumByProdotto(NUM_OF_COMMS, prodotto);
	    model.addAttribute("commenti", commenti); 
	    model.addAttribute("commentiVuoti", commenti.isEmpty()); 

	    

	    // Utente autenticato
	    User currentUser = credentialsService.getCurrentUser();
	    if (currentUser != null) {
	        model.addAttribute("currentUserId", currentUser.getId());
	    }
	    return "prodottoDetail";
	}
	
////////////////////////////gestione aggiunta prodotto
	@GetMapping("/admin/prodotto/form")
	public String AddProdotto(Model model) {
	    //Recupera l'autenticazione
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || !authentication.isAuthenticated()) {
	        return "unauthorized";
	    }

	    //Recupera lo username
	    String username = authentication.getName();

	    // 📦 Recupera le credenziali
	    Credentials credentials = credentialsService.getCredentials(username);

	    //Controllo ruolo
	    if (credentials == null || !credentials.getRole().equals("ADMIN")) {
	        return "unauthorized";
	    }


	    model.addAttribute("prodotto", new Prodotto());
	    model.addAttribute("categorie", categoriaService.findAll());
	    return "prodottoForm.html";
	}



	
	@PostMapping("/admin/prodotti")
	public String addProdotto(@Valid @ModelAttribute("prodotto") Prodotto prodotto,
	                          BindingResult bindingResult,
	                          Model model,
	                          @RequestParam("image") MultipartFile multipartFile) throws IOException {

	    //Controllo accesso: solo admin può aggiungere
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || !authentication.isAuthenticated()) {
	        return "unauthorized";
	    }

	    String username = authentication.getName();
	    Credentials credentials = credentialsService.getCredentials(username);

	    if (credentials == null || !credentials.getRole().equals("ADMIN")) {
	        return "unauthorized";
	    }

	    String fileName = null;
        if (!multipartFile.isEmpty()) {
            fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            fileName = fileName.replaceAll("\\s+", "");
	        prodotto.setFoto(fileName);
	    }

	   
	    prodottoValidator.validate(prodotto, bindingResult);

	    if (bindingResult.hasErrors()) {
	        model.addAttribute("categorie", categoriaService.findAll());
	        return "prodottoForm.html";
	    }

	    //Salvataggio
	    Prodotto prodottoSalvato = prodottoService.save(prodotto);
	    if (!multipartFile.isEmpty()) {
	        String uploadDir1 = "src/main/resources/static/images/prodotti-foto/" + prodottoSalvato.getId();
	        String uploadDir2 = "target/classes/static/images/prodotti-foto/" + prodottoSalvato.getId();
	        FileUploadUtil.saveFile(uploadDir1, fileName, multipartFile);
	        FileUploadUtil.saveFile(uploadDir2, fileName, multipartFile);
	    }

	    return "redirect:/prodotto/" + prodottoSalvato.getId();
	}
	
	/////////////////////////////GESTIONE MODIFICA PRODOTTO
	@GetMapping("/admin/prodotto/edit/form/{id}")
	public String editProdottoForm(@PathVariable Long id, Model model) {
	    Prodotto prodotto = prodottoService.findById(id);
	    model.addAttribute("prodotto", prodotto);
	    model.addAttribute("categorie", categoriaService.findAll());

	    // Controllo accesso: solo ADMIN può modificare prodotti
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName();
	    Credentials credentials = credentialsService.getCredentials(username);

	    if (credentials != null && "ADMIN".equals(credentials.getRole())) {
	        return "prodottoEditForm";
	    }
	    return "unauthorized";
	}
	
	@PostMapping("/admin/prodotto/edit/{id}")
	public String editProdotto(@ModelAttribute("prodotto") Prodotto prodotto,
	                           BindingResult bindingResult,
	                           @PathVariable Long id,
	                           Model model,
	                           @RequestParam("image") MultipartFile multipartFile) throws IOException {

	    // Recupera il prodotto originale dal DB
	    Prodotto vecchioProdotto = this.prodottoService.findById(id);

	    // Mantieni ID
	    prodotto.setId(vecchioProdotto.getId());

	    // Gestione immagine
	    if (multipartFile.isEmpty()) {
	        prodotto.setFoto(vecchioProdotto.getFoto()); // nessuna nuova immagine → tieni la vecchia
	    } else {
	        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        fileName = fileName.replaceAll("\\s+", "");
	        prodotto.setFoto(fileName);
	    }
	    // Validazione solo se è stato modificato
	    if (!vecchioProdotto.equals(prodotto)) {
	        this.prodottoValidator.validate(prodotto, bindingResult);
	    }

	    // In caso di errori, torna al form
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("categorie", this.categoriaService.findAll());
	        return "prodottoEditForm";
	    }

	    // Salvataggio
	    Prodotto prodottoSalvato = this.prodottoService.save(prodotto);

	    // Salvataggio immagine (se presente)
	    if (!multipartFile.isEmpty()) {
	        String uploadDir1 = "src/main/resources/static/images/prodotti-foto/" + prodottoSalvato.getId();
	        String uploadDir2 = "target/classes/static/images/prodotti-foto/" + prodottoSalvato.getId();
	        FileUploadUtil.saveFile(uploadDir1, prodotto.getFoto(), multipartFile);
	        FileUploadUtil.saveFile(uploadDir2, prodotto.getFoto(), multipartFile);
	    }

	    return "redirect:/prodotto/" + prodottoSalvato.getId();
	}
//////////////////////////GESTIONE ELIMINTA PRODOTTO
	@GetMapping("/admin/prodotto/delete/confirm/{id}")
	public String confirmDeleteProdotto(@PathVariable("id") Long id) {

	    // Controllo accesso: solo ADMIN può modificare prodotti
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName();
	    Credentials credentials = credentialsService.getCredentials(username);

	    // Solo ADMIN può eliminare un prodotto
	    if (credentials != null && Credentials.ADMIN_ROLE.equals(credentials.getRole())) {
	        this.prodottoService.deleteById(id);
	        return "redirect:/prodotti";
	    }
	    // NON autorizzato
	    return "unauthorized";
	}
////////////////////////////////GESTIONE PRODOTTI SIMILI
	@GetMapping("/admin/prodotti/{id}/simili/candidati")
	public String getCandidatiSimili(@PathVariable("id") Long prodottoId, Model model) {
	    Prodotto corrente = prodottoService.findById(prodottoId);
	    Categoria categoria = corrente.getCategoria();

	    // Recupera tutti i prodotti della categoria usando il servizio
	    List<Prodotto> prodottiDellaCategoria = prodottoService.findByCategoriaOrderByIdDesc(categoria);


	    // Rimuovi il prodotto corrente dalla lista
	    prodottiDellaCategoria.remove(corrente);
	    //Rimuove anche i prodotti già simili
	    prodottiDellaCategoria.removeAll(corrente.getProdottiSimili());


	    // Passa i dati al model
	    model.addAttribute("categoria", categoria);
	    model.addAttribute("prodotti", prodottiDellaCategoria);
	    model.addAttribute("corrente", corrente);
	  

	    return "prodottiSimiliCandidati"; 
	}
	
	@GetMapping("/admin/prodotti/{correnteId}/simili/add/{simileId}")
	public String addProdottoSimile(@PathVariable Long correnteId,
	                                @PathVariable Long simileId) {
	    Prodotto corrente = prodottoService.findById(correnteId);
	    Prodotto simile = prodottoService.findById(simileId);

	    corrente.getProdottiSimili().add(simile);
	    simile.getProdottiSimili().add(corrente);

	    prodottoService.save(corrente);
	    prodottoService.save(simile);

	    // dopo l'aggiunta, torno al dettaglio del prodotto corrente
	    return "redirect:/prodotto/" + correnteId;
	}
	/////////////////////GESTIONE ELIMINAZIONE PRODOTTI SIMILI
	@GetMapping("/admin/prodotti/{id}/simili/remove")
	public String showProdottiSimiliDaEliminare(@PathVariable Long id, Model model) {
	    Prodotto prodotto = prodottoService.findById(id);

	    model.addAttribute("corrente", prodotto);
	    model.addAttribute("prodottiSimili", prodotto.getProdottiSimili());

	    return "prodottiSimiliRemove"; // nuovo file HTML
	}
	
	@GetMapping("/admin/prodotti/{id}/simili/remove/{simileId}")
	public String removeProdottoSimile(@PathVariable Long id, @PathVariable Long simileId) {
	    prodottoService.removeSimilar(id, simileId);
	    return "redirect:/prodotto/" + id;
	}









}
