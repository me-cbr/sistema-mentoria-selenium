package com.example.sistemamentoria.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Agenda {
    private Long id;
    private List<LocalDateTime> horariosDisponiveis;

    public Agenda(Long id) {
        this.id = id;
        this.horariosDisponiveis = new ArrayList<>();
    }

    public void adicionarHorario(LocalDateTime horario) {
        if (horario != null && !horariosDisponiveis.contains(horario)) {
            horariosDisponiveis.add(horario);
        }
    }

    public void removerHorario(LocalDateTime horario) {
        horariosDisponiveis.remove(horario);
    }

    public Long getId() {
        return id;
    }

    public List<LocalDateTime> getHorariosDisponiveis() {
        return new ArrayList<>(horariosDisponiveis);
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agenda agenda = (Agenda) o;
        return Objects.equals(id, agenda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
