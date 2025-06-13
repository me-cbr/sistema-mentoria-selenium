package com.example.sistemamentoria.controller;

import com.example.sistemamentoria.domain.SessaoMentoria;
import com.example.sistemamentoria.repository.InMemoryDataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessaoMentoriaController {

    private final InMemoryDataStore dataStore;

    public SessaoMentoriaController(InMemoryDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @PostMapping("/atualizar-status")
    public ResponseEntity<String> atualizarStatus(
            @RequestParam Long sessaoId,
            @RequestParam String novoStatus,
            @RequestParam(required = false) String motivo) {

        SessaoMentoria sessao = dataStore.findSessionById(sessaoId);
        if (sessao == null) {
            return ResponseEntity.badRequest().body("Sessão não encontrada.");
        }

        if (novoStatus.equalsIgnoreCase("aprovada") && !sessao.getStatus().equalsIgnoreCase("Pendente")) {
            sessao.setStatus("Pendente");
        }

        boolean sucesso = sessao.atualizarStatusSessao(novoStatus, motivo);
        String statusAtual = sessao.getStatus();

        if (sucesso) {
            return ResponseEntity.ok(String.format("Status da sessão %d atualizado para %s.", sessaoId, statusAtual));
        } else {
            return ResponseEntity.badRequest().body(String.format("Sessão %d não pode ser atualizada para %s. Status atual: %s", sessaoId, novoStatus, statusAtual));
        }
    }
}