package com.fourcatsdev.aula01.Model;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Usuario {
    private final int MÍNIMO_CARACTERES = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = MÍNIMO_CARACTERES, message = "O nome deve conter no mínimo " + MÍNIMO_CARACTERES + " caracteres!")
    private String nome;

    @CPF(message = "CPF inválido")
    private String cpf;

    @Basic
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern     = "dd/MM/yyyy")
    private Date dataNascimento;

    @Email(message = "Email inválido")
    private String email;

    @NotEmpty
    @Size(min     = MÍNIMO_CARACTERES, message    = "O mínimo de carácteres deve ser " + MÍNIMO_CARACTERES)
    private String password;

    @NotEmpty
    @Size(min    = MÍNIMO_CARACTERES, message     = "O login deve ser informado com no mínimo " + MÍNIMO_CARACTERES + " caracteres")
    private String login;
    private boolean ativo;

    @ManyToMany (fetch    = FetchType.EAGER)
    @JoinTable (name      = "usuario_papel", joinColumns    = @JoinColumn (name   = "usuario_id"), inverseJoinColumns    = @JoinColumn (name    = "papel_id"))
    private List <Papel> papeis;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome    = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf    = cpf;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento    = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email    = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password    = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login    = login;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo    = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id    = id;
    }

    public List <Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(List <Papel> papeis) {
        this.papeis = papeis;
    }

}