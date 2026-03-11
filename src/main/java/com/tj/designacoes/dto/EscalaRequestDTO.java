package com.tj.designacoes.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class EscalaRequestDTO {
    private List<String> pessoas;
    private List<String> funcoes;
    private List<String> diasSemana;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private List<String> pessoasDiaAnterior;
}
