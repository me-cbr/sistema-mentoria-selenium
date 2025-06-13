package com.example.sistemamentoria.controller;

import com.example.sistemamentoria.domain.Mentor;
import com.example.sistemamentoria.domain.SessaoMentoria;
import com.example.sistemamentoria.repository.InMemoryDataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class MentorController {

    private final InMemoryDataStore dataStore;

    public MentorController(InMemoryDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @PostMapping("/gerenciar-sessao")
    public ResponseEntity<String> gerenciarSessao(
            @RequestParam String horarioProposto,
            @RequestParam Long sessaoId) {

        SessaoMentoria sessao = dataStore.findSessionById(sessaoId);

        if (sessao == null) {
            return ResponseEntity.badRequest().body("Sessão não encontrada.");
        }
        Mentor mentor = sessao.getMentor();
        LocalDateTime horario = LocalDateTime.parse(horarioProposto, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String resultado = mentor.gerenciarDisponibilidadeEAprovarSessao(horario, sessao);

        return ResponseEntity.ok(resultado);
    }
}