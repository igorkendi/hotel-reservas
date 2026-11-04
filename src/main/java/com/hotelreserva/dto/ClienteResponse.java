package com.hotelreserva.dto;

import java.time.LocalDate;

/** Contrato de saída: o CPF volta mascarado para não expor o dado completo. */
public record ClienteResponse(
        Long id,
        String nome,
        String cpfMascarado,
        LocalDate dataNascimento,
        String telefone,
        String email,
        String enderecoCompleto
) {
}
