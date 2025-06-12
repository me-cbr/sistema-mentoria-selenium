package com.example.sistemamentoria.domain;

public class Mentorado extends Usuario {
    private PlanoEstudo planoEstudo;

    public Mentorado(Long id, String nome, String email, String senha) {
        super(id, nome, email, senha);
    }

    public PlanoEstudo getPlanoEstudo() {
        return planoEstudo;
    }

    public void setPlanoEstudo(PlanoEstudo planoEstudo) {
        this.planoEstudo = planoEstudo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
