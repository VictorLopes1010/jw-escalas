package com.tj.designacoes.service.impl;

import com.tj.designacoes.service.EscalaExcelService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EscalaExcelServiceImpl implements EscalaExcelService {

    @Override
    public byte[] gerarExcel(
            List<String> pessoas,
            List<String> funcoes,
            List<DayOfWeek> diasSemana,
            LocalDate inicio,
            LocalDate fim,
            List<String> pessoasDiaAnterior
    ) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Escala");

        Locale localePtBr = new Locale("pt", "BR");
        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /* =======================
       ESTILOS
     ======================= */

        CellStyle tituloStyle = workbook.createCellStyle();
        Font tituloFont = workbook.createFont();
        tituloFont.setBold(true);
        tituloFont.setFontHeightInPoints((short) 16);
        tituloStyle.setFont(tituloFont);
        tituloStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

    /* =======================
       TÍTULO
     ======================= */

        Row tituloRow = sheet.createRow(0);
        Cell tituloCell = tituloRow.createCell(0);
        tituloCell.setCellValue("Escala de Designações - Equipe de Som");
        tituloCell.setCellStyle(tituloStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, funcoes.size() + 1));

    /* =======================
       CABEÇALHO
     ======================= */

        Row header = sheet.createRow(2);

        header.createCell(0).setCellValue("Data");
        header.createCell(1).setCellValue("Dia da Semana");

        for (int i = 0; i < funcoes.size(); i++) {
            header.createCell(i + 2).setCellValue(funcoes.get(i));
        }

        for (int i = 0; i < funcoes.size() + 2; i++) {
            header.getCell(i).setCellStyle(headerStyle);
        }

    /* =======================
       CONTROLES GLOBAIS
     ======================= */

        int rowIndex = 3;
        Random random = new Random();

        // Controle de carga mensal
        Map<String, Integer> cargaPorPessoa = new HashMap<>();
        pessoas.forEach(p -> cargaPorPessoa.put(p, 0));

        // Histórico de duplas
        Map<Set<String>, Integer> duplaHistorico = new HashMap<>();

        // Controle de quem fez qual função no dia anterior
        Map<Integer, String> funcaoDiaAnterior = new HashMap<>();

    /* =======================
       GERAÇÃO DA ESCALA
     ======================= */

        for (LocalDate data = inicio; !data.isAfter(fim); data = data.plusDays(1)) {

            if (!diasSemana.contains(data.getDayOfWeek())) {
                continue;
            }

            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(data.format(formatterData));

            String nomeDia = data.getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, localePtBr);

            row.createCell(1).setCellValue(
                    nomeDia.substring(0, 1).toUpperCase() +
                            nomeDia.substring(1).toLowerCase()
            );

            List<String> candidatas = new ArrayList<>(pessoas);
            Set<String> pessoasHoje = new HashSet<>();

            for (int i = 0; i < funcoes.size(); i++) {

                int indiceFuncao = i;

                String escolhida = candidatas.stream()
                        .min(Comparator
                                // 1️⃣ menor carga mensal
                                .comparingInt((String p) -> cargaPorPessoa.get(p))

                                // 2️⃣ penaliza repetir mesma função do dia anterior
                                .thenComparing(p -> {
                                    String ontem = funcaoDiaAnterior.get(indiceFuncao);
                                    return (ontem != null && ontem.equals(p)) ? 1 : 0;
                                })

                                // 3️⃣ penaliza repetição de dupla
                                .thenComparing(p ->
                                        calcularPesoDupla(p, pessoasHoje, duplaHistorico)
                                )

                                // 4️⃣ desempate aleatório
                                .thenComparing(p -> random.nextInt())
                        )
                        .orElseThrow();

                row.createCell(i + 2).setCellValue(escolhida);

                pessoasHoje.add(escolhida);
                candidatas.remove(escolhida);

                cargaPorPessoa.put(
                        escolhida,
                        cargaPorPessoa.get(escolhida) + 1
                );

                // Atualiza função do dia anterior
                funcaoDiaAnterior.put(indiceFuncao, escolhida);
            }

            atualizarHistoricoDuplas(pessoasHoje, duplaHistorico);

            for (int i = 0; i < funcoes.size() + 2; i++) {
                row.getCell(i).setCellStyle(cellStyle);
            }
        }

    /* =======================
       AJUSTE DE COLUNAS
     ======================= */

        for (int i = 0; i < funcoes.size() + 2; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }

    private int calcularPesoDupla(
            String candidata,
            Set<String> grupoAtual,
            Map<Set<String>, Integer> historico
    ) {
        int peso = 0;

        for (String pessoa : grupoAtual) {
            Set<String> dupla = new HashSet<>();
            dupla.add(candidata);
            dupla.add(pessoa);

            peso += historico.getOrDefault(dupla, 0);
        }

        return peso;
    }

    private void atualizarHistoricoDuplas(
            Set<String> grupo,
            Map<Set<String>, Integer> historico
    ) {
        List<String> lista = new ArrayList<>(grupo);

        for (int i = 0; i < lista.size(); i++) {
            for (int j = i + 1; j < lista.size(); j++) {

                Set<String> dupla = new HashSet<>();
                dupla.add(lista.get(i));
                dupla.add(lista.get(j));

                historico.put(
                        dupla,
                        historico.getOrDefault(dupla, 0) + 1
                );
            }
        }
    }
}
