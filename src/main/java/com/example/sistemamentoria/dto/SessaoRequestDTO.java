package com.example.sistemamentoria.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

public class SessaoRequestDTO {

    private Long mentoradoId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataHora;

    public Long getMentoradoId() {
        return mentoradoId;
    }

    public void setMentoradoId(Long mentoradoId) {
        this.mentoradoId = mentoradoId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}