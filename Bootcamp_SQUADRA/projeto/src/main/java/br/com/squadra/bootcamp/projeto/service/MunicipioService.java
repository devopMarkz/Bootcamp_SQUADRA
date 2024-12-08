package br.com.squadra.bootcamp.projeto.service;

import br.com.squadra.bootcamp.projeto.model.dao.DAOFactory;
import br.com.squadra.bootcamp.projeto.model.dao.MunicipioDAO;
import br.com.squadra.bootcamp.projeto.dto.MunicipioDTO;
import br.com.squadra.bootcamp.projeto.model.dao.UfDAO;
import br.com.squadra.bootcamp.projeto.model.entities.Municipio;
import br.com.squadra.bootcamp.projeto.model.entities.Uf;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas operações relacionadas aos municípios, incluindo inserção, atualização, consulta e validação de dados.
 */
@Service
public class MunicipioService {

    private MunicipioDAO municipioDAO = DAOFactory.createMunicipioDAO();
    private UfDAO ufDAO = DAOFactory.createUfDAO();

    /**
     * Insere um novo município no banco de dados.
     *
     * @param municipioDTO objeto com os dados do município a ser inserido.
     */
    public void insert(MunicipioDTO municipioDTO) {
        municipioDAO.insert(municipioDTO);
    }

    /**
     * Atualiza os dados de um município existente no banco de dados.
     *
     * @param municipioDTO objeto com os dados atualizados do município.
     */
    public void update(MunicipioDTO municipioDTO) {
        municipioDAO.update(municipioDTO);
    }

    /**
     * Retorna uma lista de todos os municípios cadastrados no banco de dados, ordenados por código de município de forma decrescente.
     *
     * @return lista de objetos {@link MunicipioDTO} representando os municípios encontrados.
     */
    public List<MunicipioDTO> findAll() {
        return municipioDAO.findAll()
                .stream()
                .map(municipio -> {
                    return new MunicipioDTO(
                            municipio.getCodigoMunicipio(),
                            municipio.getUf().getCodigoUF(),
                            municipio.getNome(),
                            municipio.getStatus());
                })
                .sorted((o1, o2) -> -o1.getCodigoMunicipio().compareTo(o2.getCodigoMunicipio()))
                .collect(Collectors.toList());
    }

    /**
     * Valida os dados de um município para inserção no banco de dados.
     *
     * @param municipioDTO objeto com os dados do município a ser validado.
     * @return um objeto {@link MessageErrorService} com a mensagem de erro, se houver, ou {@code null} se a validação for bem-sucedida.
     */
    public MessageErrorService validatePostMunicipio(MunicipioDTO municipioDTO) {
        if(municipioDTO.getCodigoUF() == null && municipioDTO.getNome() == null && municipioDTO.getStatus() == null){
            return new MessageErrorService("Não foi possível cadastrar município no banco de dados. Os campos codigoUF, nome e status precisam estar inclusos no corpo da requisição.", 404);
        }
        if (municipioDTO.getCodigoUF() == null) {
            return new MessageErrorService("Não foi possível cadastrar município no banco de dados. O campo codigoUF é obrigatório.", 404);
        }
        if (municipioDTO.getNome() == null || municipioDTO.getNome().isEmpty()) {
            return new MessageErrorService("Não foi possível cadastrar município no banco de dados. O campo nome é obrigatório.", 404);
        }
        if (municipioDTO.getStatus() == null) {
            return new MessageErrorService("Não foi possível cadastrar município no banco de dados. O campo status é obrigatório.", 404);
        }
        if (municipioDTO.getStatus() < 1 || municipioDTO.getStatus() > 2) {
            return new MessageErrorService("Não foi possível cadastrar município no banco de dados. O status precisa ser 1 ou 2.", 404);
        }
        Uf uf = ufDAO.findById(municipioDTO.getCodigoUF());
        if (uf == null) {
            return new MessageErrorService("Não foi possível cadastrar município no banco de dados. O codigoUF fornecido não existe.", 404);
        }
        if (municipioDAO.findByNome(municipioDTO.getNome()) != null) {
            return new MessageErrorService("Não foi possível cadastrar município no banco de dados. O município com o nome " + municipioDTO.getNome() + " já está cadastrado.", 404);
        }
        return null;
    }

