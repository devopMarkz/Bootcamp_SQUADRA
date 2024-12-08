package br.com.squadra.bootcamp.projeto.service;

import br.com.squadra.bootcamp.projeto.model.dao.DAOFactory;
import br.com.squadra.bootcamp.projeto.model.dao.UfDAO;
import br.com.squadra.bootcamp.projeto.model.entities.Uf;
import br.com.squadra.bootcamp.projeto.util.UfList;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável por realizar as operações relacionadas ao cadastro, atualização e validação de UFs (Unidades Federativas).
 * Este serviço interage com o banco de dados através do DAO de UF, e realiza a validação de dados para inserção e atualização de UFs.
 */
@Service
public class UfService {

    private UfDAO ufDAO = DAOFactory.createUfDAO(); // Instância do DAO responsável por interagir com a tabela de UFs
    private final MessageErrorService messageErrorServicePost = new MessageErrorService("Não foi possível incluir UF no banco de dados.", 404); // Mensagem de erro para POST
    private final MessageErrorService messageErrorServicePut = new MessageErrorService("Não foi possível alterar UF no banco de dados.", 404); // Mensagem de erro para PUT

    /**
     * Busca uma UF pelo nome.
     *
     * @param nome O nome da UF a ser buscada.
     * @return A UF encontrada, ou null se não houver nenhuma UF com o nome informado.
     */
    public Uf findByNome(String nome){
        Uf uf = ufDAO.findByNome(nome);
        return uf;
    }

    /**
     * Busca uma UF única com base em filtros fornecidos.
     *
     * @param codigoUF O código da UF.
     * @param sigla A sigla da UF.
     * @param nome O nome da UF.
     * @param status O status da UF (1 ou 2).
     * @return A UF que corresponde aos filtros fornecidos.
     */
    public Uf findUniqueByFilters(Long codigoUF, String sigla, String nome, Integer status) {
        return ufDAO.findUniqueByFilters(codigoUF, sigla, nome, status);
    }

    /**
     * Busca todas as UFs com um status específico.
     *
     * @param status O status da UF (1 ou 2).
     * @return Uma lista de UFs com o status informado.
     */
    public List<Uf> findByStatus(int status) {
        return ufDAO.findByStatus(status);
    }

    /**
     * Busca uma UF pelo código único (ID).
     *
     * @param codigoUF O código da UF.
     * @return A UF encontrada, ou null se não houver nenhuma UF com o código informado.
     */
    public Uf findById(Long codigoUF) {
        return ufDAO.findById(codigoUF);
    }

    /**
     * Busca todas as UFs cadastradas.
     *
     * @return Uma lista com todas as UFs cadastradas.
     */
    public List<Uf> findAll() {
        List<Uf> ufList = ufDAO.findAll();
        return ufList;
    }

    /**
     * Insere uma nova UF no banco de dados.
     *
     * @param uf O objeto UF a ser inserido.
     * @return Uma lista com todas as UFs, incluindo a recém-inserida.
     */
    public List<Uf> insert(Uf uf) {
        ufDAO.insert(uf);
        return ufDAO.findAll();
    }

    /**
     * Atualiza uma UF existente no banco de dados.
     *
     * @param uf O objeto UF com os dados atualizados.
     * @return Uma lista com todas as UFs, incluindo a UF atualizada.
     */
    public List<Uf> updateUf(Uf uf) {
        ufDAO.update(uf);
        return ufDAO.findAll();
    }

