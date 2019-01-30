package br.com.teste.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teste.model.Localizacao;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {

}
