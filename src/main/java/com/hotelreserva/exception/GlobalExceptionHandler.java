package com.hotelreserva.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/** Converte exceções em respostas HTTP padronizadas, fora do Controller. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public record Erro(int status, String mensagem, Map<String, String> campos) {
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Erro> formatoInvalido(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return resposta(HttpStatus.BAD_REQUEST, "Dados inválidos", campos);
    }

    @ExceptionHandler(CpfDuplicadoException.class)
    public ResponseEntity<Erro> cpfDuplicado(CpfDuplicadoException ex) {
        return resposta(HttpStatus.CONFLICT, ex.getMessage(), Map.of());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Erro> regraNegocio(RegraNegocioException ex) {
        return resposta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), Map.of());
    }

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<Erro> naoEncontrado(ClienteNaoEncontradoException ex) {
        return resposta(HttpStatus.NOT_FOUND, ex.getMessage(), Map.of());
    }

    private ResponseEntity<Erro> resposta(HttpStatus status, String mensagem, Map<String, String> campos) {
        return ResponseEntity.status(status).body(new Erro(status.value(), mensagem, campos));
    }
}
