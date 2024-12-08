package br.com.squadra.bootcamp.projeto.service;

import br.com.squadra.bootcamp.projeto.dto.*;
import br.com.squadra.bootcamp.projeto.model.dao.BairroDAO;
import br.com.squadra.bootcamp.projeto.model.dao.DAOFactory;
import br.com.squadra.bootcamp.projeto.model.dao.EnderecoDAO;
import br.com.squadra.bootcamp.projeto.model.dao.MunicipioDAO;
import br.com.squadra.bootcamp.projeto.model.dao.PessoaDAO;
import br.com.squadra.bootcamp.projeto.model.dao.UfDAO;
import br.com.squadra.bootcamp.projeto.model.entities.Bairro;
import br.com.squadra.bootcamp.projeto.model.entities.Endereco;
import br.com.squadra.bootcamp.projeto.model.entities.Municipio;
import br.com.squadra.bootcamp.projeto.model.entities.Pessoa;
import br.com.squadra.bootcamp.projeto.model.entities.Uf;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela gestão das operações de pessoa, incluindo manipulação de dados
 * e integração com o banco de dados para criar, consultar, atualizar e excluir pessoas.
 */
@Service
public class PessoaService {

    private PessoaDAO pessoaDAO = DAOFactory.createPessoaDAO();
    private EnderecoDAO enderecoDAO = DAOFactory.createEnderecoDAO();
    private BairroDAO bairroDAO = DAOFactory.createBairroDAO();
    private MunicipioDAO municipioDAO = DAOFactory.createMunicipioDAO();
    private UfDAO ufDAO = DAOFactory.createUfDAO();

    /**
     * Retorna uma lista de todas as pessoas, convertidas para DTO e ordenadas por código de forma decrescente.
     *
     * @return Lista de pessoas no formato DTO.
     */
    public List<PessoaDTO> findAll() {
        return pessoaDAO.findAll().stream()
                .map(this::convertToDTO)
                .sorted((o1, o2) -> -o1.getCodigoPessoa().compareTo(o2.getCodigoPessoa()))
                .collect(Collectors.toList());
    }

    /**
     * Retorna uma pessoa específica e seus endereços associados, a partir do código da pessoa.
     *
     * @param codigoPessoa Código único de identificação da pessoa.
     * @return DTO da pessoa com os endereços, ou null se não encontrar a pessoa.
     */
    public PessoaDTO findByCodigoPessoa(Long codigoPessoa) {
        Pessoa pessoa = pessoaDAO.findByCodigoPessoa(codigoPessoa);
        if (pessoa != null) {
            List<Endereco> enderecos = enderecoDAO.findByCodigoPessoa(codigoPessoa);
            PessoaDTO pessoaDTO = convertToDTO(pessoa);
            pessoaDTO.setEnderecos(enderecos.stream().map(this::convertToDTO).collect(Collectors.toList()));
            return pessoaDTO;
        }
        return null;
    }

    /**
     * Busca pessoas com base em filtros opcionais como código, login e status.
     *
     * @param codigoPessoa Código da pessoa (opcional).
     * @param login Login da pessoa (opcional).
     * @param status Status da pessoa (opcional).
     * @return Lista de pessoas no formato DTO filtradas.
     */
    public List<PessoaDTO> findByFilters(Optional<Long> codigoPessoa, Optional<String> login, Optional<Integer> status) {
        return pessoaDAO.findByFilters(codigoPessoa, login, status).stream()
                .map(this::convertToDTO)
                .sorted((o1, o2) -> -o1.getCodigoPessoa().compareTo(o2.getCodigoPessoa()))
                .collect(Collectors.toList());
    }

