package com.tj.designacoes.controller;

import com.tj.designacoes.dto.LoginRequest;
import com.tj.designacoes.dto.LoginResponse;
import com.tj.designacoes.entity.Usuario;
import com.tj.designacoes.exception.RecursoNaoEncontradoException;
import com.tj.designacoes.exception.RegraNegocioException;
import com.tj.designacoes.repository.UsuarioRepository;
import com.tj.designacoes.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        Usuario usuario = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new RegraNegocioException("Senha inválida");
        }

        String token = jwtService.gerarToken(usuario.getEmail());

        return new LoginResponse(token);
    }
}