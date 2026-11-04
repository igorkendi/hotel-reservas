package com.hotelreserva.model;

/**
 * Regras do CPF (conceito do domínio). Fica no model porque não depende de
 * tela, banco ou framework: pode ser testada isoladamente.
 */
public final class Cpf {

    private Cpf() {
    }

    public static String somenteDigitos(String cpf) {
        return cpf == null ? null : cpf.replaceAll("\\D", "");
    }

    /** Valida tamanho e os dois dígitos verificadores. */
    public static boolean isValido(String cpf) {
        String d = somenteDigitos(cpf);
        if (d == null || d.length() != 11 || d.chars().distinct().count() == 1) {
            return false;
        }
        return digito(d, 9) == d.charAt(9) - '0' && digito(d, 10) == d.charAt(10) - '0';
    }

    private static int digito(String d, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (d.charAt(i) - '0') * (tamanho + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}
