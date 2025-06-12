package com.example.sistemamentoria.domain;

import java.util.Objects;

public class Avaliacao {
    private Long id;
    private int nota;

    public Avaliacao(Long id, int nota) {
        if (nota < 0 || nota > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 0 e 5.");
        }
        this.id = id;
        this.nota = nota;
    }

    public Long getId() {
        return id;
    }

    public int getNota() {
        return nota;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNota(int nota) {
        if (nota < 0 || nota > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 0 e 5.");
        }
        this.nota = nota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avaliacao avaliacao = (Avaliacao) o;
        return Objects.equals(id, avaliacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
