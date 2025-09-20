package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Prodotto {
	
		
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    @NotBlank
	    private String nome;
	    
	    @NotNull
	    @PositiveOrZero
	    private double prezzo;



	    @NotBlank
	    @Column(length = 1000)  // per modificare massimo caratt
	    private String descrizione;
	    
	    @Column(nullable = true, length = 64)
	    private String foto;

	  

	    @ManyToOne
	    @NotNull
	    @JoinColumn(name = "categoria_id")
	    private Categoria categoria;

	    @ManyToMany
	    private List<Prodotto> prodottiSimili;

	     
	    
	    // GETTER & SETTER
	    public List<Prodotto> getProdottiSimili() {
	        return prodottiSimili;
	    }

	    public void setProdottiSimili(List<Prodotto> prodottiSimili) {
	        this.prodottiSimili = prodottiSimili;
	    }

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

	    public double getPrezzo() {
	        return prezzo;
	    }

	    public void setPrezzo(double prezzo) {
	        this.prezzo = prezzo;
	    }
	

	    public String getDescrizione() {
	        return descrizione;
	    }

	    public void setDescrizione(String descrizione) {
	        this.descrizione = descrizione;
	    }

	    public String getFoto() {
	        return foto;
	    }

	    public void setFoto(String foto) {
	        this.foto = foto;
	    }

		 

	    public Categoria getCategoria() {
	        return categoria;
	    }

	    public void setCategoria(Categoria categoria) {
	        this.categoria = categoria;
	    }

	   
	   
	    
	 

	   
	    public String getFotoImagePath() {
	        if (this.foto == null || this.id == null)
	            return null;
	        return "/images/prodotto-foto/" + this.id + "/" + this.foto;
	    }
	    
	    // HASHCODE & EQUALS
	    @Override
	    public int hashCode() {
	        return Objects.hash(categoria, descrizione, foto, nome,prezzo);
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null || getClass() != obj.getClass())
	            return false;
	        Prodotto other = (Prodotto) obj;
	        return Double.compare(other.prezzo, this.prezzo) == 0
	                && Objects.equals(this.nome, other.nome)
	                && Objects.equals(this.descrizione, other.descrizione)
	                && Objects.equals(this.foto, other.foto)
	                && Objects.equals(this.categoria, other.categoria);
	    }
	
}
