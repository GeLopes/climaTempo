package br.com.teste.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teste.model.Clima;

public interface ClimaRepository extends JpaRepository<Clima, Long> {

}