    /**
     * Valida os dados de uma UF antes de sua inserção no banco de dados.
     * Verifica se os campos obrigatórios estão presentes e se os dados estão corretos.
     *
     * @param uf A UF a ser validada.
     * @return Um objeto MessageErrorService com a mensagem de erro, se houver, ou null caso a validação passe.
     */
    public MessageErrorService validatePostUf(Uf uf) {
        if(uf.getCodigoUF() == null && uf.getSigla() == null && uf.getNome() == null && uf.getStatus() == null){
            return new MessageErrorService("Não foi possível incluir UF no banco de dados. Os campos nome, sigla e status precisam estar inclusos no corpo da requisição.", 404);
        }
        if(uf.getSigla() == null || uf.getSigla().isEmpty()) {
            return new MessageErrorService("Não foi possível incluir UF no banco de dados. O campo sigla está vazio ou não foi incluso no corpo da requisição.", 404);
        }
        if(uf.getNome() == null || uf.getNome().isEmpty()) {
            return new MessageErrorService("Não foi possível incluir UF no banco de dados. O campo nome está vazio ou não foi incluso no corpo da requisição.", 404);
        }
        if(uf.getStatus() == null) {
            return new MessageErrorService("Não foi possível incluir UF no banco de dados. O campo status está vazio ou não foi incluso no corpo da requisição.", 404);
        }
        if (uf.getStatus() < 1 || uf.getStatus() > 2) {
            return new MessageErrorService("Não foi possível incluir UF no banco de dados. O status precisa ser 1 ou 2.", 404);
        }
        if (ufDAO.findByNome(uf.getNome().toUpperCase()) != null) {
            return new MessageErrorService("Não foi possível incluir UF no banco de dados. O Estado " + uf.getNome() + " já está cadastrado.", 404);
        }
        if (ufDAO.findBySigla(uf.getSigla().toUpperCase()) != null) {
            return new MessageErrorService("Não foi possível incluir UF no banco de dados. Já existe um Estado cadastrado com a sigla " + uf.getSigla() + ".", 404);
        }
        if (!validateNomeSiglaUf(uf)) {
            return new MessageErrorService("Não foi possível incluir UF no banco de dados. O nome do Estado e/ou sigla foram digitados de maneira incorreta.", 404);
        }
        return null;
    }

    /**
     * Valida os dados de uma UF antes de sua atualização no banco de dados.
     * Verifica se os campos obrigatórios estão presentes, se o status é válido e se os dados estão corretos.
     *
     * @param uf A UF a ser validada.
     * @return Um objeto MessageErrorService com a mensagem de erro, se houver, ou null caso a validação passe.
     */
    public MessageErrorService validatePutUf(Uf uf) {
        if(uf.getNome() == null && uf.getSigla() == null && uf.getStatus() == null) {
            return new MessageErrorService("Não foi possível alterar UF no banco de dados. Os campos codigoUF, sigla, nome e status precisam estar inclusos no corpo da requisição.", 404);
        }
        if(uf.getCodigoUF() == null){
            return new MessageErrorService("Não foi possível alterar UF no banco de dados. O campo codigoUF está vazio ou não foi incluso no corpo da requisição.", 404);
        }
        if(uf.getSigla()== null || uf.getSigla().isEmpty()){
            return new MessageErrorService("Não foi possível alterar UF no banco de dados. O campo sigla está vazio ou não foi incluso no corpo da requisição.", 404);
        }
        if(uf.getNome() == null || uf.getNome().isEmpty()){
            return new MessageErrorService("Não foi possível alterar UF no banco de dados. O campo nome está vazio ou não foi incluso no corpo da requisição.", 404);
        }
        if(uf.getStatus() == null){
            return new MessageErrorService("Não foi possível alterar UF no banco de dados. O campo status está vazio ou não foi incluso no corpo da requisição.", 404);
        }
        if(uf.getStatus() == null) {
            return new MessageErrorService("Não foi possível alterar UF no banco de dados. O status precisa ser 1 ou 2.", 404);
        }
        if(uf.getStatus() < 1 || uf.getStatus() > 2) {
            return new MessageErrorService("Não foi possível alterar UF no banco de dados. O status precisa ser 1 ou 2.", 404);
        }
        if(ufDAO.findBySigla(uf.getSigla()) != null) {
            return new MessageErrorService("Não foi possível alterar UF no banco de dados. Já existe um Estado cadastrado com a sigla " + uf.getSigla() + ".", 404);
        }
        if(!(validateNomeSiglaUf(uf))) {
            return new MessageErrorService("Não foi possível alterar UF no banco de dados. o Estado e/ou sigla foram digitados de maneira incorreta ou não foram preenchidos." , 404);
        }
        if(ufDAO.findByNome(uf.getNome().toUpperCase()) != null) {
            return new MessageErrorService("Não foi possível alterar UF no banco de dados. O Estado " + uf.getNome() + " já está cadastrado.", 404);
        }
        if(ufDAO.findByNome(uf.getNome().toUpperCase()) != null || ufDAO.findBySigla(uf.getSigla().toUpperCase()) != null) {
            return messageErrorServicePut;
        }
        return null;
    }

    /**
     * Valida se o nome e a sigla de uma UF estão corretos de acordo com o mapeamento de UFs predefinido.
     *
     * @param uf A UF a ser validada.
     * @return true se o nome e a sigla forem válidos, false caso contrário.
     */
    public Boolean validateNomeSiglaUf(Uf uf) {
        return UfList.mapUfs.containsKey(uf.getNome().toUpperCase()) && UfList.mapUfs.containsValue(uf.getSigla().toUpperCase());
    }

}