    /**
     * Insere uma nova pessoa no banco de dados junto com seus endereços.
     *
     * @param pessoaDTO Dados da pessoa a serem inseridos.
     */
    public void insert(PessoaDTO pessoaDTO) {
        Pessoa pessoa = convertToEntity(pessoaDTO);
        pessoa = pessoaDAO.insert(pessoa);

        for (EnderecoDTOGet enderecoDTOGet : pessoaDTO.getEnderecos()) {
            Endereco endereco = convertToEntity(enderecoDTOGet, pessoa.getCodigoPessoa());
            enderecoDAO.insert(endereco);
        }
    }

    /**
     * Atualiza os dados de uma pessoa e seus endereços no banco de dados.
     *
     * @param pessoaDTO Dados da pessoa a serem atualizados.
     */
    public void update(PessoaDTO pessoaDTO) {
        Pessoa pessoa = convertToEntity(pessoaDTO);
        pessoaDAO.update(pessoa);

        List<Endereco> enderecosAtuais = enderecoDAO.findByCodigoPessoa(pessoa.getCodigoPessoa());

        List<Long> enderecosIdsAtualizados = new ArrayList<>();

        for (EnderecoDTOGet enderecoDTO : pessoaDTO.getEnderecos()) {
            if (enderecoDTO.getCodigoEndereco() != null) {
                Endereco enderecoExistente = enderecosAtuais.stream()
                        .filter(e -> e.getCodigoEndereco().equals(enderecoDTO.getCodigoEndereco()))
                        .findFirst()
                        .orElse(null);

                if (enderecoExistente != null) {
                    enderecoExistente.setCodigoBairro(enderecoDTO.getCodigoBairro());
                    enderecoExistente.setNomeRua(enderecoDTO.getNomeRua());
                    enderecoExistente.setNumero(enderecoDTO.getNumero());
                    enderecoExistente.setComplemento(enderecoDTO.getComplemento());
                    enderecoExistente.setCep(enderecoDTO.getCep());
                    enderecoDAO.update(enderecoExistente);
                    enderecosIdsAtualizados.add(enderecoExistente.getCodigoEndereco());
                }
            } else {
                Endereco novoEndereco = convertToEntity(enderecoDTO, pessoa.getCodigoPessoa());
                enderecoDAO.insert(novoEndereco);
                enderecosIdsAtualizados.add(novoEndereco.getCodigoEndereco());
            }
        }

        for (Endereco enderecoAtual : enderecosAtuais) {
            if (!enderecosIdsAtualizados.contains(enderecoAtual.getCodigoEndereco())) {
                enderecoDAO.deleteByCodigoEndereco(enderecoAtual.getCodigoEndereco());
            }
        }
    }

    /**
     * Converte uma entidade Pessoa para o seu DTO correspondente.
     *
     * @param pessoa Entidade Pessoa a ser convertida.
     * @return DTO correspondente à Pessoa.
     */
    private PessoaDTO convertToDTO(Pessoa pessoa) {
        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.setCodigoPessoa(pessoa.getCodigoPessoa());
        pessoaDTO.setNome(pessoa.getNome());
        pessoaDTO.setSobrenome(pessoa.getSobrenome());
        pessoaDTO.setIdade(pessoa.getIdade());
        pessoaDTO.setLogin(pessoa.getLogin());
        pessoaDTO.setSenha(pessoa.getSenha());
        pessoaDTO.setStatus(pessoa.getStatus());
        pessoaDTO.setEnderecos(new ArrayList<>()); // Inicializa com uma lista vazia
        return pessoaDTO;
    }

    /**
     * Converte um DTO de Pessoa para a entidade correspondente.
     *
     * @param pessoaDTO DTO de Pessoa a ser convertido.
     * @return Entidade correspondente à Pessoa.
     */
    private Pessoa convertToEntity(PessoaDTO pessoaDTO) {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigoPessoa(pessoaDTO.getCodigoPessoa());
        pessoa.setNome(pessoaDTO.getNome());
        pessoa.setSobrenome(pessoaDTO.getSobrenome());
        pessoa.setIdade(pessoaDTO.getIdade());
        pessoa.setLogin(pessoaDTO.getLogin());
        pessoa.setSenha(pessoaDTO.getSenha());
        pessoa.setStatus(pessoaDTO.getStatus());
        return pessoa;
    }

