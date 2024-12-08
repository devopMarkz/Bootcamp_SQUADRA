package br.com.squadra.bootcamp.projeto.model.dao.impl;

import br.com.squadra.bootcamp.projeto.exception.DbException;
import br.com.squadra.bootcamp.projeto.model.dao.BairroDAO;
import br.com.squadra.bootcamp.projeto.dto.BairroDTO;
import br.com.squadra.bootcamp.projeto.model.entities.Bairro;
import br.com.squadra.bootcamp.projeto.model.entities.Municipio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface BairroDAO responsável por operações relacionadas à entidade Bairro.
 */
public class BairroDAOImpl implements BairroDAO {

    private Connection connection;

    /**
     * Construtor da classe BairroDAOImpl.
     *
     * @param connection Conexão com o banco de dados.
     */
    public BairroDAOImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * Busca um bairro pelo código.
     *
     * @param codigoBairro Código do bairro a ser buscado.
     * @return Bairro encontrado ou null se não existir.
     * @throws DbException Se ocorrer um erro na consulta ao banco de dados.
     */
    @Override
    public Bairro findByCodigoBairro(Long codigoBairro) {
        Bairro bairro = null;
        String sql = "SELECT * FROM TB_BAIRRO WHERE CODIGO_BAIRRO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, codigoBairro);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    bairro = instanceBairro(rs);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar Bairro pelo código: " + e.getMessage(), e);
        }

        return bairro;
    }

    /**
     * Busca bairros pertencentes a um município específico.
     *
     * @param codigoMunicipio Código do município.
     * @return Lista de bairros pertencentes ao município.
     * @throws DbException Se ocorrer um erro na consulta ao banco de dados.
     */
    @Override
    public List<Bairro> findByCodigoMunicipio(Long codigoMunicipio) {
        List<Bairro> bairroList = new ArrayList<>();
        String sql = "SELECT * FROM TB_BAIRRO WHERE CODIGO_MUNICIPIO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, codigoMunicipio);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bairroList.add(instanceBairro(rs));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar Bairros pelo código do município: " + e.getMessage(), e);
        }

        return bairroList;
    }

    /**
     * Retorna todos os bairros cadastrados.
     *
     * @return Lista de bairros cadastrados.
     * @throws DbException Se ocorrer um erro na consulta ao banco de dados.
     */
    @Override
    public List<Bairro> findAll() {
        List<Bairro> bairroList = new ArrayList<>();
        String sql = "SELECT * FROM TB_BAIRRO ORDER BY CODIGO_BAIRRO DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bairroList.add(instanceBairro(rs));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar todos os Bairros: " + e.getMessage(), e);
        }

        return bairroList;
    }

    /**
     * Busca bairros pelo status.
     *
     * @param status Status do bairro.
     * @return Lista de bairros com o status especificado.
     * @throws DbException Se ocorrer um erro na consulta ao banco de dados.
     */
    @Override
    public List<Bairro> findByStatus(int status) {
        List<Bairro> bairroList = new ArrayList<>();
        String sql = "SELECT * FROM TB_BAIRRO WHERE STATUS = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bairroList.add(instanceBairro(rs));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar Bairros pelo status: " + e.getMessage(), e);
        }

        return bairroList;
    }

    /**
     * Insere um novo bairro no banco de dados.
     *
     * @param bairroDTO Dados do bairro a ser inserido.
     * @return Bairro inserido com seu código gerado.
     * @throws DbException Se ocorrer um erro na inserção.
     */
    @Override
    public BairroDTO insert(BairroDTO bairroDTO) {
        Long codigoBairro = getNextCodigoBairro();
        String sql = "INSERT INTO TB_BAIRRO (CODIGO_BAIRRO, CODIGO_MUNICIPIO, NOME, STATUS) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, codigoBairro);
            stmt.setLong(2, bairroDTO.getCodigoMunicipio());
            stmt.setString(3, bairroDTO.getNome().toUpperCase());
            stmt.setInt(4, bairroDTO.getStatus());

            stmt.executeUpdate();
            bairroDTO.setCodigoBairro(codigoBairro);
        } catch (SQLException e) {
            throw new DbException("Erro ao salvar o Bairro: " + e.getMessage(), e);
        }

        return bairroDTO;
    }

    /**
     * Atualiza os dados de um bairro existente.
     *
     * @param bairroDTO Dados atualizados do bairro.
     * @return Bairro atualizado.
     * @throws DbException Se ocorrer um erro na atualização.
     */
    @Override
    public BairroDTO update(BairroDTO bairroDTO) {
        String sql = "UPDATE TB_BAIRRO SET CODIGO_MUNICIPIO = ?, NOME = ?, STATUS = ? WHERE CODIGO_BAIRRO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, bairroDTO.getCodigoMunicipio());
            stmt.setString(2, bairroDTO.getNome().toUpperCase());
            stmt.setInt(3, bairroDTO.getStatus());
            stmt.setLong(4, bairroDTO.getCodigoBairro());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao atualizar Bairro de código " + bairroDTO.getCodigoBairro() + ": " + e.getMessage(), e);
        }

        return bairroDTO;
    }

    /**
     * Obtém o próximo código disponível para inserção de bairro.
     *
     * @return Próximo código disponível.
     * @throws DbException Se ocorrer um erro ao acessar a sequence.
     */
    private Long getNextCodigoBairro() {
        String sql = "SELECT SEQUENCE_BAIRRO.NEXTVAL FROM DUAL";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

            throw new DbException("Erro ao obter o próximo código de Bairro da sequence.");
        } catch (SQLException e) {
            throw new DbException("Erro ao obter o próximo valor da sequence: " + e.getMessage(), e);
        }
    }

    /**
     * Instancia um objeto Bairro a partir de um ResultSet.
     *
     * @param rs ResultSet contendo os dados do bairro.
     * @return Objeto Bairro instanciado.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private Bairro instanceBairro(ResultSet rs) throws SQLException {
        Bairro bairro = new Bairro();
        bairro.setCodigoBairro(rs.getLong("CODIGO_BAIRRO"));
        bairro.setNome(rs.getString("NOME"));
        bairro.setStatus(rs.getInt("STATUS"));
        bairro.setMunicipio(new MunicipioDAOImpl(connection).findByCodigoMunicipio(rs.getLong("CODIGO_MUNICIPIO")));
        return bairro;
    }

    /**
     * Busca bairros com base em filtros opcionais.
     *
     * @param codigoBairro   Código do bairro (opcional).
     * @param codigoMunicipio Código do município (opcional).
     * @param nome            Nome do bairro (opcional).
     * @param status          Status do bairro (opcional).
     * @return Lista de bairros que atendem aos filtros.
     * @throws DbException Se ocorrer um erro na consulta ao banco de dados.
     */
    @Override
    public List<Bairro> findByFilters(Optional<Long> codigoBairro, Optional<Long> codigoMunicipio, Optional<String> nome, Optional<Integer> status) {
        List<Bairro> bairroList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM TB_BAIRRO WHERE 1=1");

        if (codigoBairro.isPresent()) {
            sql.append(" AND CODIGO_BAIRRO = ?");
        }
        if (codigoMunicipio.isPresent()) {
            sql.append(" AND CODIGO_MUNICIPIO = ?");
        }
        if (nome.isPresent()) {
            sql.append(" AND NOME = ?");
        }
        if (status.isPresent()) {
            sql.append(" AND STATUS = ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;

            if (codigoBairro.isPresent()) {
                stmt.setLong(index++, codigoBairro.get());
            }
            if (codigoMunicipio.isPresent()) {
                stmt.setLong(index++, codigoMunicipio.get());
            }
            if (nome.isPresent()) {
                stmt.setString(index++, nome.get().toUpperCase());
            }
            if (status.isPresent()) {
                stmt.setInt(index++, status.get());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bairroList.add(instanceBairro(rs));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar Bairros com filtros: " + e.getMessage(), e);
        }

        return bairroList;
    }

    /**
     * Busca um bairro pelo nome.
     *
     * @param nome Nome do bairro.
     * @return Bairro encontrado ou null se não existir.
     * @throws DbException Se ocorrer um erro na consulta ao banco de dados.
     */
    @Override
    public Bairro findByNome(String nome) {
        String sql = "SELECT * FROM TB_BAIRRO WHERE NOME = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Bairro bairro = new Bairro();
                    bairro.setCodigoBairro(rs.getLong("CODIGO_BAIRRO"));
                    bairro.setNome(rs.getString("NOME"));
                    bairro.setStatus(rs.getInt("STATUS"));

                    Municipio municipio = new Municipio();
                    municipio.setCodigoMunicipio(rs.getLong("CODIGO_MUNICIPIO"));
                    bairro.setMunicipio(municipio);

                    return bairro;
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar bairro por nome. Motivo: " + e.getMessage(), e);
        }
        return null;
    }
}
