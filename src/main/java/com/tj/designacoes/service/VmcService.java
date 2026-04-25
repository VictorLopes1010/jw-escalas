package com.tj.designacoes.service;

import com.tj.designacoes.dto.VmcDTO;
import jakarta.transaction.Transactional;

public interface VmcService {
    @Transactional
    com.tj.designacoes.entity.Vmc salvar(VmcDTO dto);
}
