package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Prodotto;



public interface CommentoRepository extends CrudRepository<Commento, Long> {

	List<Commento> findByProdottoOrderByDataDesc(Prodotto prodotto);

}
