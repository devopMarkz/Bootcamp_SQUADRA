package br.com.squadra.bootcamp.projeto.model.entities;

import java.util.Objects;

public class Uf {

    private Long codigoUF;
    private String sigla;
    private String nome;
    private Integer status;

    public Uf() {}

    public Uf(Long codigoUF, String sigla, String nome, Integer status) {
        this.codigoUF = codigoUF;
        this.sigla = sigla.toUpperCase();
        this.nome = nome.toUpperCase();
        this.status = status;
    }

    public Long getCodigoUF() {
        return codigoUF;
    }

    public void setCodigoUF(Long codigoUf) {
        this.codigoUF = codigoUf;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
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
        if (!(o instanceof Uf)) return false;
        Uf uf = (Uf) o;
        return Objects.equals(codigoUF, uf.codigoUF);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoUF);
    }

    @Override
    public String toString() {
        return "Uf{" +
                "codigoUF=" + codigoUF +
                ", sigla='" + sigla + '\'' +
                ", nome='" + nome + '\'' +
                ", status=" + status +
                '}';
    }
}