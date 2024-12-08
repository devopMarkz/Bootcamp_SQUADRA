package br.com.squadra.bootcamp.projeto.model.entities;

import java.util.Objects;

public class Bairro {

    private Long codigoBairro;
    private Municipio municipio;
    private String nome;
    private Integer status;

    public Bairro() {}

    public Bairro(Long codigoBairro, Municipio municipio, String nome, Integer status) {
        this.codigoBairro = codigoBairro;
        this.municipio = municipio;
        this.nome = nome;
        this.status = status;
    }

    public Long getCodigoBairro() {
        return codigoBairro;
    }

    public void setCodigoBairro(Long codigoBairro) {
        this.codigoBairro = codigoBairro;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bairro)) return false;
        Bairro bairro = (Bairro) o;
        return Objects.equals(codigoBairro, bairro.codigoBairro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoBairro);
    }

    @Override
    public String toString() {
        return "Bairro{" +
                "codigoBairro=" + codigoBairro +
                ", municipio=" + municipio +
                ", nome='" + nome + '\'' +
                ", status=" + status +
                '}';
    }
}