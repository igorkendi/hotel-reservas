package com.hotelreserva.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CpfTest {

    @Test
    void aceitaCpfValidoComOuSemMascara() {
        assertTrue(Cpf.isValido("529.982.247-25"));
        assertTrue(Cpf.isValido("52998224725"));
    }

    @Test
    void rejeitaDigitoVerificadorErradoOuSequenciaRepetida() {
        assertFalse(Cpf.isValido("529.982.247-24"));
        assertFalse(Cpf.isValido("111.111.111-11"));
        assertFalse(Cpf.isValido("123"));
    }

    @Test
    void removeMascara() {
        assertEquals("52998224725", Cpf.somenteDigitos("529.982.247-25"));
    }
}
