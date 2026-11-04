package com.hotelreserva.controller;

import com.hotelreserva.dto.ClienteRequest;
import com.hotelreserva.dto.ClienteResponse;
import com.hotelreserva.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Controller enxuto: traduz HTTP em chamadas ao Service e devolve a resposta.
 * Não contém regra de negócio; o @Valid só checa o formato do DTO.
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(@Valid @RequestBody ClienteRequest dto) {
        ClienteResponse criado = service.cadastrar(dto);
        return ResponseEntity.created(URI.create("/clientes/" + criado.id())).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> buscar(@RequestParam(defaultValue = "") String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }
}
