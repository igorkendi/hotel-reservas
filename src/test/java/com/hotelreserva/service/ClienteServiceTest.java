package com.hotelreserva.service;

import com.hotelreserva.dto.ClienteRequest;
import com.hotelreserva.dto.ClienteResponse;
import com.hotelreserva.exception.CpfDuplicadoException;
import com.hotelreserva.exception.RegraNegocioException;
import com.hotelreserva.mapper.ClienteMapper;
import com.hotelreserva.model.Cliente;
import com.hotelreserva.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Testa as regras do Service isoladas do banco e do HTTP (repositório simulado). */
class ClienteServiceTest {

    private ClienteRepository repository;
    private ClienteService service;

    @BeforeEach
    void setUp() {
        repository = mock(ClienteRepository.class);
        Clock hoje = Clock.fixed(Instant.parse("2026-11-03T12:00:00Z"), ZoneOffset.UTC);
        service = new ClienteService(repository, new ClienteMapper(), hoje);
    }

    private ClienteRequest request(String cpf, LocalDate nascimento) {
        return new ClienteRequest("Ana Souza", cpf, nascimento, "(34) 99999-0001", "ana@email.com", null);
    }

    @Test
    void cadastraClienteValido() {
        when(repository.existsByCpf("52998224725")).thenReturn(false);
        when(repository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        ClienteResponse resposta = service.cadastrar(request("529.982.247-25", LocalDate.of(1995, 5, 10)));

        assertEquals("Ana Souza", resposta.nome());
        assertEquals("***.***.***-25", resposta.cpfMascarado());
        verify(repository).save(any(Cliente.class));
    }

    @Test
    void naoPermiteCpfDuplicado() {
        when(repository.existsByCpf("52998224725")).thenReturn(true);

        assertThrows(CpfDuplicadoException.class,
                () -> service.cadastrar(request("529.982.247-25", LocalDate.of(1995, 5, 10))));
        verify(repository, never()).save(any());
    }

    @Test
    void naoPermiteCpfInvalido() {
        assertThrows(RegraNegocioException.class,
                () -> service.cadastrar(request("111.111.111-11", LocalDate.of(1995, 5, 10))));
        verify(repository, never()).save(any());
    }

    @Test
    void naoPermiteTitularMenorDeIdade() {
        when(repository.existsByCpf(any())).thenReturn(false);

        assertThrows(RegraNegocioException.class,
                () -> service.cadastrar(request("168.995.350-09", LocalDate.of(2010, 1, 1))));
        verify(repository, never()).save(any());
    }
}
