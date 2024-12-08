package br.com.squadra.bootcamp.projeto.controller;

import br.com.squadra.bootcamp.projeto.dto.PessoaDTO;
import br.com.squadra.bootcamp.projeto.service.MessageErrorService;
import br.com.squadra.bootcamp.projeto.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador responsável pelas operações relacionadas às pessoas, como
 * inserção, atualização e consulta das pessoas através da API.
 * A classe contém métodos para manipulação de dados das pessoas, validando as entradas
 * e retornando respostas adequadas ao cliente.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;  // Serviço para operações com pessoas

    /**
     * Metodo GET para consultar pessoas com base nos parâmetros fornecidos.
     *
     * @param codigoPessoa Código da pessoa (opcional)
     * @param login Login da pessoa (opcional)
     * @param status Status da pessoa (opcional)
     * @return Lista de pessoas ou uma única pessoa, dependendo do filtro aplicado.
     */
    @GetMapping
    public ResponseEntity<?> getPessoas(
            @RequestParam(required = false) String codigoPessoa,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) String status) {

        Long codigoPessoaLong = null;
        Integer statusInt = null;

        // Validação para codigoPessoa: garante que apenas números são aceitos
        if (codigoPessoa != null) {
            try {
                codigoPessoaLong = Long.parseLong(codigoPessoa);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(404)
                        .body(new MessageErrorService(
                                "Não foi possível consultar Pessoa no banco de dados. O campo codigoPessoa deve conter apenas números.", 404));
            }
        }

        // Validação para status: garante que apenas números são aceitos
        if (status != null) {
            try {
                statusInt = Integer.parseInt(status);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(404)
                        .body(new MessageErrorService(
                                "Não foi possível consultar Pessoa no banco de dados. O campo status deve conter apenas números.", 404));
            }
        }

        // Verifica se o código da pessoa está presente e retorna a pessoa com endereços completos
        if (codigoPessoaLong != null) {
            PessoaDTO pessoa = pessoaService.findByCodigoPessoa(codigoPessoaLong);
            List<PessoaDTO> pessoaDTOS = new ArrayList<>();
            if (pessoa != null) {
                pessoaDTOS.add(pessoa);
                return ResponseEntity.ok(pessoaDTOS.getFirst());
            } else {
                return ResponseEntity.status(200).body(pessoaDTOS);
            }
        } else {
            List<PessoaDTO> pessoas = pessoaService.findByFilters(
                    Optional.ofNullable(codigoPessoaLong),
                    Optional.ofNullable(login),
                    Optional.ofNullable(statusInt));
            return ResponseEntity.ok(pessoas);
        }
    }

    /**
     * Metodo POST para inserir uma nova pessoa.
     *
     * @param pessoaDTO Dados da pessoa a ser inserida
     * @return Lista atualizada de pessoas ou uma mensagem de erro caso não seja possível inserir a pessoa.
     */
    @PostMapping
    public ResponseEntity<?> insertPessoa(@RequestBody PessoaDTO pessoaDTO) {
        MessageErrorService messageErrorService = pessoaService.validatePostPessoa(pessoaDTO);
        if (messageErrorService != null) {
            return ResponseEntity.status(404).body(messageErrorService);
        }
        pessoaService.insert(pessoaDTO);
        return ResponseEntity.status(200).body(pessoaService.findAll());
    }

    /**
     * Metodo PUT para atualizar os dados de uma pessoa existente.
     *
     * @param pessoaDTO Dados da pessoa a ser atualizada
     * @return Lista atualizada de pessoas ou uma mensagem de erro caso não seja possível atualizar a pessoa.
     */
    @PutMapping
    public ResponseEntity<?> updatePessoa(@RequestBody PessoaDTO pessoaDTO) {
        MessageErrorService messageErrorService = pessoaService.validatePutPessoa(pessoaDTO);
        if (messageErrorService != null) {
            return ResponseEntity.status(404).body(messageErrorService);
        }
        try {
            pessoaService.update(pessoaDTO);
            return ResponseEntity.status(200).body(pessoaService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new MessageErrorService("Não foi possível alterar pessoa no banco de dados.", 404));
        }
    }

    /**
     * Captura exceções de tipo inválido no corpo da requisição (JSON malformado) e retorna uma mensagem de erro personalizada.
     *
     * @param ex Exceção capturada
     * @param handlerMethod Metodo que causou a exceção
     * @return Mensagem de erro com status 404
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MessageErrorService> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HandlerMethod handlerMethod) {
        String methodName = handlerMethod.getMethod().getName();
        String message;

        // Verifica qual metodo causou a exceção e define a mensagem de erro adequada
        if ("insertPessoa".equals(methodName)) {
            message = "Não foi possível incluir pessoa no banco de dados. Tipo de dado inválido.";
        } else if ("updatePessoa".equals(methodName)) {
            message = "Não foi possível alterar endereço no banco de dados. Tipo de dado inválido.";
        } else {
            message = "Não foi possível processar a requisição.";
        }

        return ResponseEntity.status(404).body(new MessageErrorService(message, 404));
    }
}
