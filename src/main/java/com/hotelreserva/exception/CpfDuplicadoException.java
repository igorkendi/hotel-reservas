package com.hotelreserva.exception;

/** O CPF informado já pertence a outro cliente (resposta HTTP 409). */
public class CpfDuplicadoException extends RuntimeException {

    public CpfDuplicadoException() {
        super("Já existe um cliente cadastrado com este CPF");
    }
}
