package com.tj.designacoes.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "VMC_PARTE_PARTICIPANTE")
public class VmcParteParticipante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "VMC_PARTE_ID", nullable = false)
    private VmcParte vmcParte;

    @Column(name = "NOME", nullable = false)
    private String nome;

}