package br.com.teste.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Clima {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idClima;
	
	@NotBlank
	private String tempMAxima;
	
	@NotBlank
	private String tempMinima;


	public Long getIdClima() {
		return idClima;
	}

	public void setIdClima(Long idClima) {
		this.idClima = idClima;
	}

	public String getTempMAxima() {
		return tempMAxima;
	}

	public void setTempMAxima(String tempMAxima) {
		this.tempMAxima = tempMAxima;
	}

	public String getTempMinima() {
		return tempMinima;
	}

	public void setTempMinima(String tempMinima) {
		this.tempMinima = tempMinima;
	}

	
}