    /**
     * Valida os dados de um município para atualização no banco de dados.
     *
     * @param municipioDTO objeto com os dados do município a ser validado.
     * @return um objeto {@link MessageErrorService} com a mensagem de erro, se houver, ou {@code null} se a validação for bem-sucedida.
     */
    public MessageErrorService validatePutMunicipio(MunicipioDTO municipioDTO) {
        if(municipioDTO.getCodigoMunicipio() == null && municipioDTO.getCodigoUF() == null && municipioDTO.getNome() == null && municipioDTO.getStatus() == null) {
            return new MessageErrorService("Não foi possível alterar município no banco de dados. Os campos codigoMunicipio, codigoUF, nome e status precisam estar inclusos no corpo da requisição.", 404);
        }
        if(municipioDTO.getCodigoMunicipio() == null){
            return new MessageErrorService("Não foi possível alterar município no banco de dados. O campo codigoMunicipio é obrigatório.", 404);
        }
        if (municipioDTO.getCodigoUF() == null) {
            return new MessageErrorService("Não foi possível alterar município no banco de dados. O campo codigoUF é obrigatório.", 404);
        }
        if (municipioDTO.getNome() == null || municipioDTO.getNome().isEmpty()) {
            return new MessageErrorService("Não foi possível alterar município no banco de dados. O campo nome é obrigatório.", 404);
        }
        if (municipioDTO.getStatus() == null) {
            return new MessageErrorService("Não foi possível alterar município no banco de dados. O campo status é obrigatório.", 404);
        }
        if (municipioDTO.getStatus() < 1 || municipioDTO.getStatus() > 2) {
            return new MessageErrorService("Não foi possível alterar município no banco de dados. O status precisa ser 1 ou 2.", 404);
        }
        if(municipioDAO.findByCodigoMunicipio(municipioDTO.getCodigoMunicipio()) == null){
            return new MessageErrorService("Não foi possível alterar município no banco de dados. O codigoMunicipio fornecido não existe.", 404);
        }
        Uf uf = ufDAO.findById(municipioDTO.getCodigoUF());
        if (uf == null) {
            return new MessageErrorService("Não foi possível alterar município no banco de dados. O codigoUF fornecido não existe.", 404);
        }
        Municipio existingMunicipio = municipioDAO.findByNome(municipioDTO.getNome());
        if (existingMunicipio != null && !existingMunicipio.getCodigoMunicipio().equals(municipioDTO.getCodigoMunicipio())) {
            return new MessageErrorService("Não foi possível alterar município no banco de dados. O município com o nome " + municipioDTO.getNome() + " já está cadastrado.", 404);
        }
        return null;
    }

    /**
     * Retorna uma lista de municípios filtrados de acordo com os parâmetros fornecidos.
     *
     * @param codigoMunicipio código do município a ser filtrado.
     * @param codigoUF código da unidade federativa a ser filtrado.
     * @param nome nome do município a ser filtrado.
     * @param status status do município a ser filtrado.
     * @return lista de objetos {@link MunicipioDTO} que atendem aos critérios de filtro.
     */
    public List<MunicipioDTO> findByFilters(Optional<Long> codigoMunicipio, Optional<Long> codigoUF, Optional<String> nome, Optional<Integer> status) {
        List<Municipio> municipios = municipioDAO.findByFilters(codigoMunicipio, codigoUF, nome, status);

        return municipios.stream()
                .map(municipio -> new MunicipioDTO(
                        municipio.getCodigoMunicipio(),
                        municipio.getUf().getCodigoUF(),
                        municipio.getNome(),
                        municipio.getStatus()))
                .sorted((o1, o2) -> -o1.getCodigoMunicipio().compareTo(o2.getCodigoMunicipio()))
                .collect(Collectors.toList());
    }

}
