package org.example.controller;

import org.example.model.UsuarioModel;
import org.example.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @RestController
    @RequestMapping("/usuarios")
    @CrossOrigin(origins = "*")
    class UsuarioRestController {

        @GetMapping
        public List<UsuarioModel> listar() {
            return service.listar();
        }

        @GetMapping("/{id}")
        public ResponseEntity<UsuarioModel> buscar(@PathVariable Long id) {
            UsuarioModel usuario = service.buscar(id);
            if (usuario == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(usuario);
        }

        @PostMapping
        public ResponseEntity<UsuarioModel> criar(@RequestBody UsuarioModel usuario) {
            try {
                service.criar(usuario.getNome(), usuario.getEmail());
                return ResponseEntity.ok(usuario);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        @PutMapping("/{id}")
        public ResponseEntity<UsuarioModel> atualizar(@PathVariable Long id, @RequestBody UsuarioModel usuario) {
            boolean ok = service.atualizar(id, usuario.getNome(), usuario.getEmail());
            if (!ok) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(usuario);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletar(@PathVariable Long id) {
            boolean ok = service.deletar(id);
            if (!ok) return ResponseEntity.notFound().build();
            return ResponseEntity.noContent().build();
        }
    }
}