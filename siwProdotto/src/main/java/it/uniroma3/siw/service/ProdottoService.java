package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.repository.ProdottoRepository;
import jakarta.validation.Valid;


@Service
public class ProdottoService {
	
	@Autowired
	private ProdottoRepository prodottoRepository;
	
	public List<Prodotto> findFirstNum(int n) {
		List<Prodotto> prodotti = prodottoRepository.findAllByOrderByIdDesc();

		if (prodotti.size() <= n) {
			return prodotti;
		}

		List<Prodotto> iprodotti = new ArrayList<Prodotto>();
		;

		for (int i = 0; i < n; i++) {
			iprodotti.add(prodotti.get(i));
		}

		return iprodotti;
	}
	
	public List<Prodotto> findAll() {
		List<Prodotto> animali = prodottoRepository.findAllByOrderByIdDesc(); 
		

		return animali;
	}
	
	public Prodotto findById(Long id) {
	    return prodottoRepository.findById(id).get();
	}

	 @Transactional
	 public Prodotto save(Prodotto prodotto) {
		 return prodottoRepository.save(prodotto);
	}
	 
	 public boolean alreadyExists(Prodotto prodotto) {
			return prodottoRepository.existsByNomeAndDescrizioneAndFotoAndCategoria(
					prodotto.getNome(), prodotto.getDescrizione(), prodotto.getFoto(),
					prodotto.getCategoria());
		}

}
