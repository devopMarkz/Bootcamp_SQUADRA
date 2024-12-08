package br.com.squadra.bootcamp.projeto.model.dao.impl;

import br.com.squadra.bootcamp.projeto.exception.DbException;
import br.com.squadra.bootcamp.projeto.model.dao.MunicipioDAO;
import br.com.squadra.bootcamp.projeto.dto.MunicipioDTO;
import br.com.squadra.bootcamp.projeto.model.entities.Municipio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface {@link MunicipioDAO}, responsável pelas operações relacionadas à entidade Município.
 */
public class MunicipioDAOImpl implements MunicipioDAO {

    private Connection connection;

    /**
     * Construtor que inicializa a conexão com o banco de dados.
     *
     * @param connection Conexão com o banco de dados.
     */
    public MunicipioDAOImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * Busca um município pelo código.
     *
     * @param codigoMunicipio Código do município a ser buscado.
     * @return Objeto {@link Municipio} correspondente ao código informado ou {@code null} se não encontrado.
     * @throws DbException Se ocorrer um erro ao buscar o município.
     */
    @Override
    public Municipio findByCodigoMunicipio(Long codigoMunicipio) {
        Municipio municipio = null;
        String sql = "SELECT * FROM TB_MUNICIPIO WHERE CODIGO_MUNICIPIO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, codigoMunicipio);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    municipio = instanceMunicipio(rs);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar Município pelo código: " + e.getMessage(), e);
        }

        return municipio;
    }

    /**
     * Busca municípios pelo código da UF.
     *
     * @param codigoUF Código da UF.
     * @return Lista de municípios associados ao código da UF.
     * @throws DbException Se ocorrer um erro ao buscar os municípios.
     */
    @Override
    public List<Municipio> findByCodigoUF(Long codigoUF) {
        List<Municipio> municipioList = new ArrayList<>();
        String sql = "SELECT * FROM TB_MUNICIPIO WHERE CODIGO_UF = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, codigoUF);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    municipioList.add(instanceMunicipio(rs));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar Município pelo código UF: " + e.getMessage(), e);
        }

        return municipioList;
    }

    /**
     * Busca todos os municípios cadastrados no banco.
     *
     * @return Lista de todos os municípios.
     * @throws DbException Se ocorrer um erro ao buscar os municípios.
     */
    @Override
    public List<Municipio> findAll() {
        List<Municipio> municipioList = new ArrayList<>();
        String sql = "SELECT * FROM TB_MUNICIPIO ORDER BY CODIGO_MUNICIPIO DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    municipioList.add(instanceMunicipio(rs));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar todos os Municípios: " + e.getMessage(), e);
        }

        return municipioList;
    }

    /**
     * Busca municípios pelo status.
     *
     * @param status Status do município.
     * @return Lista de municípios com o status informado.
     * @throws DbException Se ocorrer um erro ao buscar os municípios.
     */
    @Override
    public List<Municipio> findByStatus(int status) {
        List<Municipio> municipioList = new ArrayList<>();
        String sql = "SELECT * FROM TB_MUNICIPIO WHERE STATUS = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    municipioList.add(instanceMunicipio(rs));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar Município pelo status: " + e.getMessage(), e);
        }

        return municipioList;
    }

    /**
     * Busca um município pelo nome.
     *
     * @param nomeMunicipio Nome do município.
     * @return Objeto {@link Municipio} correspondente ao nome informado ou {@code null} se não encontrado.
     * @throws DbException Se ocorrer um erro ao buscar o município.
     */
    @Override
    public Municipio findByNome(String nomeMunicipio) {
        Municipio municipio = null;
        String sql = "SELECT * FROM TB_MUNICIPIO WHERE NOME = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeMunicipio.toUpperCase());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    municipio = instanceMunicipio(rs);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar Município pelo nome: " + e.getMessage(), e);
        }

        return municipio;
    }

    /**
     * Insere um novo município no banco de dados.
     *
     * @param municipioDTO Objeto {@link MunicipioDTO} com os dados do município a ser inserido.
     * @return Objeto {@link MunicipioDTO} atualizado com o código gerado.
     * @throws DbException Se ocorrer um erro ao inserir o município.
     */
    @Override
    public MunicipioDTO insert(MunicipioDTO municipioDTO) {
        Long codigoMunicipio = getNextCodigoMunicipio();

        String sql = "INSERT INTO TB_MUNICIPIO (CODIGO_MUNICIPIO, CODIGO_UF, NOME, STATUS) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, codigoMunicipio);
            stmt.setLong(2, municipioDTO.getCodigoUF());
            stmt.setString(3, municipioDTO.getNome().toUpperCase());
            stmt.setInt(4, municipioDTO.getStatus());

            stmt.executeUpdate();

            municipioDTO.setCodigoMunicipio(codigoMunicipio);

        } catch (SQLException e) {
            throw new DbException("Erro ao salvar o Município: " + e.getMessage(), e);
        }

        return municipioDTO;
    }

    /**
     * Atualiza os dados de um município existente.
     *
     * @param municipioDTO Objeto {@link MunicipioDTO} com os dados atualizados do município.
     * @return Município atualizado.
     * @throws DbException Se ocorrer um erro ao atualizar o município.
     */
    @Override
    public MunicipioDTO update(MunicipioDTO municipioDTO) {
        String sql = "UPDATE TB_MUNICIPIO SET CODIGO_UF = ?, NOME = ?, STATUS = ? WHERE CODIGO_MUNICIPIO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, municipioDTO.getCodigoUF());
            stmt.setString(2, municipioDTO.getNome().toUpperCase());
            stmt.setInt(3, municipioDTO.getStatus());
            stmt.setLong(4, municipioDTO.getCodigoMunicipio());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DbException("Erro ao atualizar Município de código " + municipioDTO.getCodigoMunicipio() + ": " + e.getMessage(), e);
        }

        return municipioDTO;
    }

    /**
     * Obtém o próximo código disponível para um município usando uma sequência.
     *
     * @return Próximo código de município.
     * @throws DbException Se ocorrer um erro ao obter o valor da sequência.
     */
    private Long getNextCodigoMunicipio() {
        String sql = "SELECT SEQUENCE_MUNICIPIO.NEXTVAL FROM DUAL";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

            throw new DbException("Erro ao obter o próximo código de Município da sequence.");

        } catch (SQLException e) {
            throw new DbException("Erro ao obter o próximo valor da sequence: " + e.getMessage(), e);
        }
    }

    /**
     * Instancia um objeto {@link Municipio} a partir de um {@link ResultSet}.
     *
     * @param rs ResultSet contendo os dados do município.
     * @return Objeto {@link Municipio} instanciado.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private Municipio instanceMunicipio(ResultSet rs) throws SQLException {
        Municipio municipio = new Municipio();
        municipio.setCodigoMunicipio(rs.getLong("CODIGO_MUNICIPIO"));
        municipio.setNome(rs.getString("NOME"));
        municipio.setStatus(rs.getInt("STATUS"));
        municipio.setUf(new UfDAOImpl(connection).findById(rs.getLong("CODIGO_UF")));
        return municipio;
    }

    /**
     * Busca municípios com base em filtros opcionais.
     *
     * @param codigoMunicipio Código do município (opcional).
     * @param codigoUF        Código da UF (opcional).
     * @param nome            Nome do município (opcional).
     * @param status          Status do município (opcional).
     * @return Lista de municípios que atendem aos filtros informados.
     * @throws DbException Se ocorrer um erro ao buscar os municípios.
     */
    @Override
    public List<Municipio> findByFilters(Optional<Long> codigoMunicipio, Optional<Long> codigoUF, Optional<String> nome, Optional<Integer> status) {
        List<Municipio> municipioList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM TB_MUNICIPIO WHERE 1=1");

        if (codigoMunicipio.isPresent()) {
            sql.append(" AND CODIGO_MUNICIPIO = ?");
        }
        if (codigoUF.isPresent()) {
            sql.append(" AND CODIGO_UF = ?");
        }
        if (nome.isPresent()) {
            sql.append(" AND NOME = ?");
        }
        if (status.isPresent()) {
            sql.append(" AND STATUS = ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;

            if (codigoMunicipio.isPresent()) {
                stmt.setLong(index++, codigoMunicipio.get());
            }
            if (codigoUF.isPresent()) {
                stmt.setLong(index++, codigoUF.get());
            }
            if (nome.isPresent()) {
                stmt.setString(index++, nome.get().toUpperCase());
            }
            if (status.isPresent()) {
                stmt.setInt(index++, status.get());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    municipioList.add(instanceMunicipio(rs));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar Municípios com filtros: " + e.getMessage(), e);
        }

        return municipioList;
    }
}
