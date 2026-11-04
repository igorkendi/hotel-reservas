package com.hotelreserva.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

/**
 * Contrato de entrada do cadastro. As anotações validam apenas o FORMATO
 * (campo preenchido, padrão do CPF, e-mail bem formado). Regras de negócio
 * ficam no Service.
 */
public record ClienteRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}", message = "CPF deve ter 11 dígitos")
        String cpf,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve estar no passado")
        LocalDate dataNascimento,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @Email(message = "E-mail inválido")
        String email,

        @Valid
        EnderecoDTO endereco
) {
}
