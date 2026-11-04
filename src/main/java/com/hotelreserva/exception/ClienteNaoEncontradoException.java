package com.hotelreserva.exception;

/** Cliente inexistente (resposta HTTP 404). */
public class ClienteNaoEncontradoException extends RuntimeException {

    public ClienteNaoEncontradoException(Long id) {
        super("Cliente " + id + " não encontrado");
    }
}
