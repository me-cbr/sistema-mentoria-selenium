package com.example.sistemamentoria.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlanoEstudo {
    private Long id;
    private List<Meta> metas;

    public PlanoEstudo(Long id) {
        this.id = id;
        this.metas = new ArrayList<>();
    }

    public void adicionarMeta(Meta meta) {
        if (meta != null && !metas.contains(meta)) {
            metas.add(meta);
        }
    }

    public double avaliarProgresso() {
        if (metas.isEmpty()) {
            return 0.0;
        }

        long metasConcluidas = metas.stream().filter(m -> "Concluída".equalsIgnoreCase(m.getStatus())).count();
        return (double) metasConcluidas / metas.size() * 100.0;
    }

    public Long getId() {
        return id;
    }

    public List<Meta> getMetas() {
        return new ArrayList<>(metas);
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanoEstudo that = (PlanoEstudo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
