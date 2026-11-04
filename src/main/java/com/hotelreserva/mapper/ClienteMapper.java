package com.hotelreserva.mapper;

import com.hotelreserva.dto.ClienteRequest;
import com.hotelreserva.dto.ClienteResponse;
import com.hotelreserva.dto.EnderecoDTO;
import com.hotelreserva.model.Cliente;
import com.hotelreserva.model.Endereco;
import org.springframework.stereotype.Component;

/** Converte entre DTOs (borda da aplicação) e entidades (domínio). */
@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequest dto) {
        EnderecoDTO e = dto.endereco();
        Endereco endereco = e == null ? null
                : new Endereco(e.cep(), e.logradouro(), e.numero(), e.cidade(), e.uf());
        return new Cliente(dto.nome().trim(), dto.cpf(), dto.dataNascimento(), dto.telefone(), dto.email(), endereco);
    }

    public ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(
                c.getId(),
                c.getNome(),
                mascarar(c.getCpf()),
                c.getDataNascimento(),
                c.getTelefone(),
                c.getEmail(),
                c.getEndereco() == null ? null : c.getEndereco().getEnderecoCompleto());
    }

    private String mascarar(String cpf) {
        return "***.***.***-" + cpf.substring(cpf.length() - 2);
    }
}
