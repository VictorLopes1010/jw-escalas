package com.tj.designacoes.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VmcDTO {
    private Integer id;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private List<VmcParteDTO> partes;
}
