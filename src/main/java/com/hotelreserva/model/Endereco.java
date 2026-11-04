package com.hotelreserva.model;

import jakarta.persistence.Embeddable;

/**
 * Endereço do cliente. É {@code @Embeddable} porque, no diagrama de classes,
 * a relação Cliente-Endereco é uma composição: o endereço não existe sem o cliente
 * e é gravado na mesma tabela.
 */
@Embeddable
public class Endereco {

    private String cep;
    private String logradouro;
    private String numero;
    private String cidade;
    private String uf;

    protected Endereco() {
        // exigido pela JPA
    }

    public Endereco(String cep, String logradouro, String numero, String cidade, String uf) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.cidade = cidade;
        this.uf = uf;
    }

    public String getEnderecoCompleto() {
        return String.format("%s, %s - %s/%s - CEP %s", logradouro, numero, cidade, uf, cep);
    }

    public String getCep() { return cep; }
    public String getLogradouro() { return logradouro; }
    public String getNumero() { return numero; }
    public String getCidade() { return cidade; }
    public String getUf() { return uf; }
}
