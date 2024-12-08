package br.com.squadra.bootcamp.projeto.model.dao;

import br.com.squadra.bootcamp.projeto.model.entities.Endereco;

import java.util.List;

public interface EnderecoDAO {
    List<Endereco> findByCodigoPessoa(Long codigoPessoa);
    Endereco insert(Endereco endereco);
    Endereco update(Endereco endereco);
    void deleteByCodigoPessoa(Long codigoPessoa);
    void deleteByCodigoEndereco(Long codigoEndereco);
}
