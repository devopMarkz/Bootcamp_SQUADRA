package br.com.squadra.bootcamp.projeto.model.dao.impl;

import br.com.squadra.bootcamp.projeto.exception.DbException;
import br.com.squadra.bootcamp.projeto.model.dao.UfDAO;
import br.com.squadra.bootcamp.projeto.model.entities.Uf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface {@link UfDAO}, responsável por gerenciar as operações relacionadas à entidade UF
 * no banco de dados.
 */
public class UfDAOImpl implements UfDAO {

    private Connection connection;

    /**
     * Construtor que inicializa a conexão com o banco de dados.
     *
     * @param connection Conexão com o banco de dados.
     */
    public UfDAOImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retorna todas as UFs do banco de dados ordenadas pelo código UF em ordem decrescente.
     *
     * @return Lista de UFs.
     * @throws DbException Se ocorrer um erro ao buscar as UFs.
     */
    @Override
    public List<Uf> findAll() {
        List<Uf> ufList = new ArrayList<>();
        String sql = "SELECT * FROM TB_UF ORDER BY CODIGO_UF DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Uf uf = instanceUf(rs);
                    ufList.add(uf);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar UF pelo nome: " + e.getMessage(), e);
        }

        return ufList;
    }

    /**
     * Busca uma UF pelo nome.
     *
     * @param nomeUF Nome da UF.
     * @return Objeto {@link Uf} correspondente ou {@code null} se não encontrado.
     * @throws DbException Se ocorrer um erro ao buscar a UF.
     */
    @Override
    public Uf findByNome(String nomeUF) {
        Uf uf = null;
        String sql = "SELECT * FROM TB_UF WHERE NOME = ? ORDER BY CODIGO_UF DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeUF);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    uf = instanceUf(rs);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar UF pelo nome: " + e.getMessage(), e);
        }

        return uf;
    }

    /**
     * Busca UFs pelo status.
     *
     * @param status Status da UF.
     * @return Lista de UFs correspondentes.
     * @throws DbException Se ocorrer um erro ao buscar as UFs.
     */
    @Override
    public List<Uf> findByStatus(int status) {
        List<Uf> ufList = new ArrayList<>();
        String sql = "SELECT * FROM TB_UF WHERE STATUS = ? ORDER BY CODIGO_UF DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Uf uf = instanceUf(rs);
                    ufList.add(uf);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar UF pelo status: " + e.getMessage(), e);
        }

        return ufList;
    }

    /**
     * Busca uma UF pela sigla.
     *
     * @param sigla Sigla da UF.
     * @return Objeto {@link Uf} correspondente ou {@code null} se não encontrado.
     * @throws DbException Se ocorrer um erro ao buscar a UF.
     */
    @Override
    public Uf findBySigla(String sigla) {
        Uf uf = null;
        String sql = "SELECT * FROM TB_UF WHERE SIGLA = ? ORDER BY CODIGO_UF DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sigla);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    uf = instanceUf(rs);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar UF pelo status: " + e.getMessage(), e);
        }
        return uf;
    }

    /**
     * Busca uma UF pelo ID.
     *
     * @param id ID da UF.
     * @return Objeto {@link Uf} correspondente ou {@code null} se não encontrado.
     * @throws DbException Se ocorrer um erro ao buscar a UF.
     */
    @Override
    public Uf findById(Long id) {
        Uf uf = null;
        String sql = "SELECT * FROM TB_UF WHERE CODIGO_UF = ? ORDER BY CODIGO_UF DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    uf = instanceUf(rs);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar UF pelo ID: " + e.getMessage(), e);
        }
        return uf;
    }

    /**
     * Busca uma UF com base em filtros opcionais.
     *
     * @param codigoUF Código da UF (opcional).
     * @param sigla    Sigla da UF (opcional).
     * @param nome     Nome da UF (opcional).
     * @param status   Status da UF (opcional).
     * @return Objeto {@link Uf} correspondente ou {@code null} se não encontrado.
     * @throws DbException Se ocorrer um erro ao buscar a UF.
     */
    @Override
    public Uf findUniqueByFilters(Long codigoUF, String sigla, String nome, Integer status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM TB_UF WHERE 1=1");

        if (codigoUF != null) {
            sql.append(" AND CODIGO_UF = ?");
        }
        if (sigla != null) {
            sql.append(" AND SIGLA = ?");
        }
        if (nome != null) {
            sql.append(" AND NOME = ?");
        }
        if (status != null) {
            sql.append(" AND STATUS = ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (codigoUF != null) stmt.setLong(index++, codigoUF);
            if (sigla != null) stmt.setString(index++, sigla.toUpperCase());
            if (nome != null) stmt.setString(index++, nome.toUpperCase());
            if (status != null) stmt.setInt(index++, status);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return instanceUf(rs);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar UF com filtros: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Insere uma nova UF no banco de dados.
     *
     * @param uf Objeto {@link Uf} a ser inserido.
     * @return Objeto {@link Uf} inserido com o código gerado.
     * @throws DbException Se ocorrer um erro ao inserir a UF.
     */
    @Override
    public Uf insert(Uf uf) {
        if (existsByNomeOrSigla(uf.getNome(), uf.getSigla())) {
            throw new DbException("Já existe uma UF com o mesmo nome ou sigla.");
        }

        Long codigoUf = getNextCodigoUf();

        String sql = "INSERT INTO TB_UF (CODIGO_UF, SIGLA, NOME, STATUS) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, codigoUf);
            stmt.setString(2, uf.getSigla().toUpperCase());
            stmt.setString(3, uf.getNome().toUpperCase());
            stmt.setInt(4, uf.getStatus());

            stmt.executeUpdate();

            uf.setCodigoUF(codigoUf);

        } catch (SQLException e) {
            throw new DbException("Erro ao salvar a UF: " + e.getMessage(), e);
        }

        return uf;
    }

    /**
     * Atualiza os dados de uma UF existente no banco de dados.
     *
     * @param uf Objeto {@link Uf} com os dados atualizados.
     * @return UF atualizada.
     * @throws DbException Se ocorrer um erro ao atualizar a UF.
     */
    @Override
    public Uf update(Uf uf) {
        String sql = "UPDATE TB_UF SET SIGLA = ?, NOME = ?, STATUS = ? WHERE CODIGO_UF = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uf.getSigla());
            stmt.setString(2, uf.getNome());
            stmt.setInt(3, uf.getStatus());
            stmt.setLong(4, uf.getCodigoUF());
            stmt.executeUpdate();
            return uf;
        } catch (SQLException e) {
            throw new DbException("Erro ao atualizar UF de código " + uf.getCodigoUF() + ". Causado por: " + e.getMessage(), e);
        }
    }

    /**
     * Método não implementado para deletar uma UF pelo ID.
     *
     * @param id ID da UF a ser deletada.
     */
    @Override
    public void deleteById(Long id) {
        // Método não implementado
    }

    /**
     * Instancia um objeto {@link Uf} a partir dos dados do {@link ResultSet}.
     *
     * @param rs ResultSet contendo os dados da UF.
     * @return Objeto {@link Uf} instanciado.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private Uf instanceUf(ResultSet rs) throws SQLException {
        Uf uf = new Uf();
        uf.setCodigoUF(rs.getLong("CODIGO_UF"));
        uf.setSigla(rs.getString("SIGLA"));
        uf.setNome(rs.getString("NOME"));
        uf.setStatus(rs.getInt("STATUS"));
        return uf;
    }

    /**
     * Verifica se já existe uma UF com o mesmo nome ou sigla no banco de dados.
     *
     * @param nome Nome da UF.
     * @param sigla Sigla da UF.
     * @return {@code true} se existir, {@code false} caso contrário.
     * @throws DbException Se ocorrer um erro ao verificar a existência.
     */
    private boolean existsByNomeOrSigla(String nome, String sigla) {
        String sql = "SELECT 1 FROM TB_UF WHERE NOME = ? OR SIGLA = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, sigla);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao verificar se a UF existe: " + e.getMessage(), e);
        }
    }

    /**
     * Obtém o próximo código de UF utilizando uma sequência do banco de dados.
     *
     * @return Próximo código disponível.
     * @throws DbException Se ocorrer um erro ao obter o valor da sequência.
     */
    private Long getNextCodigoUf() {
        String sql = "SELECT SEQUENCE_UF.NEXTVAL FROM DUAL";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

            throw new DbException("Erro ao obter o próximo código de UF da sequence.");

        } catch (SQLException e) {
            throw new DbException("Erro ao obter o próximo valor da sequence: " + e.getMessage(), e);
        }
    }
}

