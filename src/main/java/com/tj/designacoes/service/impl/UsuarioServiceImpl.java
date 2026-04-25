package com.tj.designacoes.service.impl;

import com.tj.designacoes.dto.UsuarioDTO;
import com.tj.designacoes.exception.RecursoNaoEncontradoException;
import com.tj.designacoes.exception.RegraNegocioException;
import com.tj.designacoes.mapper.UsuarioMapper;
import com.tj.designacoes.repository.UsuarioRepository;
import com.tj.designacoes.service.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Integer salvarUsuario(UsuarioDTO dto) {

        boolean isEdicao = dto.getId() != null;

        com.tj.designacoes.entity.Usuario usuario = isEdicao
                ? repository.findById(dto.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"))
                : mapper.toEntity(dto);

        repository.findByEmail(dto.getEmail())
                .filter(u -> !u.getId().equals(dto.getId()))
                .ifPresent(u -> {
                    throw new RegraNegocioException("Email já cadastrado");
                });

        if (isEdicao) {
            mapper.updateEntityFromDto(dto, usuario);
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        if (!isEdicao && dto.getUsuarioCriacaoId() != null) {
            com.tj.designacoes.entity.Usuario criador = repository.findById(dto.getUsuarioCriacaoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário criador não encontrado"));

            usuario.setUsuarioCriacao(criador);
        }

        if (!isEdicao) {
            usuario.setDataCriacao(LocalDateTime.now());
        }


        if (isEdicao && dto.getUsuarioAlteracaoId() != null) {
            com.tj.designacoes.entity.Usuario alterador = repository.findById(dto.getUsuarioAlteracaoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário alterador não encontrado"));

            usuario.setUsuarioAlteracao(alterador);
            usuario.setDataAlteracao(LocalDateTime.now());
        }

        return repository.save(usuario).getId();
    }
}