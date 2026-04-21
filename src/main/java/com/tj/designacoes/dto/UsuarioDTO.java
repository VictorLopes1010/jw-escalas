package com.tj.designacoes.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer id;
    private String nome;
    private String email;
    private String senha;
    private Integer usuarioCriacaoId;
    private Integer usuarioAlteracaoId;
}
