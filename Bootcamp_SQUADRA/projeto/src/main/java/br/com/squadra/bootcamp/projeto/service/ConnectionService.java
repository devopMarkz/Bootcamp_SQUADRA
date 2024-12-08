package br.com.squadra.bootcamp.projeto.service;

import br.com.squadra.bootcamp.projeto.exception.DbException;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe responsável pela gestão da conexão com o banco de dados.
 * Oferece métodos para estabelecer a conexão, fechar a conexão e fechar o ResultSet.
 */
public class ConnectionService {

    private static Connection connection = null;

    /**
     * Estabelece uma conexão com o banco de dados utilizando as propriedades configuradas no arquivo {@code application.properties}.
     * Caso já exista uma conexão ativa, retorna a mesma.
     *
     * @return a conexão com o banco de dados.
     * @throws DbException se houver falha ao estabelecer a conexão.
     */
    public static Connection getConnection() {
        if(connection == null) {
            try {
                String dbUrl = loadProperties().getProperty("dburl");
                String username = loadProperties().getProperty("username");
                String password = loadProperties().getProperty("password");
                connection = DriverManager.getConnection(dbUrl, username, password);
                return connection;
            } catch (SQLException e) {
                throw new DbException("Erro ao estabelecer conexão com banco de dados. Caused by: " + e.getMessage());
            }
        } else return connection;
    }

    /**
     * Fecha a conexão ativa com o banco de dados.
     *
     * @throws DbException se houver falha ao fechar a conexão.
     */
    public static void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DbException("Erro ao fechar conexão. Caused by: " + e.getMessage());
            }
        }
    }

    /**
     * Fecha o {@link ResultSet} passado como parâmetro.
     *
     * @param rs o {@code ResultSet} a ser fechado.
     * @throws DbException se houver falha ao fechar o {@code ResultSet}.
     */
    public static void closeResultSet(ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException("Erro ao fechar ResultSet. Caused By: " + e.getMessage());
            }
        }
    }

    /**
     * Carrega as propriedades de configuração para a conexão com o banco de dados a partir do arquivo {@code application.properties}.
     *
     * @return um objeto {@link Properties} contendo as propriedades carregadas.
     * @throws DbException se houver falha ao carregar as propriedades.
     */
    private static Properties loadProperties() {
        try (FileInputStream fr = new FileInputStream("./src/main/resources/application.properties")){
            Properties properties = new Properties();
            properties.load(fr);
            return properties;
        } catch (Exception e) {
            throw new DbException("Erro ao extrair propriedades para a conexão. Caused by: " + e.getMessage());
        }
    }
}
