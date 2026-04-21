package com.tj.designacoes.dto;

import lombok.Data;

import java.util.List;
@Data
public class VmcParte {
    private Integer id;
    private String tipo;
    private List<VmcParteParticipante> participantes;
}
