package com.hotelreserva.controller;

import com.hotelreserva.dto.ClienteResponse;
import com.hotelreserva.exception.CpfDuplicadoException;
import com.hotelreserva.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Testa só a borda HTTP: formato da entrada, códigos de status e JSON. */
@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClienteService service;

    private static final String JSON_VALIDO = """
            {"nome":"Ana Souza","cpf":"529.982.247-25","dataNascimento":"1995-05-10",
             "telefone":"(34) 99999-0001","email":"ana@email.com"}
            """;

    @Test
    void retorna201AoCadastrar() throws Exception {
        when(service.cadastrar(any())).thenReturn(new ClienteResponse(1L, "Ana Souza", "***.***.***-25",
                LocalDate.of(1995, 5, 10), "(34) 99999-0001", "ana@email.com", null));

        mvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cpfMascarado").value("***.***.***-25"));
    }

    @Test
    void retorna400QuandoFormatoInvalidoSemChamarService() throws Exception {
        mvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\",\"cpf\":\"12\",\"telefone\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.cpf").exists());
        verify(service, never()).cadastrar(any());
    }

    @Test
    void retorna409QuandoCpfDuplicado() throws Exception {
        when(service.cadastrar(any())).thenThrow(new CpfDuplicadoException());

        mvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andExpect(status().isConflict());
    }
}
