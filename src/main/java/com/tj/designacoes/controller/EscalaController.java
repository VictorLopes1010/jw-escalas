package com.tj.designacoes.controller;

import com.tj.designacoes.dto.EscalaRequestDTO;
import com.tj.designacoes.service.EscalaExcelService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/escala")
public class EscalaController {

    private final EscalaExcelService service;

    public EscalaController(EscalaExcelService service) {
        this.service = service;
    }

    @PostMapping("/excel")
    public ResponseEntity<byte[]> gerarExcel(@RequestBody EscalaRequestDTO dto) throws Exception {

        List<DayOfWeek> dias = dto.getDiasSemana()
                .stream()
                .map(d -> DayOfWeek.valueOf(d.toUpperCase()))
                .toList();

        byte[] arquivo = service.gerarExcel(
                dto.getPessoas(),
                dto.getFuncoes(),
                dias,
                dto.getDataInicio(),
                dto.getDataFim(),
                dto.getPessoasDiaAnterior()

        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=escala.xlsx")
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(arquivo);
    }
}
