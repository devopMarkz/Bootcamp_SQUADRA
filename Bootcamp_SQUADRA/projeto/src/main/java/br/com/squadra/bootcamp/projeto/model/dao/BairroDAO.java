package br.com.squadra.bootcamp.projeto.model.dao;

import br.com.squadra.bootcamp.projeto.dto.BairroDTO;
import br.com.squadra.bootcamp.projeto.model.entities.Bairro;

import java.util.List;
import java.util.Optional;

public interface BairroDAO {

    public Bairro findByCodigoBairro(Long codigoBairro);
    public List<Bairro> findByCodigoMunicipio(Long codigoMunicipio);
    public List<Bairro> findAll();
    public List<Bairro> findByStatus(int status);
    public BairroDTO insert(BairroDTO bairroDTO);
    public BairroDTO update(BairroDTO bairroDTO);
    public List<Bairro> findByFilters(Optional<Long> codigoBairro, Optional<Long> codigoMunicipio, Optional<String> nome, Optional<Integer> status);
    Bairro findByNome(String nome);

}