    /**
     * Converte um DTO de Endereço para a entidade Endereço, contendo o DTO do Bairro aninhado.
     *
     * @param enderecoDTO DTO de Endereço a ser convertido.
     * @param codigoPessoa Código da pessoa associada ao endereço.
     * @return Entidade Endereço correspondente.
     */
    private Endereco convertToEntity(EnderecoDTOGet enderecoDTO, Long codigoPessoa) {
        Endereco endereco = new Endereco();
        endereco.setCodigoPessoa(codigoPessoa);
        endereco.setCodigoBairro(enderecoDTO.getCodigoBairro());
        endereco.setNomeRua(enderecoDTO.getNomeRua());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setComplemento(enderecoDTO.getComplemento());
        endereco.setCep(enderecoDTO.getCep());
        return endereco;
    }

    /**
     * Converte um DTO de Endereço para a entidade Endereço, sem conter o DTO do bairro aninhado.
     *
     * @param enderecoDTO DTO de Endereço a ser convertido.
     * @param codigoPessoa Código da pessoa associada ao endereço.
     * @return Entidade Endereço correspondente.
     */
    private Endereco convertToEntity(EnderecoDTO enderecoDTO, Long codigoPessoa) {
        Endereco endereco = new Endereco();
        endereco.setCodigoEndereco(enderecoDTO.getCodigoEndereco());
        endereco.setCodigoPessoa(codigoPessoa);
        endereco.setCodigoBairro(enderecoDTO.getCodigoBairro());
        endereco.setNomeRua(enderecoDTO.getNomeRua());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setComplemento(enderecoDTO.getComplemento());
        endereco.setCep(enderecoDTO.getCep());
        return endereco;
    }

    /**
     * Converte uma entidade Endereco para o seu DTO correspondente, incluindo os detalhes do bairro,
     * município e UF (Unidade Federativa) associados ao endereço.
     *
     * @param endereco A entidade Endereco a ser convertida.
     * @return O DTO correspondente à entidade Endereco com informações completas, incluindo Bairro, Município e UF.
     */

    private EnderecoDTOGet convertToDTO(Endereco endereco) {
        EnderecoDTOGet enderecoDTO = new EnderecoDTOGet();
        enderecoDTO.setCodigoEndereco(endereco.getCodigoEndereco());
        enderecoDTO.setCodigoPessoa(endereco.getCodigoPessoa());
        enderecoDTO.setCodigoBairro(endereco.getCodigoBairro());
        enderecoDTO.setNomeRua(endereco.getNomeRua());
        enderecoDTO.setNumero(endereco.getNumero());
        enderecoDTO.setComplemento(endereco.getComplemento());
        enderecoDTO.setCep(endereco.getCep());

        // Obter e preencher os detalhes do bairro
        Bairro bairro = bairroDAO.findByCodigoBairro(endereco.getCodigoBairro());
        if (bairro != null) {
            BairroDTOGet bairroDTO = new BairroDTOGet();
            bairroDTO.setCodigoBairro(bairro.getCodigoBairro());
            bairroDTO.setCodigoMunicipio(bairro.getMunicipio().getCodigoMunicipio());
            bairroDTO.setNome(bairro.getNome());
            bairroDTO.setStatus(bairro.getStatus());

            // Obter e preencher os detalhes do município a partir do objeto Bairro
            Municipio municipio = bairro.getMunicipio();
            if (municipio != null) {
                MunicipioDTOGet municipioDTO = new MunicipioDTOGet();
                municipioDTO.setCodigoMunicipio(municipio.getCodigoMunicipio());
                municipioDTO.setCodigoUF(municipio.getUf().getCodigoUF());
                municipioDTO.setNome(municipio.getNome());
                municipioDTO.setStatus(municipio.getStatus());

                // Obter e preencher os detalhes da UF a partir do objeto Município
                Uf uf = municipio.getUf();
                if (uf != null) {
                    UfDTO ufDTO = new UfDTO();
                    ufDTO.setCodigoUF(uf.getCodigoUF());
                    ufDTO.setSigla(uf.getSigla());
                    ufDTO.setNome(uf.getNome());
                    ufDTO.setStatus(uf.getStatus());
                    municipioDTO.setUf(ufDTO);
                }

                bairroDTO.setMunicipio(municipioDTO);
            }

            enderecoDTO.setBairro(bairroDTO);
        }

        return enderecoDTO;
    }

