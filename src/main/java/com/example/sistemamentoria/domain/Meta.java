package com.example.sistemamentoria.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Meta {
    private Long id;
    private String descricao;
    private String status;
    private LocalDateTime prazo;

    public Meta(Long id, String descricao, String status, LocalDateTime prazo) {
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.prazo = prazo;
    }

    public void atualizarStatus(String novoStatus) {
        if (novoStatus != null && !novoStatus.trim().isEmpty()) {
            this.status = novoStatus;
        }
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getPrazo() {
        return prazo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrazo(LocalDateTime prazo) {
        this.prazo = prazo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meta meta = (Meta) o;
        return Objects.equals(id, meta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
