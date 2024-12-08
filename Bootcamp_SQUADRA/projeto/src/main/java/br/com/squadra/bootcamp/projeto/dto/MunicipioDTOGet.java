package br.com.squadra.bootcamp.projeto.dto;

import br.com.squadra.bootcamp.projeto.model.entities.Uf;

public class MunicipioDTOGet {

    private Long codigoMunicipio;
    private Long codigoUF;
    private String nome;
    private Integer status;
    private UfDTO uf;

    public MunicipioDTOGet(){
    }

    public MunicipioDTOGet(Long codigoMunicipio, Long codigoUF, String nome, Integer status, UfDTO uf) {
        this.codigoMunicipio = codigoMunicipio;
        this.codigoUF = codigoUF;
        this.nome = nome;
        this.status = status;
        this.uf = uf;
    }

    public Long getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(Long codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public Long getCodigoUF() {
        return codigoUF;
    }

    public void setCodigoUF(Long codigoUF) {
        this.codigoUF = codigoUF;
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

    public UfDTO getUf() {
        return uf;
    }

    public void setUf(UfDTO uf) {
        this.uf = uf;
    }
}
