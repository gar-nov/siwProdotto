package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Categoria;
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
			return prodottoRepository.existsByNomeAndPrezzoAndDescrizioneAndFotoAndCategoria(
					prodotto.getNome(),prodotto.getPrezzo(), prodotto.getDescrizione(), prodotto.getFoto(),
					prodotto.getCategoria());
		}
	 
	 public void deleteById(Long id) {
		    prodottoRepository.deleteById(id);
		}

	 public List<Prodotto> findByCategoriaOrderByIdDesc(Categoria categoria) {
		    return prodottoRepository.findByCategoriaOrderByIdDesc(categoria);
		}
	 
	 public void removeSimilar(Long idPrincipale, Long idSimile) {
		    Prodotto principale = prodottoRepository.findById(idPrincipale).get();
		    Prodotto simile = prodottoRepository.findById(idSimile).get();

		    principale.getProdottiSimili().remove(simile);
		    simile.getProdottiSimili().remove(principale);

		    prodottoRepository.save(principale);
		    prodottoRepository.save(simile);
		}



}
