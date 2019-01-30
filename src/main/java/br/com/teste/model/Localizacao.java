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
public class Localizacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idLocalizacao;
	
	private String cidade;
	
	private String longitude;
	
	private String latitude;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idClima")
    private Clima clima;

	public Long getIdLocalizacao() {
		return idLocalizacao;
	}

	public void setIdLocalizacao(Long idLocalizacao) {
		this.idLocalizacao = idLocalizacao;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public Clima getClima() {
		return clima;
	}

	public void setClima(Clima clima) {
		this.clima = clima;
	} 
	
	
}
