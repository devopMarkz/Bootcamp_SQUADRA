package br.com.squadra.bootcamp.projeto.service;

import br.com.squadra.bootcamp.projeto.dto.BairroDTO;
import br.com.squadra.bootcamp.projeto.model.dao.BairroDAO;
import br.com.squadra.bootcamp.projeto.model.dao.DAOFactory;
import br.com.squadra.bootcamp.projeto.model.dao.MunicipioDAO;
import br.com.squadra.bootcamp.projeto.model.entities.Bairro;
import br.com.squadra.bootcamp.projeto.model.entities.Municipio;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas operações relacionadas aos bairros, incluindo inserção, atualização, consulta e validação de dados.
 */
@Service
public class BairroService {

    private BairroDAO bairroDAO = DAOFactory.createBairroDAO();
    private MunicipioDAO municipioDAO = DAOFactory.createMunicipioDAO();

    /**
     * Insere um novo bairro no banco de dados.
     *
     * @param bairroDTO objeto com os dados do bairro a ser inserido.
     */
    public void insert(BairroDTO bairroDTO) {
        bairroDAO.insert(bairroDTO);
    }

    /**
     * Atualiza os dados de um bairro existente no banco de dados.
     *
     * @param bairroDTO objeto com os dados atualizados do bairro.
     */
    public void update(BairroDTO bairroDTO) {
        bairroDAO.update(bairroDTO);
    }

    /**
     * Retorna uma lista de todos os bairros cadastrados no banco de dados, ordenados por código de bairro de forma decrescente.
     *
     * @return lista de objetos {@link BairroDTO} representando os bairros encontrados.
     */
    public List<BairroDTO> findAll() {
        return bairroDAO.findAll()
                .stream()
                .map(bairro -> new BairroDTO(
                        bairro.getCodigoBairro(),
                        bairro.getMunicipio().getCodigoMunicipio(),
                        bairro.getNome(),
                        bairro.getStatus()))
                .sorted((o1, o2) -> -o1.getCodigoBairro().compareTo(o2.getCodigoBairro()))
                .collect(Collectors.toList());
    }

    /**
     * Valida os dados de um bairro para inserção no banco de dados.
     *
     * @param bairroDTO objeto com os dados do bairro a ser validado.
     * @return um objeto {@link MessageErrorService} com a mensagem de erro, se houver, ou {@code null} se a validação for bem-sucedida.
     */
    public MessageErrorService validatePostBairro(BairroDTO bairroDTO) {
        if(bairroDTO.getCodigoMunicipio() == null && bairroDTO.getNome() == null && bairroDTO.getStatus() == null) {
            return new MessageErrorService("Não foi possível incluir município no banco de dados. Os campos codigoMunicipio, nome e status precisam estar inclusos no corpo da requisição.", 404);
        }
        if (bairroDTO.getCodigoMunicipio() == null) {
            return new MessageErrorService("Não foi possível incluir bairro no banco de dados. O campo codigoMunicipio é obrigatório.", 404);
        }
        if (bairroDTO.getNome() == null || bairroDTO.getNome().isEmpty()) {
            return new MessageErrorService("Não foi possível incluir bairro no banco de dados. O campo nome é obrigatório.", 404);
        }
        if (bairroDTO.getStatus() == null) {
            return new MessageErrorService("Não foi possível incluir bairro no banco de dados. O campo status é obrigatório.", 404);
        }
        if (bairroDTO.getStatus() < 1 || bairroDTO.getStatus() > 2) {
            return new MessageErrorService("Não foi possível incluir bairro no banco de dados. O status precisa ser 1 ou 2.", 404);
        }
        Municipio municipio = municipioDAO.findByCodigoMunicipio(bairroDTO.getCodigoMunicipio());
        if (municipio == null) {
            return new MessageErrorService("Não foi possível incluir bairro no banco de dados. O codigoMunicipio fornecido não existe.", 404);
        }
        if (bairroDAO.findByNome(bairroDTO.getNome().toUpperCase()) != null) {
            return new MessageErrorService("Não foi possível incluir bairro no banco de dados. O bairro com o nome " + bairroDTO.getNome() + " já está cadastrado.", 404);
        }
        return null;
    }

