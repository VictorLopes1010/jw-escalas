package com.tj.designacoes.entity;

import com.tj.designacoes.enumarate.ParteReuniao;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "VMC_PARTE")
public class VmcParte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "VMC_ID", nullable = false)
    private Vmc vmc;

    @Column(name = "DATA", nullable = false)
    private LocalDateTime data;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "PARTE_REUNIAO", nullable = false)
    private ParteReuniao parteReuniao;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "DURACAO")
    private Integer duracao;

    @Column(name = "NUMERO")
    private Integer numero;

    @OneToMany(mappedBy = "vmcParte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VmcParteParticipante> participantes;

}