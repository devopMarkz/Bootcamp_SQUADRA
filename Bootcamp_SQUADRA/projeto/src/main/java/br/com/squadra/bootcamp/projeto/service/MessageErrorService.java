package br.com.squadra.bootcamp.projeto.service;

/**
 * Classe que representa uma mensagem de erro com um código de status HTTP.
 * Esta classe é utilizada para encapsular as informações de erro, como a mensagem de erro e o código de status HTTP,
 * para ser retornada em respostas de erro da aplicação.
 */
public class MessageErrorService {

    private String mensagem;  // Mensagem de erro a ser retornada
    private Integer status;   // Código de status HTTP relacionado ao erro

    /**
     * Construtor para inicializar a mensagem de erro e o código de status.
     *
     * @param mensagem A mensagem de erro a ser associada ao erro.
     * @param status O código de status HTTP relacionado ao erro (ex.: 404 para "Not Found").
     */
    public MessageErrorService(String mensagem, Integer status) {
        this.mensagem = mensagem;
        this.status = status;
    }

    /**
     * Obtém a mensagem de erro.
     *
     * @return A mensagem de erro.
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Define a mensagem de erro.
     *
     * @param mensagem A mensagem de erro a ser definida.
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * Obtém o código de status HTTP.
     *
     * @return O código de status HTTP (ex.: 404, 500).
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Define o código de status HTTP.
     *
     * @param status O código de status HTTP a ser definido.
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}
