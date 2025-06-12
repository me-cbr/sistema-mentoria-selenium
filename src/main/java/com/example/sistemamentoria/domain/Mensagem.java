package com.example.sistemamentoria.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Mensagem {
    private Long id;
    private Usuario remetente;
    private Usuario destinatario;
    private String conteudo;
    private LocalDateTime dataEnvio;

    public Mensagem(Long id, Usuario remetente, Usuario destinatario, String conteudo, LocalDateTime dataEnvio) {
        if (remetente == null || destinatario == null || conteudo == null || dataEnvio == null) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos.");
        }
        this.id = id;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
    }

    public Long getId() {
        return id;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRemetente(Usuario remetente) {
        this.remetente = remetente;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mensagem mensagem = (Mensagem) o;
        return Objects.equals(id, mensagem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
