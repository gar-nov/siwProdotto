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
	
	@Transactional
	public void save(Commento commento, Prodotto prodotto, User user) {
	    commento.setProdotto(prodotto);
	    commento.setUser(user);

	    // Imposta data e ora attuali
	    commento.setData(LocalDateTime.now());

	    commentoRepository.save(commento);
	}


}
