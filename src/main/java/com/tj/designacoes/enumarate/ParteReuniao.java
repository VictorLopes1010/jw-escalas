package com.tj.designacoes.enumarate;

import java.util.Arrays;

public enum ParteReuniao {

    TESOUROS_DA_PALAVRA_DE_DEUS("Tesouros da Palavra de Deus"),
    FACA_SEU_MELHOR_NO_MINISTERIO("Faça seu Melhor no Ministerio"),
    NOSSA_VIDA_CRISTA("Nossa Vida Cristã");

    private final String descricao;

    ParteReuniao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static ParteReuniao fromDescricao(String descricao) {
        return Arrays.stream(values())
                .filter(e -> e.descricao.equalsIgnoreCase(descricao))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Parte Reuniao inválida: " + descricao));
    }

    public static int getOrdinalByDescricao(String descricao) {
        return fromDescricao(descricao).ordinal();
    }
}