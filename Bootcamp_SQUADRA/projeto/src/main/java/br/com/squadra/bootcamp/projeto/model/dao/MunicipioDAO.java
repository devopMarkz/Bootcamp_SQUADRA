package br.com.squadra.bootcamp.projeto.model.dao;

import br.com.squadra.bootcamp.projeto.dto.MunicipioDTO;
import br.com.squadra.bootcamp.projeto.model.entities.Municipio;

import java.util.List;
import java.util.Optional;

public interface MunicipioDAO {

    Municipio findByCodigoMunicipio(Long codigoMunicipio);
    List<Municipio> findByCodigoUF(Long codigoUF);
    List<Municipio> findAll();
    List<Municipio> findByStatus(int status);
    Municipio findByNome(String nomeMunicipio);
    MunicipioDTO insert(MunicipioDTO municipioDTO);
    MunicipioDTO update(MunicipioDTO municipio);

    List<Municipio> findByFilters(Optional<Long> codigoMunicipio, Optional<Long> codigoUF, Optional<String> nome, Optional<Integer> status);


}