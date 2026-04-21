package com.tj.designacoes.service.impl;

import com.tj.designacoes.dto.Vmc;
import com.tj.designacoes.dto.VmcParte;
import com.tj.designacoes.dto.VmcParteParticipante;
import com.tj.designacoes.enumarate.ParteReuniao;
import com.tj.designacoes.repository.VmcRepository;
import com.tj.designacoes.service.VmcService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VmcServiceImpl implements VmcService {

    private final VmcRepository vmcRepository;

    @Override
    @Transactional
    public com.tj.designacoes.entity.Vmc salvar(Vmc dto) {

        com.tj.designacoes.entity.Vmc vmc;

        if (dto.getId() != null) {
            vmc = vmcRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("VMC não encontrado"));
        } else {
            vmc = new com.tj.designacoes.entity.Vmc();
        }

        vmc.setDataInicio(dto.getDataInicio());
        vmc.setDataFim(dto.getDataFim());

        Map<Integer, com.tj.designacoes.entity.VmcParte> partesExistentes = new HashMap<>();

        if (vmc.getPartes() != null) {
            for (com.tj.designacoes.entity.VmcParte p : vmc.getPartes()) {
                partesExistentes.put(p.getId(), p);
            }
        } else {
            vmc.setPartes(new ArrayList<>());
        }

        List<com.tj.designacoes.entity.VmcParte> novasPartes = new ArrayList<>();

        for (VmcParte parteDTO : dto.getPartes()) {

            com.tj.designacoes.entity.VmcParte parte;

            if (parteDTO.getId() != null && partesExistentes.containsKey(parteDTO.getId())) {
                parte = partesExistentes.get(parteDTO.getId());
            } else {
                parte = new com.tj.designacoes.entity.VmcParte();
                parte.setVmc(vmc);
            }

            parte.setParteReuniao(ParteReuniao.fromDescricao(parteDTO.getTipo()));

            Map<Integer, com.tj.designacoes.entity.VmcParteParticipante> participantesExistentes = new HashMap<>();

            if (parte.getParticipantes() != null) {
                for (com.tj.designacoes.entity.VmcParteParticipante p : parte.getParticipantes()) {
                    participantesExistentes.put(p.getId(), p);
                }
            } else {
                parte.setParticipantes(new ArrayList<>());
            }

            List<com.tj.designacoes.entity.VmcParteParticipante> novosParticipantes = new ArrayList<>();

            for (VmcParteParticipante partDTO : parteDTO.getParticipantes()) {

                com.tj.designacoes.entity.VmcParteParticipante participante;

                if (partDTO.getId() != null && participantesExistentes.containsKey(partDTO.getId())) {
                    participante = participantesExistentes.get(partDTO.getId());
                } else {
                    participante = new com.tj.designacoes.entity.VmcParteParticipante();
                    participante.setVmcParte(parte);
                }

                participante.setNome(partDTO.getNome());

                novosParticipantes.add(participante);
            }

            parte.getParticipantes().clear();
            parte.getParticipantes().addAll(novosParticipantes);

            novasPartes.add(parte);
        }

        vmc.getPartes().clear();
        vmc.getPartes().addAll(novasPartes);

        return vmcRepository.save(vmc);
    }
}