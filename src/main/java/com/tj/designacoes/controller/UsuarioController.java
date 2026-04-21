package com.tj.designacoes.controller;

import com.tj.designacoes.dto.Usuario;
import com.tj.designacoes.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/criacao")
    public ResponseEntity<Integer> criarUsuario(@RequestBody Usuario usuario){
        return ResponseEntity.ok(service.salvarUsuario(usuario));
    }

}
