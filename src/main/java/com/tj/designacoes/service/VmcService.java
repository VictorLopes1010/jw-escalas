package com.tj.designacoes.service;

import com.tj.designacoes.dto.VmcDTO;
import com.tj.designacoes.entity.Vmc;
import jakarta.transaction.Transactional;

public interface VmcService {
    @Transactional
    Vmc salvar(VmcDTO dto);
}
