package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Categoria;
import it.uniroma3.siw.service.CategoriaService;

@Component
public class CategoriaValidator implements Validator {

    @Autowired
    private CategoriaService categoriaService;

    @Override
    public void validate(Object o, Errors errors) {
        Categoria categoria = (Categoria) o;

        if (categoria.getNome() != null && categoriaService.existsByNome(categoria.getNome())) {
            errors.rejectValue("nome", "categoria.duplicato");
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Categoria.class.equals(clazz);
    }
}