    /**
     * Valida os dados de um bairro para atualização no banco de dados.
     *
     * @param bairroDTO objeto com os dados do bairro a ser validado.
     * @return um objeto {@link MessageErrorService} com a mensagem de erro, se houver, ou {@code null} se a validação for bem-sucedida.
     */
    public MessageErrorService validatePutBairro(BairroDTO bairroDTO) {
        if(bairroDTO.getCodigoBairro() == null && bairroDTO.getCodigoMunicipio() == null && bairroDTO.getNome() == null && bairroDTO.getStatus() == null) {
            return new MessageErrorService("Não foi possível incluir município no banco de dados. Os campos codigoBairro, codigoMunicipio, nome e status precisam estar inclusos no corpo da requisição.", 404);
        }
        if (bairroDTO.getCodigoMunicipio() == null) {
            return new MessageErrorService("Não foi possível alterar bairro no banco de dados. O campo codigoMunicipio é obrigatório.", 404);
        }
        if (bairroDTO.getNome() == null || bairroDTO.getNome().isEmpty()) {
            return new MessageErrorService("Não foi possível alterar bairro no banco de dados. O campo nome é obrigatório.", 404);
        }
        if (bairroDTO.getStatus() == null) {
            return new MessageErrorService("Não foi possível alterar bairro no banco de dados. O campo status é obrigatório.", 404);
        }
        if (bairroDTO.getStatus() < 1 || bairroDTO.getStatus() > 2) {
            return new MessageErrorService("Não foi possível alterar bairro no banco de dados. O status precisa ser 1 ou 2.", 404);
        }
        Municipio municipio = municipioDAO.findByCodigoMunicipio(bairroDTO.getCodigoMunicipio());
        if (municipio == null) {
            return new MessageErrorService("Não foi possível alterar bairro no banco de dados. O código Município fornecido não existe.", 404);
        }
        Bairro existingBairro = bairroDAO.findByNome(bairroDTO.getNome());
        if (existingBairro != null && !existingBairro.getCodigoBairro().equals(bairroDTO.getCodigoBairro())) {
            return new MessageErrorService("Não foi possível alterar bairro no banco de dados. O bairro com o nome " + bairroDTO.getNome() + " já está cadastrado.", 404);
        }
        return null;
    }

    /**
     * Retorna uma lista de bairros filtrados de acordo com os parâmetros fornecidos.
     *
     * @param codigoBairro código do bairro a ser filtrado.
     * @param codigoMunicipio código do município a ser filtrado.
     * @param nome nome do bairro a ser filtrado.
     * @param status status do bairro a ser filtrado.
     * @return lista de objetos {@link BairroDTO} que atendem aos critérios de filtro.
     */
    public List<BairroDTO> findByFilters(Optional<Long> codigoBairro, Optional<Long> codigoMunicipio, Optional<String> nome, Optional<Integer> status) {
        List<Bairro> bairros = bairroDAO.findByFilters(codigoBairro, codigoMunicipio, nome, status);

        return bairros.stream()
                .map(bairro -> new BairroDTO(
                        bairro.getCodigoBairro(),
                        bairro.getMunicipio().getCodigoMunicipio(),
                        bairro.getNome(),
                        bairro.getStatus()))
                .sorted((o1, o2) -> -o1.getCodigoBairro().compareTo(o2.getCodigoBairro()))
                .collect(Collectors.toList());
    }

    /**
     * Valida os parâmetros de consulta de bairros.
     *
     * @param codigoBairro código do bairro a ser consultado.
     * @param codigoMunicipio código do município a ser consultado.
     * @param nome nome do bairro a ser consultado.
     * @param status status do bairro a ser consultado.
     * @return um objeto {@link MessageErrorService} com a mensagem de erro, se houver, ou {@code null} se a validação for bem-sucedida.
     */
    public MessageErrorService validateGetBairro(Optional<Long> codigoBairro, Optional<Long> codigoMunicipio, Optional<String> nome, Optional<Integer> status) {
        if (codigoBairro.isPresent() && codigoBairro.get() <= 0) {
            return new MessageErrorService("Não foi possível consultar bairro no banco de dados. O código do bairro deve ser maior que zero.", 404);
        } else if (codigoBairro.isPresent() && bairroDAO.findByCodigoBairro(codigoBairro.get()) == null) {
            return new MessageErrorService("Não foi possível consultar bairro no banco de dados. O código do bairro informado não existe.", 404);
        }
        if (codigoMunicipio.isPresent() && codigoMunicipio.get() <= 0) {
            return new MessageErrorService("Não foi possível consultar bairro no banco de dados. O código do município deve ser maior que zero.", 404);
        } else if (codigoMunicipio.isPresent() && municipioDAO.findByCodigoMunicipio(codigoMunicipio.get()) == null) {
            return new MessageErrorService("Não foi possível consultar bairro no banco de dados. O código do município informado não existe.", 404);
        }

        if (nome.isPresent() && nome.get().trim().isEmpty()) {
            return new MessageErrorService("Não foi possível consultar bairro no banco de dados. O nome do bairro não pode ser vazio.", 404);
        } else if (nome.isPresent()) {
            if (bairroDAO.findByNome(nome.get()) == null) {
                return new MessageErrorService("Não foi possível consultar bairro no banco de dados. O nome do bairro informado não existe no banco de dados.", 404);
            }
        }
        if (status.isPresent()) {
            if (status.get() < 1 || status.get() > 2) {
                return new MessageErrorService("Não foi possível consultar bairro no banco de dados. O status deve ser 1 (ativo) ou 2 (inativo).", 404);
            }
        }

        return null;
    }
}
