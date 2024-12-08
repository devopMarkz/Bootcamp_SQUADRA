package br.com.squadra.bootcamp.projeto.model.dao;

import br.com.squadra.bootcamp.projeto.model.entities.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaDAO {
    Pessoa findByCodigoPessoa(Long codigoPessoa);
    List<Pessoa> findByFilters(Optional<Long> codigoPessoa, Optional<String> login, Optional<Integer> status);
    List<Pessoa> findAll();
    Pessoa insert(Pessoa pessoa);
    Pessoa update(Pessoa pessoa);
    Pessoa findByLogin(String login);
}
