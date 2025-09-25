package it.uniroma3.siw.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Categoria;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.service.CategoriaService;
import it.uniroma3.siw.validator.CategoriaValidator;
import jakarta.validation.Valid;

@Controller
public class CategoriaController {
	
	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private CategoriaValidator categoriaValidator;
	@GetMapping("/categoria/{id}")
	public String getCategoria(@PathVariable("id") Long id, Model model) {
		Categoria categoria = categoriaService.findById(id);
		model.addAttribute("categoria", categoria);
		
		List<Prodotto> prodotti = categoria.getProdotti();
		
		Collections.reverse(prodotti); //così appaiono dal più recente al meno recente
		
		model.addAttribute("prodotti", prodotti);
		

		return "categoria";
	}
	
	@GetMapping("/admin/categoria/form")
	public String showCategoriaForm(Model model) {
	    model.addAttribute("categoria", new Categoria());
	    return "categoriaForm";
	}
	
	@PostMapping("/admin/categoria")
	public String addCategoria(@Valid @ModelAttribute("categoria") Categoria categoria,
	                           BindingResult bindingResult,
	                           Model model) {

		categoriaValidator.validate(categoria, bindingResult);

	    if (bindingResult.hasErrors()) {
	        // Se ci sono errori, torno al form con l'elenco categorie
	       
	        return "categoriaForm"; // o "admin/categoriaForm" se usi la sottocartella
	    }

	    categoriaService.save(categoria);
	    return "redirect:/prodotti";
	}



}
