package com.hotelreserva.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoDTO(
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve ter 8 dígitos")
        String cep,
        String logradouro,
        String numero,
        String cidade,
        @Size(min = 2, max = 2, message = "UF deve ter 2 letras")
        String uf
) {
}
