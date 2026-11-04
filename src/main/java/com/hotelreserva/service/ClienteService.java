package com.hotelreserva.service;

import com.hotelreserva.dto.ClienteRequest;
import com.hotelreserva.dto.ClienteResponse;
import com.hotelreserva.exception.ClienteNaoEncontradoException;
import com.hotelreserva.exception.CpfDuplicadoException;
import com.hotelreserva.exception.RegraNegocioException;
import com.hotelreserva.mapper.ClienteMapper;
import com.hotelreserva.model.Cliente;
import com.hotelreserva.model.Cpf;
import com.hotelreserva.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/**
 * Camada de aplicação: orquestra o caso de uso "Cadastrar cliente" (US02 / UC02)
 * e concentra as regras. Não conhece HTTP nem tela.
 */
@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;
    private final Clock clock;

    public ClienteService(ClienteRepository repository, ClienteMapper mapper, Clock clock) {
        this.repository = repository;
        this.mapper = mapper;
        this.clock = clock;
    }

    @Transactional
    public ClienteResponse cadastrar(ClienteRequest dto) {
        // UC "Validar CPF" (<<include>>): dígitos verificadores
        if (!Cpf.isValido(dto.cpf())) {
            throw new RegraNegocioException("CPF inválido");
        }
        // Regra: CPF não pode se repetir
        if (repository.existsByCpf(Cpf.somenteDigitos(dto.cpf()))) {
            throw new CpfDuplicadoException();
        }

        Cliente cliente = mapper.toEntity(dto);

        // Regra de domínio: titular precisa ser maior de idade
        if (!cliente.isMaiorDeIdade(LocalDate.now(clock))) {
            throw new RegraNegocioException("Cliente precisa ter ao menos "
                    + Cliente.IDADE_MINIMA_TITULAR + " anos para ser titular de reserva");
        }

        return mapper.toResponse(repository.save(cliente));
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> buscarPorNome(String nome) {
        String termo = nome == null ? "" : nome.trim();
        return repository.findByNomeContainingIgnoreCaseOrderByNome(termo).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
