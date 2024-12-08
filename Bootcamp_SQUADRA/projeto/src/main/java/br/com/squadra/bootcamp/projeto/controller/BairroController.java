package br.com.squadra.bootcamp.projeto.controller;

import br.com.squadra.bootcamp.projeto.dto.BairroDTO;
import br.com.squadra.bootcamp.projeto.service.BairroService;
import br.com.squadra.bootcamp.projeto.service.MessageErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;

import java.util.List;
import java.util.Optional;

/**
 * Controlador responsável pelas operações relacionadas aos bairros, como
 * inserção, atualização e consulta dos bairros através da API.
 * A classe contém métodos para manipulação de dados dos bairros, validando as entradas
 * e retornando respostas adequadas ao cliente.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/bairro")
public class BairroController {

    @Autowired
    private BairroService bairroService;  // Serviço para operações com bairros

    /**
     * Metodo GET que permite consultar bairros com base nos parâmetros fornecidos.
     *
     * @param codigoBairro Código do bairro (opcional)
     * @param codigoMunicipio Código do município (opcional)
     * @param nome Nome do bairro (opcional)
     * @param status Status do bairro (opcional)
     * @return Lista de bairros ou um único bairro, dependendo do filtro aplicado.
     */
    @GetMapping
    public ResponseEntity<?> getBairros(
            @RequestParam(required = false) String codigoBairro,
            @RequestParam(required = false) String codigoMunicipio,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String status) {

        Long codigoBairroLong = null;
        Long codigoMunicipioLong = null;
        Integer statusInt = null;

        // Validação para codigoBairro: garante que apenas números são aceitos
        if (codigoBairro != null) {
            try {
                codigoBairroLong = Long.parseLong(codigoBairro);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(404)
                        .body(new MessageErrorService(
                                "Não foi possível consultar Bairro no banco de dados. O campo codigoBairro deve conter apenas números.", 404));
            }
        }

        // Validação para codigoMunicipio: garante que apenas números são aceitos
        if (codigoMunicipio != null) {
            try {
                codigoMunicipioLong = Long.parseLong(codigoMunicipio);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(404)
                        .body(new MessageErrorService(
                                "Não foi possível consultar Bairro no banco de dados. O campo codigoMunicipio deve conter apenas números.", 404));
            }
        }

        // Validação para status: garante que apenas números são aceitos
        if (status != null) {
            try {
                statusInt = Integer.parseInt(status);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(404)
                        .body(new MessageErrorService(
                                "Não foi possível consultar Bairro no banco de dados. O campo status deve conter apenas números.", 404));
            }
        }

        // Busca os bairros com os filtros validados
        List<BairroDTO> result = bairroService.findByFilters(
                Optional.ofNullable(codigoBairroLong),
                Optional.ofNullable(codigoMunicipioLong),
                Optional.ofNullable(nome),
                Optional.ofNullable(statusInt));

        // Se apenas codigoBairro for fornecido e encontrar um único resultado, retorna o objeto
        if (codigoBairroLong != null && result.size() == 1) {
            return ResponseEntity.ok(result.get(0));
        }

        // Caso contrário, retorna a lista de bairros ou uma lista vazia
        return ResponseEntity.ok(result);
    }

    /**
     * Metodo POST para inserir um novo bairro.
     *
     * @param bairroDTO Dados do bairro a ser inserido
     * @return Lista atualizada de bairros ou uma mensagem de erro caso não seja possível inserir o bairro.
     */
    @PostMapping
    public ResponseEntity<?> insertBairro(@RequestBody BairroDTO bairroDTO) {
        MessageErrorService messageErrorService = bairroService.validatePostBairro(bairroDTO);
        if (messageErrorService == null) {
            bairroService.insert(bairroDTO);
            return ResponseEntity.status(200).body(bairroService.findAll());
        } else {
            return ResponseEntity.status(404).body(messageErrorService);
        }
    }

    /**
     * Metodo PUT para atualizar os dados de um bairro existente.
     *
     * @param bairroDTO Dados do bairro a ser atualizado
     * @return Lista atualizada de bairros ou uma mensagem de erro caso não seja possível atualizar o bairro.
     */
    @PutMapping
    public ResponseEntity<?> updateBairro(@RequestBody BairroDTO bairroDTO) {
        MessageErrorService messageErrorService = bairroService.validatePutBairro(bairroDTO);
        if (messageErrorService == null) {
            bairroService.update(bairroDTO);
            return ResponseEntity.status(200).body(bairroService.findAll());
        } else {
            return ResponseEntity.status(404).body(messageErrorService);
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
        if ("insertBairro".equals(methodName)) {
            message = "Não foi possível incluir bairro no banco de dados. Tipo de dado inválido.";
        } else if ("updateBairro".equals(methodName)) {
            message = "Não foi possível alterar bairro no banco de dados. Tipo de dado inválido.";
        } else {
            message = "Não foi possível processar a requisição.";
        }

        return ResponseEntity.status(404).body(new MessageErrorService(message, 404));
    }
}
