package br.com.squadra.bootcamp.projeto.model.dao;

import br.com.squadra.bootcamp.projeto.model.dao.impl.*;
import br.com.squadra.bootcamp.projeto.service.ConnectionService;

/**
 * Classe responsável por implementar o padrão Factory para instanciar objetos DAO.
 * <p>
 * Esta classe fornece métodos estáticos para criar instâncias específicas de DAOs,
 * garantindo que todos utilizem a mesma conexão gerenciada pelo {@link ConnectionService}.
 * </p>
 */
public class DAOFactory {

    /**
     * Cria e retorna uma instância de {@link UfDAO}.
     * <p>
     * A implementação retornada é {@link UfDAOImpl}, com a conexão obtida do {@link ConnectionService}.
     * </p>
     *
     * @return Uma instância de {@link UfDAO}.
     */
    public static UfDAO createUfDAO() {
        return new UfDAOImpl(ConnectionService.getConnection());
    }

    /**
     * Cria e retorna uma instância de {@link MunicipioDAO}.
     * <p>
     * A implementação retornada é {@link MunicipioDAOImpl}, com a conexão obtida do {@link ConnectionService}.
     * </p>
     *
     * @return Uma instância de {@link MunicipioDAO}.
     */
    public static MunicipioDAO createMunicipioDAO() {
        return new MunicipioDAOImpl(ConnectionService.getConnection());
    }

    /**
     * Cria e retorna uma instância de {@link BairroDAO}.
     * <p>
     * A implementação retornada é {@link BairroDAOImpl}, com a conexão obtida do {@link ConnectionService}.
     * </p>
     *
     * @return Uma instância de {@link BairroDAO}.
     */
    public static BairroDAO createBairroDAO() {
        return new BairroDAOImpl(ConnectionService.getConnection());
    }

    /**
     * Cria e retorna uma instância de {@link PessoaDAO}.
     * <p>
     * A implementação retornada é {@link PessoaDAOImpl}, com a conexão obtida do {@link ConnectionService}.
     * </p>
     *
     * @return Uma instância de {@link PessoaDAO}.
     */
    public static PessoaDAO createPessoaDAO() {
        return new PessoaDAOImpl(ConnectionService.getConnection());
    }

    /**
     * Cria e retorna uma instância de {@link EnderecoDAO}.
     * <p>
     * A implementação retornada é {@link EnderecoDAOImpl}, com a conexão obtida do {@link ConnectionService}.
     * </p>
     *
     * @return Uma instância de {@link EnderecoDAO}.
     */
    public static EnderecoDAO createEnderecoDAO() {
        return new EnderecoDAOImpl(ConnectionService.getConnection());
    }

}