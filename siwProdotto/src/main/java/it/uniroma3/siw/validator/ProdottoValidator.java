package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.service.ProdottoService;

@Component
public class ProdottoValidator implements Validator {

    @Autowired
    private ProdottoService prodottoService;

    @Override
    public void validate(Object target, Errors errors) {
        if (this.prodottoService.alreadyExists((Prodotto) target)) {
            errors.reject("prodotto.duplicato");
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Prodotto.class.equals(clazz);
    }
}

