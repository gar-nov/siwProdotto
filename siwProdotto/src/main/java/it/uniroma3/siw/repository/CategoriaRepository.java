package it.uniroma3.siw.repository;



import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Categoria;

public interface CategoriaRepository extends CrudRepository<Categoria,Long> {
	boolean existsByNome(String nome);


}