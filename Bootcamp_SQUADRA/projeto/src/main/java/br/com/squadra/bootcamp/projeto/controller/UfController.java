package br.com.squadra.bootcamp.projeto.controller;

import br.com.squadra.bootcamp.projeto.model.entities.Uf;
import br.com.squadra.bootcamp.projeto.service.MessageErrorService;
import br.com.squadra.bootcamp.projeto.service.UfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador responsável pelas operações relacionadas às Unidades Federativas (UFs),
 * como inserção, atualização e consulta das UFs através da API.
 * A classe contém métodos para manipulação de dados das UFs, validando as entradas
 * e retornando respostas adequadas ao cliente.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/uf")
public class UfController {

    @Autowired
    private UfService ufService;  // Serviço para operações com UFs

    /**
     * Metodo GET para consultar UFs com base nos parâmetros fornecidos.
     * Realiza a validação dos parâmetros e retorna a UF ou lista de UFs de acordo com os filtros aplicados.
     *
     * @param codigoUF Código da UF (opcional)
     * @param sigla Sigla da UF (opcional)
     * @param nome Nome da UF (opcional)
     * @param status Status da UF (opcional)
     * @return UF única ou lista de UFs conforme os filtros aplicados.
     */
    @GetMapping
    public ResponseEntity<?> getUfByFilters(
            @RequestParam(required = false) String codigoUF,
            @RequestParam(required = false) String sigla,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String status) {

        Long codigoUfLong = null;
        Integer statusInt = null;

        // Validação para codigoUF: assegura que apenas números são aceitos
        if (codigoUF != null) {
            try {
                codigoUfLong = Long.parseLong(codigoUF);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(404)
                        .body(new MessageErrorService("Não foi possível consultar UF no banco de dados. o campo codigoUF deve receber apenas números.", 404));
            }
        }

        // Validação para status: assegura que apenas números são aceitos
        if (status != null) {
            try {
                statusInt = Integer.parseInt(status);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(404)
                        .body(new MessageErrorService("Não foi possível consultar UF no banco de dados. O campo status deve receber apenas números.", 404));
            }
        }

        // Se SÓ o codigoUF for PASSADO, retorna a UF com esse código
        if (codigoUfLong != null && sigla == null && nome == null && statusInt == null) {
            Uf uf = ufService.findById(codigoUfLong);
            List<Uf> ufList = new ArrayList<>();
            if(uf != null){
                ufList.add(uf);
            }
            return uf != null ? ResponseEntity.ok(ufList.getFirst()) : ResponseEntity.status(200).body(ufList);
        }

        // Se só o status for fornecido, retorna uma lista de UFs com esse status
        if (statusInt != null && codigoUfLong == null && sigla == null && nome == null) {
            List<Uf> ufList = ufService.findByStatus(statusInt);
            return ResponseEntity.ok(ufList);
        }

        // Se qualquer outro filtro for fornecido, ou uma combinação deles, busca um único UF
        if (codigoUfLong != null || sigla != null || nome != null || statusInt != null) {
            Uf uf = ufService.findUniqueByFilters(codigoUfLong, sigla, nome, statusInt);
            List<Uf> ufList = new ArrayList<>();
            if(uf != null) {
                ufList.add(uf);
            }
            return uf != null ? ResponseEntity.ok(ufList.getFirst()) : ResponseEntity.status(200).body(ufList);
        }

        // Se nenhum filtro for fornecido, retorna uma lista com todas as UFs
        return ResponseEntity.ok(ufService.findAll());
    }

    /**
     * Metodo POST para inserir uma nova UF.
     * Realiza a validação dos dados da UF antes de inseri-la no banco de dados.
     *
     * @param uf Dados da UF a ser inserida
     * @return Lista atualizada de UFs ou uma mensagem de erro caso não seja possível inserir a UF.
     */
    @PostMapping
    public ResponseEntity<?> insertUf(@RequestBody Uf uf) {
        MessageErrorService messageErrorServicePost = ufService.validatePostUf(uf);
        if(messageErrorServicePost != null) return ResponseEntity.status(404).body(messageErrorServicePost);
        ufService.insert(uf);
        return ResponseEntity.ok(ufService.findAll());
    }

    /**
     * Metodo PUT para atualizar os dados de uma UF existente.
     * Realiza a validação dos dados antes de atualizar a UF no banco de dados.
     *
     * @param uf Dados da UF a ser atualizada
     * @return Lista atualizada de UFs ou uma mensagem de erro caso não seja possível atualizar a UF.
     */
    @PutMapping
    public ResponseEntity<?> updateUf(@RequestBody Uf uf) {
        // Validações que você já implementou
        MessageErrorService messageErrorServicePut = ufService.validatePutUf(uf);
        if (messageErrorServicePut != null) {
            return ResponseEntity.status(404).body(messageErrorServicePut);
        }

        // Atualização da UF
        ufService.updateUf(uf);
        return ResponseEntity.status(200).body(ufService.findAll());
    }

    /**
     * Captura a exceção de tipo inválido no corpo da requisição (JSON malformado) e retorna uma mensagem de erro personalizada.
     *
     * @param ex Exceção capturada
     * @param handlerMethod Metodo que causou a exceção
     * @return Mensagem de erro com status 404
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MessageErrorService> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HandlerMethod handlerMethod) {

        String methodName = handlerMethod.getMethod().getName();
        String message;

        // Verifica qual metodo causou a exceção e define a mensagem de erro adequada
        if ("insertUf".equals(methodName)) {
            message = "Não foi possível incluir UF no banco de dados. Tipo de dado inválido.";
        } else if ("updateUf".equals(methodName)) {
            message = "Não foi possível alterar UF no banco de dados. Tipo de dado inválido.";
        } else {
            message = "Não foi possível processar a requisição.";
        }

        return ResponseEntity.status(404).body(new MessageErrorService(message, 404));
    }

}