    /**
     * Valida os dados de entrada para a consulta de uma pessoa.
     *
     * @param codigoPessoa Código da pessoa.
     * @param login Login da pessoa.
     * @param status Status da pessoa.
     * @return Mensagem de erro caso haja algum problema, ou null se tudo estiver correto.
     */
    private MessageErrorService validateGetPessoa(Long codigoPessoa, String login, Integer status) {
        if(codigoPessoa != null) {
            Pessoa pessoa = pessoaDAO.findByCodigoPessoa(codigoPessoa);
            if(pessoaDAO.findByCodigoPessoa(codigoPessoa) == null) {
                return new MessageErrorService("Não foi possível consultar pessoa no banco de dados.", 404);
            }
        }
        return null;
    }

    /**
     * Valida os dados de entrada para a criação de uma nova pessoa no banco de dados.
     *
     * @param pessoaDTO Dados da pessoa a serem validados.
     * @return Mensagem de erro caso haja algum problema, ou null se tudo estiver correto.
     */
    public MessageErrorService validatePostPessoa(PessoaDTO pessoaDTO) {
        // Valida se campos obrigatórios estão preenchidos
        if (pessoaDTO.getNome() == null || pessoaDTO.getNome().isEmpty()) {
            return new MessageErrorService("Não foi possível incluir pessoa no banco de dados. O campo 'nome' é obrigatório e não pode estar vazio.", 404);
        }
        if (pessoaDTO.getSobrenome() == null || pessoaDTO.getSobrenome().isEmpty()) {
            return new MessageErrorService("Não foi possível incluir pessoa no banco de dados. O campo 'sobrenome' é obrigatório e não pode estar vazio.", 404);
        }
        if (pessoaDTO.getIdade() == null) {
            return new MessageErrorService("Não foi possível incluir pessoa no banco de dados. O campo 'idade' é obrigatório.", 404);
        }
        if (pessoaDTO.getLogin() == null || pessoaDTO.getLogin().isEmpty()) {
            return new MessageErrorService("Não foi possível incluir pessoa no banco de dados. O campo 'login' é obrigatório e não pode estar vazio.", 404);
        }
        if (pessoaDTO.getSenha() == null || pessoaDTO.getSenha().isEmpty()) {
            return new MessageErrorService("Não foi possível incluir pessoa no banco de dados. O campo 'senha' é obrigatório e não pode estar vazio.", 404);
        }
        if (pessoaDTO.getStatus() == null) {
            return new MessageErrorService("Não foi possível incluir pessoa no banco de dados. O campo 'status' é obrigatório.", 404);
        }

        // Valida se o login já existe no banco de dados
        if (pessoaDAO.findByLogin(pessoaDTO.getLogin()) != null) {
            return new MessageErrorService("Não foi possível incluir pessoa no banco de dados. O login já existe.", 404);
        }

        // Valida se o status é 1 ou 2
        if (pessoaDTO.getStatus() != 1 && pessoaDTO.getStatus() != 2) {
            return new MessageErrorService("Não foi possível incluir pessoa no banco de dados. O status deve ser 1 ou 2.", 404);
        }

        // Valida se todos os códigos de bairro existem no banco de dados
        for (EnderecoDTOGet endereco : pessoaDTO.getEnderecos()) {
            if (endereco.getCodigoBairro() == null) {
                return new MessageErrorService("Não foi possível incluir pessoa no banco de dados. O campo 'codigoBairro' em 'enderecos' é obrigatório.", 404);
            }
            Bairro bairro = bairroDAO.findByCodigoBairro(endereco.getCodigoBairro());
            if (bairro == null) {
                return new MessageErrorService("Não foi possível incluir pessoa no banco de dados. O código do bairro " + endereco.getCodigoBairro() + " não existe.", 404);
            }
        }

        return null;
    }

