package br.com.teste.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teste.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
