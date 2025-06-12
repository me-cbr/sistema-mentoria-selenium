package com.example.sistemamentoria.domain;

import java.util.Objects;

public class Tecnologia {
    private Long id;
    private String nome;
    private AreaConhecimento areaConhecimento;

    public Tecnologia(Long id, String nome, AreaConhecimento areaConhecimento) {
        this.id = id;
        this.nome = nome;
        this.areaConhecimento = areaConhecimento;
    }

    public AreaConhecimento getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimento areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }

    public void setId(Long id) { this.id = id; }

    public void setNome(String nome) { this.nome = nome; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tecnologia that = (Tecnologia) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
