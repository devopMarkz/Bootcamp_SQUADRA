package br.com.squadra.bootcamp.projeto.controller;

import br.com.squadra.bootcamp.projeto.dto.MunicipioDTO;
import br.com.squadra.bootcamp.projeto.service.MessageErrorService;
import br.com.squadra.bootcamp.projeto.service.MunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;

import java.util.List;
import java.util.Optional;

/**
 * Controlador responsável pelas operações relacionadas aos municípios, como
 * inserção, atualização e consulta dos municípios através da API.
 * A classe contém métodos para manipulação de dados dos municípios, validando as entradas
 * e retornando respostas adequadas ao cliente.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/municipio")
public class MunicipioController {

    @Autowired
    private MunicipioService municipioService;  // Serviço para operações com municípios

    /**
     * Metodo GET que permite consultar municípios com base nos parâmetros fornecidos.
     *
     * @param codigoMunicipio Código do município (opcional)
     * @param codigoUF Código da unidade federativa (opcional)
     * @param nome Nome do município (opcional)
     * @param status Status do município (opcional)
     * @return Lista de municípios ou um único município, dependendo do filtro aplicado.
     */
    @GetMapping
    public ResponseEntity<?> getMunicipios(
            @RequestParam(required = false) String codigoMunicipio,
            @RequestParam(required = false) String codigoUF,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String status) {

        Long codigoMunicipioLong = null;
        Long codigoUfLong = null;
        Integer statusInt = null;

        // Validação para codigoMunicipio: assegura que apenas números são aceitos
        if (codigoMunicipio != null) {
            try {
                codigoMunicipioLong = Long.parseLong(codigoMunicipio);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(404)
                        .body(new MessageErrorService(
                                "Não foi possível consultar Município no banco de dados. O campo codigoMunicipio deve conter apenas números.", 404));
            }
        }

        // Validação para codigoUF: assegura que apenas números são aceitos
        if (codigoUF != null) {
            try {
                codigoUfLong = Long.parseLong(codigoUF);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(404)
                        .body(new MessageErrorService(
                                "Não foi possível consultar Município no banco de dados. O campo codigoUF deve conter apenas números.", 404));
            }
        }

        // Validação para status: assegura que apenas números são aceitos
        if (status != null) {
            try {
                statusInt = Integer.parseInt(status);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(404)
                        .body(new MessageErrorService(
                                "Não foi possível consultar Município no banco de dados. O campo status deve conter apenas números.", 404));
            }
        }

        // Busca os municípios com os filtros validados
        List<MunicipioDTO> result = municipioService.findByFilters(
                Optional.ofNullable(codigoMunicipioLong),
                Optional.ofNullable(codigoUfLong),
                Optional.ofNullable(nome),
                Optional.ofNullable(statusInt));

        // Se apenas codigoMunicipio for fornecido e encontrar um único resultado, retorna o objeto
        if (codigoMunicipioLong != null && result.size() == 1) {
            return ResponseEntity.ok(result.get(0));
        }

        // Caso contrário, retorna a lista de municípios ou uma lista vazia
        return ResponseEntity.ok(result);
    }

    /**
     * Metodo POST para inserir um novo município.
     *
     * @param municipioDTO Dados do município a ser inserido
     * @return Lista atualizada de municípios ou uma mensagem de erro caso não seja possível inserir o município.
     */
    @PostMapping
    public ResponseEntity<?> insertMunicipio(@RequestBody MunicipioDTO municipioDTO) {
        try {
            MessageErrorService messageErrorService = municipioService.validatePostMunicipio(municipioDTO);
            if (messageErrorService == null) {
                municipioService.insert(municipioDTO);
                return ResponseEntity.status(200).body(municipioService.findAll());
            } else {
                return ResponseEntity.status(404).body(messageErrorService);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(
                    new MessageErrorService("Não foi possível cadastrar município no banco de dados.", 404)
            );
        }
    }

    /**
     * Metodo PUT para atualizar os dados de um município existente.
     *
     * @param municipioDTO Dados do município a ser atualizado
     * @return Lista atualizada de municípios ou uma mensagem de erro caso não seja possível atualizar o município.
     */
    @PutMapping
    public ResponseEntity<?> updateMunicipio(@RequestBody MunicipioDTO municipioDTO){
        MessageErrorService messageErrorService = municipioService.validatePutMunicipio(municipioDTO);
        if(messageErrorService == null) {
            municipioService.update(municipioDTO);
            return ResponseEntity.status(200).body(municipioService.findAll());
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
    public ResponseEntity<MessageErrorService> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HandlerMethod handlerMethod) {
        String methodName = handlerMethod.getMethod().getName();
        String message;

        // Verifica qual metodo causou a exceção e define a mensagem de erro adequada
        if ("insertMunicipio".equals(methodName)) {
            message = "Não foi possível cadastrar município no banco de dados. Tipo de dado inválido.";
        } else if ("updateMunicipio".equals(methodName)) {
            message = "Não foi possível alterar município no banco de dados. Tipo de dado inválido.";
        } else {
            message = "Não foi possível processar a requisição.";
        }

        return ResponseEntity.status(404).body(new MessageErrorService(message, 404));
    }
}
