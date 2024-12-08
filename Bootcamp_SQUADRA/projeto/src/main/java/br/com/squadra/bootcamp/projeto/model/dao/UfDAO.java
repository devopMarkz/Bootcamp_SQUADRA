package br.com.squadra.bootcamp.projeto.model.dao;

import br.com.squadra.bootcamp.projeto.model.entities.Uf;

import java.util.List;

public interface UfDAO {
    Uf findByNome(String nomeUF);
    Uf findBySigla(String sigla);
    Uf findById(Long id);
    List<Uf> findAll();
    List<Uf> findByStatus(int status);
    Uf insert(Uf uf);
    Uf update(Uf uf);
    void deleteById(Long id);
    // ----------------------------------------------------------------------------------------
    public Uf findUniqueByFilters(Long codigoUF, String sigla, String nome, Integer status);
}