    /**
     * Valida os dados para a atualização de uma pessoa existente.
     *
     * @param pessoaDTO Dados da pessoa a serem validados para atualização.
     * @return Mensagem de erro caso haja algum problema, ou null se tudo estiver correto.
     */
    public MessageErrorService validatePutPessoa(PessoaDTO pessoaDTO) {
        // Valida se campos obrigatórios estão preenchidos
        if (pessoaDTO.getNome() == null || pessoaDTO.getNome().isEmpty()) {
            return new MessageErrorService("Não foi possível alterar pessoa no banco de dados. O campo 'nome' é obrigatório e não pode estar vazio.", 404);
        }
        if (pessoaDTO.getSobrenome() == null || pessoaDTO.getSobrenome().isEmpty()) {
            return new MessageErrorService("Não foi possível alterar pessoa no banco de dados. O campo 'sobrenome' é obrigatório e não pode estar vazio.", 404);
        }
        if (pessoaDTO.getIdade() == null) {
            return new MessageErrorService("Não foi possível alterar pessoa no banco de dados. O campo 'idade' é obrigatório.", 404);
        }
        if (pessoaDTO.getLogin() == null || pessoaDTO.getLogin().isEmpty()) {
            return new MessageErrorService("Não foi possível alterar pessoa no banco de dados. O campo 'login' é obrigatório e não pode estar vazio.", 404);
        }
        if (pessoaDTO.getSenha() == null || pessoaDTO.getSenha().isEmpty()) {
            return new MessageErrorService("Não foi possível alterar pessoa no banco de dados. O campo 'senha' é obrigatório e não pode estar vazio.", 404);
        }
        if (pessoaDTO.getStatus() == null) {
            return new MessageErrorService("Não foi possível alterar pessoa no banco de dados. O campo 'status' é obrigatório.", 404);
        }

        // Valida se o login já existe no banco de dados para outro usuário
        Pessoa pessoaExistente = pessoaDAO.findByLogin(pessoaDTO.getLogin());
        if (pessoaExistente != null && !pessoaExistente.getCodigoPessoa().equals(pessoaDTO.getCodigoPessoa())) {
            return new MessageErrorService("Não foi possível alterar pessoa no banco de dados. O login já existe.", 404);
        }

        // Valida se o status é 1 ou 2
        if (pessoaDTO.getStatus() != 1 && pessoaDTO.getStatus() != 2) {
            return new MessageErrorService("Não foi possível alterar pessoa no banco de dados. O status deve ser 1 ou 2.", 404);
        }

        // Valida se todos os códigos de bairro existem no banco de dados
        for (EnderecoDTOGet endereco : pessoaDTO.getEnderecos()) {
            if (endereco.getCodigoBairro() == null) {
                return new MessageErrorService("Não foi possível alterar pessoa no banco de dados. O campo 'codigoBairro' em 'enderecos' é obrigatório.", 404);
            }
            Bairro bairro = bairroDAO.findByCodigoBairro(endereco.getCodigoBairro());
            if (bairro == null) {
                return new MessageErrorService("Não foi possível alterar pessoa no banco de dados. O código do bairro " + endereco.getCodigoBairro() + " não existe.", 404);
            }
        }

        return null;
    }
}
