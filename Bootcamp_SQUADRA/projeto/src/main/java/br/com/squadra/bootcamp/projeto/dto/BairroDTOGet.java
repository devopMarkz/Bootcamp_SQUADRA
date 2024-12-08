package br.com.squadra.bootcamp.projeto.dto;

public class BairroDTOGet {

    private Long codigoBairro;
    private Long codigoMunicipio;
    private String nome;
    private Integer status;
    private MunicipioDTOGet municipio;

    public BairroDTOGet(){
    }

    public BairroDTOGet(Long codigoBairro, Long codigoMunicipio, String nome, Integer status, MunicipioDTOGet municipio) {
        this.codigoBairro = codigoBairro;
        this.codigoMunicipio = codigoMunicipio;
        this.nome = nome;
        this.status = status;
        this.municipio = municipio;
    }

    public Long getCodigoBairro() {
        return codigoBairro;
    }

    public void setCodigoBairro(Long codigoBairro) {
        this.codigoBairro = codigoBairro;
    }

    public Long getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(Long codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public MunicipioDTOGet getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MunicipioDTOGet municipio) {
        this.municipio = municipio;
    }
}
