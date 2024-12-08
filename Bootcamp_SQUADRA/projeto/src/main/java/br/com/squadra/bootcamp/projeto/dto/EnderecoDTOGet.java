package br.com.squadra.bootcamp.projeto.dto;

public class EnderecoDTOGet {

    private Long codigoEndereco;
    private Long codigoPessoa;
    private Long codigoBairro;
    private String nomeRua;
    private String numero;
    private String complemento;
    private String cep;
    private BairroDTOGet bairro;

    public EnderecoDTOGet(){
    }

    public EnderecoDTOGet(Long codigoEndereco, Long codigoPessoa, Long codigoBairro, String nomeRua, String numero, String complemento, String cep, BairroDTOGet bairro) {
        this.codigoEndereco = codigoEndereco;
        this.codigoPessoa = codigoPessoa;
        this.codigoBairro = codigoBairro;
        this.nomeRua = nomeRua;
        this.numero = numero;
        this.complemento = complemento;
        this.cep = cep;
        this.bairro = bairro;
    }

    public Long getCodigoEndereco() {
        return codigoEndereco;
    }

    public void setCodigoEndereco(Long codigoEndereco) {
        this.codigoEndereco = codigoEndereco;
    }

    public Long getCodigoPessoa() {
        return codigoPessoa;
    }

    public void setCodigoPessoa(Long codigoPessoa) {
        this.codigoPessoa = codigoPessoa;
    }

    public Long getCodigoBairro() {
        return codigoBairro;
    }

    public void setCodigoBairro(Long codigoBairro) {
        this.codigoBairro = codigoBairro;
    }

    public String getNomeRua() {
        return nomeRua;
    }

    public void setNomeRua(String nomeRua) {
        this.nomeRua = nomeRua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public BairroDTOGet getBairro() {
        return bairro;
    }

    public void setBairro(BairroDTOGet bairro) {
        this.bairro = bairro;
    }
}
