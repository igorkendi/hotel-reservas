package com.hotelreserva.repository;

import com.hotelreserva.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Camada de persistência. Quem usa o repositório não precisa conhecer SQL
 * nem detalhes de conexão; o Spring Data gera as consultas pelo nome do método.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCpf(String cpf);

    List<Cliente> findByNomeContainingIgnoreCaseOrderByNome(String nome);
}
