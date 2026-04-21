package com.tj.designacoes.dto;

import lombok.Data;

import java.util.List;
@Data
public class VmcParteDTO {
    private Integer id;
    private String tipo;
    private List<VmcParteParticipanteDTO> participantes;
}
