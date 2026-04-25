package com.tj.designacoes.service.impl;

import com.tj.designacoes.dto.VmcDTO;
import com.tj.designacoes.dto.VmcParteDTO;
import com.tj.designacoes.dto.VmcParteParticipanteDTO;
import com.tj.designacoes.entity.Vmc;
import com.tj.designacoes.entity.VmcParte;
import com.tj.designacoes.entity.VmcParteParticipante;
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
    public Vmc salvar(VmcDTO dto) {

        Vmc vmc;

        if (dto.getId() != null) {
            vmc = vmcRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("VMC não encontrado"));
        } else {
            vmc = new com.tj.designacoes.entity.Vmc();
        }

        vmc.setDataInicio(dto.getDataInicio());
        vmc.setDataFim(dto.getDataFim());

        Map<Integer, VmcParte> partesExistentes = new HashMap<>();

        if (vmc.getPartes() != null) {
            for (VmcParte p : vmc.getPartes()) {
                partesExistentes.put(p.getId(), p);
            }
        } else {
            vmc.setPartes(new ArrayList<>());
        }

        List<com.tj.designacoes.entity.VmcParte> novasPartes = new ArrayList<>();

        for (VmcParteDTO parteDTO : dto.getPartes()) {

            com.tj.designacoes.entity.VmcParte parte;

            if (parteDTO.getId() != null && partesExistentes.containsKey(parteDTO.getId())) {
                parte = partesExistentes.get(parteDTO.getId());
            } else {
                parte = new com.tj.designacoes.entity.VmcParte();
                parte.setVmc(vmc);
            }

            parte.setParteReuniao(ParteReuniao.fromDescricao(parteDTO.getTipo()));

            Map<Integer, VmcParteParticipante> participantesExistentes = new HashMap<>();

            if (parte.getParticipantes() != null) {
                for (com.tj.designacoes.entity.VmcParteParticipante p : parte.getParticipantes()) {
                    participantesExistentes.put(p.getId(), p);
                }
            } else {
                parte.setParticipantes(new ArrayList<>());
            }

            List<VmcParteParticipante> novosParticipantes = new ArrayList<>();

            for (VmcParteParticipanteDTO partDTO : parteDTO.getParticipantes()) {

                VmcParteParticipante participante;

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