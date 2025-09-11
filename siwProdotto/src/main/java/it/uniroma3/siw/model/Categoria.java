package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Categoria {
	
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    @NotBlank        
	    private String nome;

	  

	   
	    @OneToMany(mappedBy="categoria", cascade={CascadeType.REMOVE})
	    private List<Prodotto> prodotti;


	    // GETTER & SETTER
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

	   

	    public List<Prodotto> getProdotti() { 
	    	   return prodotti; 
	    }
	    public void setProdotti(List<Prodotto> prodotti) { 
	    	        this.prodotti = prodotti; 
	    }

	    // equals & hashCode 
	    @Override
	    public int hashCode() { 
	    	       return Objects.hash(nome); }
        
	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        Categoria other = (Categoria) obj;
	        return Objects.equals(nome, other.nome);
	              
	    }
}