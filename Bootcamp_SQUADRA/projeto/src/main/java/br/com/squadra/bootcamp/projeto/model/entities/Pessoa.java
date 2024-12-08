package br.com.squadra.bootcamp.projeto.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pessoa {

    private Long codigoPessoa;
    private String nome;
    private String sobrenome;
    private Integer idade;
    private String login;
    private String senha;
    private Integer status;
    private List<Endereco> enderecos = new ArrayList<>();

    public Pessoa() {}

    public Pessoa(Long codigoPessoa, String nome, String sobrenome, Integer idade, String login, String senha, Integer status) {
        this.codigoPessoa = codigoPessoa;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.idade = idade;
        this.login = login;
        this.senha = senha;
        this.status = status;
    }

    public Long getCodigoPessoa() {
        return codigoPessoa;
    }

    public void setCodigoPessoa(Long codigoPessoa) {
        this.codigoPessoa = codigoPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(codigoPessoa, pessoa.codigoPessoa);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigoPessoa);
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "codigoPessoa=" + codigoPessoa +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", idade=" + idade +
                ", login='" + login + '\'' +
                ", senha='" + senha + '\'' +
                ", status=" + status +
                ", enderecos=" + enderecos +
                '}';
    }
}