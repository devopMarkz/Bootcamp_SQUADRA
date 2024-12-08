package br.com.squadra.bootcamp.projeto.model.dao.impl;

import br.com.squadra.bootcamp.projeto.exception.DbException;
import br.com.squadra.bootcamp.projeto.model.dao.PessoaDAO;
import br.com.squadra.bootcamp.projeto.model.entities.Pessoa;
import com.sun.source.tree.ReturnTree;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link PessoaDAO}, responsável pelas operações relacionadas à entidade Pessoa.
 */
public class PessoaDAOImpl implements PessoaDAO {
    private Connection connection;

    /**
     * Construtor que inicializa a conexão com o banco de dados.
     *
     * @param connection Conexão com o banco de dados.
     */
    public PessoaDAOImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * Busca uma pessoa pelo código.
     *
     * @param codigoPessoa Código da pessoa a ser buscada.
     * @return Objeto {@link Pessoa} correspondente ao código informado ou {@code null} se não encontrado.
     * @throws DbException Se ocorrer um erro ao buscar a pessoa.
     */
    @Override
    public Pessoa findByCodigoPessoa(Long codigoPessoa) {
        Pessoa pessoa = null;
        String sql = "SELECT * FROM TB_PESSOA WHERE CODIGO_PESSOA = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, codigoPessoa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pessoa = instancePessoa(rs, true);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar Pessoa por código: " + e.getMessage(), e);
        }
        return pessoa;
    }

    /**
     * Busca pessoas com base em filtros opcionais.
     *
     * @param codigoPessoa Código da pessoa (opcional).
     * @param login        Login da pessoa (opcional).
     * @param status       Status da pessoa (opcional).
     * @return Lista de pessoas que atendem aos filtros informados.
     * @throws DbException Se ocorrer um erro ao buscar as pessoas.
     */
    @Override
    public List<Pessoa> findByFilters(Optional<Long> codigoPessoa, Optional<String> login, Optional<Integer> status) {
        List<Pessoa> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM TB_PESSOA WHERE 1=1");
        if (codigoPessoa.isPresent()) sql.append(" AND CODIGO_PESSOA = ?");
        if (login.isPresent()) sql.append(" AND UPPER(LOGIN) = ?");
        if (status.isPresent()) sql.append(" AND STATUS = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (codigoPessoa.isPresent()) stmt.setLong(index++, codigoPessoa.get());
            if (login.isPresent()) stmt.setString(index++, login.get().toUpperCase());
            if (status.isPresent()) stmt.setInt(index++, status.get());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(instancePessoa(rs, false));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar Pessoas com filtros: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Busca todas as pessoas cadastradas no banco.
     *
     * @return Lista de todas as pessoas.
     * @throws DbException Se ocorrer um erro ao buscar as pessoas.
     */
    @Override
    public List<Pessoa> findAll() {
        List<Pessoa> lista = new ArrayList<>();
        String sql = "SELECT * FROM TB_PESSOA";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(instancePessoa(rs, false));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar todas as Pessoas: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Insere uma nova pessoa no banco de dados.
     *
     * @param pessoa Objeto {@link Pessoa} contendo os dados da pessoa a ser inserida.
     * @return Objeto {@link Pessoa} atualizado com o código gerado.
     * @throws DbException Se ocorrer um erro ao inserir a pessoa.
     */
    @Override
    public Pessoa insert(Pessoa pessoa) {
        String sql = "INSERT INTO TB_PESSOA (CODIGO_PESSOA, NOME, SOBRENOME, IDADE, LOGIN, SENHA, STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            Long codigoPessoa = getNextCodigoPessoa();
            stmt.setLong(1, codigoPessoa);
            stmt.setString(2, pessoa.getNome());
            stmt.setString(3, pessoa.getSobrenome());
            stmt.setInt(4, pessoa.getIdade());
            stmt.setString(5, pessoa.getLogin());
            stmt.setString(6, pessoa.getSenha());
            stmt.setInt(7, pessoa.getStatus());
            stmt.executeUpdate();
            pessoa.setCodigoPessoa(codigoPessoa);
        } catch (SQLException e) {
            throw new DbException("Erro ao inserir Pessoa: " + e.getMessage(), e);
        }
        return pessoa;
    }

    /**
     * Atualiza os dados de uma pessoa existente.
     *
     * @param pessoa Objeto {@link Pessoa} com os dados atualizados.
     * @return Pessoa atualizada.
     * @throws DbException Se ocorrer um erro ao atualizar a pessoa.
     */
    @Override
    public Pessoa update(Pessoa pessoa) {
        String sql = "UPDATE TB_PESSOA SET NOME = ?, SOBRENOME = ?, IDADE = ?, LOGIN = ?, SENHA = ?, STATUS = ? WHERE CODIGO_PESSOA = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getSobrenome());
            stmt.setInt(3, pessoa.getIdade());
            stmt.setString(4, pessoa.getLogin());
            stmt.setString(5, pessoa.getSenha());
            stmt.setInt(6, pessoa.getStatus());
            stmt.setLong(7, pessoa.getCodigoPessoa());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao atualizar Pessoa: " + e.getMessage(), e);
        }
        return pessoa;
    }

    /**
     * Busca uma pessoa pelo login.
     *
     * @param login Login da pessoa.
     * @return Objeto {@link Pessoa} correspondente ao login informado ou {@code null} se não encontrado.
     * @throws DbException Se ocorrer um erro ao buscar a pessoa.
     */
    @Override
    public Pessoa findByLogin(String login) {
        String sql = "SELECT * FROM TB_PESSOA WHERE LOGIN = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return instancePessoa(rs, false);
            } else return null;
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar pessoa por login", e);
        }
    }

    /**
     * Instancia um objeto {@link Pessoa} a partir de um {@link ResultSet}.
     *
     * @param rs               ResultSet contendo os dados da pessoa.
     * @param includeEnderecos Indica se os endereços associados devem ser incluídos.
     * @return Objeto {@link Pessoa} instanciado.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private Pessoa instancePessoa(ResultSet rs, boolean includeEnderecos) throws SQLException {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigoPessoa(rs.getLong("CODIGO_PESSOA"));
        pessoa.setNome(rs.getString("NOME"));
        pessoa.setSobrenome(rs.getString("SOBRENOME"));
        pessoa.setIdade(rs.getInt("IDADE"));
        pessoa.setLogin(rs.getString("LOGIN"));
        pessoa.setSenha(rs.getString("SENHA"));
        pessoa.setStatus(rs.getInt("STATUS"));

        if (includeEnderecos) {
            pessoa.setEnderecos(new EnderecoDAOImpl(connection).findByCodigoPessoa(pessoa.getCodigoPessoa()));
        }

        return pessoa;
    }

    /**
     * Obtém o próximo código disponível para uma pessoa usando uma sequência.
     *
     * @return Próximo código de pessoa.
     * @throws DbException Se ocorrer um erro ao obter o valor da sequência.
     */
    private Long getNextCodigoPessoa() {
        String sql = "SELECT SEQUENCE_PESSOA.NEXTVAL FROM DUAL";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new DbException("Erro ao obter próximo código de Pessoa.");
        } catch (SQLException e) {
            throw new DbException("Erro ao obter próximo valor da sequence: " + e.getMessage(), e);
        }
    }
}
