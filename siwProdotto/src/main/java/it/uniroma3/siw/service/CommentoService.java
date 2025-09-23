package it.uniroma3.siw.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CommentoRepository;

@Service
public class CommentoService {

   @Autowired
    private CommentoRepository commentoRepository;
   
	public List<Commento> findFirstNumByProdotto(int n, Prodotto prodotto) {
		
		
		
	    List<Commento> commenti = commentoRepository.findByProdottoOrderByDataDesc(prodotto);

	    if (commenti.size() <= n) {
	        return commenti;
	    }

	    List<Commento> iCommenti = new ArrayList<Commento>();

	    for (int i = 0; i < n; i++) {
	        iCommenti.add(commenti.get(i));
	    }

	    return iCommenti;
	}
	
	public List<Commento> findByProdotto(Prodotto prodotto) {
        return commentoRepository.findByProdottoOrderByDataDesc(prodotto);
    }
	
	@Transactional
	public void save(Commento commento) {
	    commentoRepository.save(commento);
	}
	
	public Commento findById(Long id) {
	    return commentoRepository.findById(id).get();
	}



}
