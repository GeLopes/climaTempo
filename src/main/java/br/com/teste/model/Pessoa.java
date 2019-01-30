package br.com.teste.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
    private String nome;

    private Integer idade;
    
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idLocalizacao")
    private Localizacao localizacao; 

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Localizacao getLocalizacao() {
		return localizacao;
	}


	public void setLocalizacao(Localizacao localizacao) {
		this.localizacao = localizacao;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public Integer getIdade() {
		return idade;
	}


	public void setIdade(Integer idade) {
		this.idade = idade;
	}

}
