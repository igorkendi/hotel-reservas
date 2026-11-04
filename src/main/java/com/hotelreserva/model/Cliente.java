package com.hotelreserva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.Period;

/**
 * Entidade de domínio. Guarda os dados do cliente e as regras que dependem
 * apenas dele mesmo (ex.: maioridade). Não conhece tela, HTTP nem SQL.
 */
@Entity
public class Cliente {

    public static final int IDADE_MINIMA_TITULAR = 18;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private String telefone;

    private String email;

    @Embedded
    private Endereco endereco;

    protected Cliente() {
        // exigido pela JPA
    }

    public Cliente(String nome, String cpf, LocalDate dataNascimento, String telefone, String email, Endereco endereco) {
        this.nome = nome;
        this.cpf = Cpf.somenteDigitos(cpf);
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    /** Regra de domínio: só maiores de idade podem ser titulares de reserva. */
    public boolean isMaiorDeIdade(LocalDate hoje) {
        return Period.between(dataNascimento, hoje).getYears() >= IDADE_MINIMA_TITULAR;
    }

    public void atualizarContato(String telefone, String email) {
        this.telefone = telefone;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public Endereco getEndereco() { return endereco; }
}
