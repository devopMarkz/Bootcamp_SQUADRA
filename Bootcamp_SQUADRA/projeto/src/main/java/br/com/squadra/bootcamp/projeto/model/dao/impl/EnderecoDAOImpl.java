package br.com.squadra.bootcamp.projeto.model.dao.impl;

import br.com.squadra.bootcamp.projeto.exception.DbException;
import br.com.squadra.bootcamp.projeto.model.dao.EnderecoDAO;
import br.com.squadra.bootcamp.projeto.model.entities.Endereco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface {@link EnderecoDAO} responsável pelas operações relacionadas à entidade Endereço.
 */
public class EnderecoDAOImpl implements EnderecoDAO {

    private Connection connection;

    /**
     * Construtor para inicializar a conexão com o banco de dados.
     *
     * @param connection Conexão com o banco de dados.
     */
    public EnderecoDAOImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * Busca todos os endereços associados a uma pessoa específica.
     *
     * @param codigoPessoa Código da pessoa.
     * @return Lista de endereços pertencentes à pessoa especificada.
     * @throws DbException Se ocorrer um erro ao buscar os dados no banco.
     */
    @Override
    public List<Endereco> findByCodigoPessoa(Long codigoPessoa) {
        List<Endereco> lista = new ArrayList<>();
        String sql = "SELECT * FROM TB_ENDERECO WHERE CODIGO_PESSOA = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, codigoPessoa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(instanceEndereco(rs));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar Endereços por Código Pessoa: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Insere um novo endereço no banco de dados.
     *
     * @param endereco Entidade do tipo {@link Endereco} a ser inserida.
     * @return Endereço inserido com o código gerado.
     * @throws DbException Se ocorrer um erro ao inserir o endereço.
     */
    @Override
    public Endereco insert(Endereco endereco) {
        String sql = "INSERT INTO TB_ENDERECO (CODIGO_ENDERECO, CODIGO_PESSOA, CODIGO_BAIRRO, NOME_RUA, NUMERO, COMPLEMENTO, CEP) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            Long codigoEndereco = getNextCodigoEndereco();
            stmt.setLong(1, codigoEndereco);
            stmt.setLong(2, endereco.getCodigoPessoa());
            stmt.setLong(3, endereco.getCodigoBairro());
            stmt.setString(4, endereco.getNomeRua());
            stmt.setString(5, endereco.getNumero());
            stmt.setString(6, endereco.getComplemento());
            stmt.setString(7, endereco.getCep());
            stmt.executeUpdate();
            endereco.setCodigoEndereco(codigoEndereco);
        } catch (SQLException e) {
            throw new DbException("Erro ao inserir Endereço: " + e.getMessage(), e);
        }
        return endereco;
    }

    /**
     * Atualiza os dados de um endereço existente no banco de dados.
     *
     * @param endereco Entidade do tipo {@link Endereco} com os dados atualizados.
     * @return Endereço atualizado.
     * @throws DbException Se ocorrer um erro ao atualizar o endereço.
     */
    @Override
    public Endereco update(Endereco endereco) {
        String sql = "UPDATE TB_ENDERECO SET CODIGO_BAIRRO = ?, NOME_RUA = ?, NUMERO = ?, COMPLEMENTO = ?, CEP = ? WHERE CODIGO_ENDERECO = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, endereco.getCodigoBairro());
            stmt.setString(2, endereco.getNomeRua());
            stmt.setString(3, endereco.getNumero());
            stmt.setString(4, endereco.getComplemento());
            stmt.setString(5, endereco.getCep());
            stmt.setLong(6, endereco.getCodigoEndereco());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao atualizar Endereço: " + e.getMessage(), e);
        }
        return endereco;
    }

    /**
     * Exclui todos os endereços associados a uma pessoa pelo código da pessoa.
     *
     * @param codigoPessoa Código da pessoa.
     * @throws DbException Se ocorrer um erro ao excluir os endereços.
     */
    @Override
    public void deleteByCodigoPessoa(Long codigoPessoa) {
        String sql = "DELETE FROM TB_ENDERECO WHERE CODIGO_PESSOA = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, codigoPessoa);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao deletar Endereços por Código Pessoa: " + e.getMessage(), e);
        }
    }

    /**
     * Exclui um endereço pelo código do endereço.
     *
     * @param codigoEndereco Código do endereço.
     * @throws DbException Se ocorrer um erro ao excluir o endereço.
     */
    @Override
    public void deleteByCodigoEndereco(Long codigoEndereco) {
        String sql = "DELETE FROM TB_ENDERECO WHERE CODIGO_ENDERECO = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, codigoEndereco);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao deletar Endereço: " + e.getMessage(), e);
        }
    }

    /**
     * Instancia um objeto {@link Endereco} a partir de um {@link ResultSet}.
     *
     * @param rs ResultSet contendo os dados do endereço.
     * @return Objeto {@link Endereco} instanciado.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private Endereco instanceEndereco(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setCodigoEndereco(rs.getLong("CODIGO_ENDERECO"));
        endereco.setCodigoPessoa(rs.getLong("CODIGO_PESSOA"));
        endereco.setCodigoBairro(rs.getLong("CODIGO_BAIRRO"));
        endereco.setNomeRua(rs.getString("NOME_RUA"));
        endereco.setNumero(rs.getString("NUMERO"));
        endereco.setComplemento(rs.getString("COMPLEMENTO"));
        endereco.setCep(rs.getString("CEP"));
        return endereco;
    }

    /**
     * Obtém o próximo valor da sequência para o código do endereço.
     *
     * @return Próximo código de endereço disponível.
     * @throws DbException Se ocorrer um erro ao acessar a sequência.
     */
    private Long getNextCodigoEndereco() {
        String sql = "SELECT SEQUENCE_ENDERECO.NEXTVAL FROM DUAL";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new DbException("Erro ao obter próximo código de Endereço.");
        } catch (SQLException e) {
            throw new DbException("Erro ao obter próximo valor da sequence: " + e.getMessage(), e);
        }
    }
}
