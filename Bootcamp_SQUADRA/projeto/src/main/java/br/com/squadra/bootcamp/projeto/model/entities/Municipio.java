package br.com.squadra.bootcamp.projeto.model.entities;

import java.util.Objects;

public class Municipio {

    private Long codigoMunicipio;
    private Uf uf;
    private String nome;
    private Integer status;

    public Municipio() {}

    public Municipio(Long codigoMunicipio, Uf uf, String nome, Integer status) {
        this.codigoMunicipio = codigoMunicipio;
        this.uf = uf;
        this.nome = nome;
        this.status = status;
    }

    public Long getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(Long codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public Uf getUf() {
        return uf;
    }

    public void setUf(Uf uf) {
        this.uf = uf;
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
        if (!(o instanceof Municipio)) return false;
        Municipio that = (Municipio) o;
        return Objects.equals(codigoMunicipio, that.codigoMunicipio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoMunicipio);
    }

    @Override
    public String toString() {
        return "Municipio{" +
                "codigoMunicipio=" + codigoMunicipio +
                ", uf=" + uf +
                ", nome='" + nome + '\'' +
                ", status=" + status +
                '}';
    }
}