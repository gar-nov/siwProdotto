package it.uniroma3.siw.validator;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Commento;


@Component
public class CommentoValidator implements Validator {



    @Override
    public boolean supports(Class<?> clazz) {
        return Commento.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Commento commento = (Commento) target;

        if (commento.getTesto() == null || commento.getTesto().trim().isEmpty()) {
            errors.rejectValue("testo", "commento.testo.empty", "Il commento non può essere vuoto");
        }

        if (commento.getTesto() != null && commento.getTesto().length() > 1000) {
            errors.rejectValue("testo", "commento.testo.lungo", "Massimo 1000 caratteri");
        }
    }
}


