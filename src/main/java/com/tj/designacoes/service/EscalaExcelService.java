package com.tj.designacoes.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface EscalaExcelService {
    byte[] gerarExcel(
            List<String> pessoas,
            List<String> funcoes,
            List<DayOfWeek> diasSemana,
            LocalDate inicio,
            LocalDate fim,
            List<String> pessoasDiaAnterior
    ) throws Exception;
}
