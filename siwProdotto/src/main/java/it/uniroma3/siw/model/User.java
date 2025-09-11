package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "users")
public class User {
	 @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    @NotBlank
	    private String nome;

	    @NotBlank
	    private String cognome;
	    
	    @OneToOne(mappedBy = "user")
	    private Credentials credentials;


	    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
	    private List<Prodotto> prodotti;

	    // Getters e Setters
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getNome() {
	        return nome;
	    }

	    public void setNome(String nome) {
	        this.nome = nome;
	    }

	    public String getCognome() {
	        return cognome;
	    }

	    public void setCognome(String cognome) {
	        this.cognome = cognome;
	    }

	    public Credentials getCredentials() {
	        return credentials;
	    }

	    public void setCredentials(Credentials credentials) {
	        this.credentials = credentials;
	    }

	    public List<Prodotto> getProdotti() {
	        return prodotti;
	    }

	    public void setProdotti(List<Prodotto> prodotti) {
	        this.prodotti = prodotti;
	    }
	    
	 
}
