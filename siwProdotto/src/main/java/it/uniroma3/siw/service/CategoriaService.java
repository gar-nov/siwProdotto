package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Categoria;
import it.uniroma3.siw.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public List<Categoria> findAll() {
		List<Categoria> categorie = new ArrayList<Categoria>();

		for (Categoria c : categoriaRepository.findAll()) {
			categorie.add(c);
		}

		return categorie;
	}
	
	//gestione visualizzazione categoriA
	
		public Categoria findById(Long id) {
			return categoriaRepository.findById(id).get();
		}
}
