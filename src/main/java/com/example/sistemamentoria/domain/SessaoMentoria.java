package com.example.sistemamentoria.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SessaoMentoria {
    private Long id;
    private Mentor mentor;
    private Mentorado mentorado;
    private LocalDateTime dataHora;
    private String status;
    private List<Feedback> feedbacks;

    public SessaoMentoria(Long id, Mentor mentor, Mentorado mentorado, LocalDateTime dataHora) {
        this.id = id;
        this.mentor = mentor;
        this.mentorado = mentorado;
        this.dataHora = dataHora;
        this.status = "Pendente";
        this.feedbacks = new ArrayList<>();
    }

    public void iniciarSessao() {
        if (this.status.equalsIgnoreCase("Aprovada") && LocalDateTime.now().isAfter(dataHora.minusMinutes(15)) && LocalDateTime.now().isBefore(dataHora.plusMinutes(60))) {
            this.status = "Iniciada";
            System.out.println("Sessão " + id + " iniciada com sucesso.");
        } else {
            System.out.println("Sessão " + id + " não pode ser iniciada. Verifique o status e o horário.");
        }
    }

    public void adicionarFeedback(Usuario autor, int nota, String comentario) throws FeedbackException {
        if (!this.status.equalsIgnoreCase("Finalizada")) {
            throw new IllegalStateException("Só é possível dar feedback após a sessão ser finalizada.");
        }

        for (Feedback f : this.feedbacks) {
            if (f.getAutor().equals(autor)) {
                throw new FeedbackException("Este usuário já forneceu um feedback para esta sessão.");
            }
        }

        long feedbackId = System.currentTimeMillis();
        Feedback novoFeedback = new Feedback(feedbackId, this, autor, nota, comentario);
        this.feedbacks.add(novoFeedback);
    }

    public List<Feedback> getFeedbacks() {
        return new ArrayList<>(feedbacks);
    }

    public boolean getTodosFeedbacks() {
        return feedbacks.size() == 2;
    }

    public void finalizarSessao() {
        if (this.status.equalsIgnoreCase("Iniciada") && LocalDateTime.now().isAfter(dataHora.plusMinutes(30))) {
            this.status = "Finalizada";
            System.out.println("Sessão " + id + " finalizada com sucesso.");
        } else {
            System.out.println("Sessão " + id + " não pode ser finalizada. Verifique o status e o horário.");
        }
    }

    public boolean atualizarStatusSessao(String novoStatus, String motivo) {
        if (novoStatus == null || novoStatus.trim().isEmpty()) {
            System.out.println("Novo status não pode ser nulo ou vazio.");
            return false;
        }

        String statusAtual = this.status;

        switch (novoStatus.toLowerCase()) {
            case "aprovada":
                if (statusAtual.equalsIgnoreCase("Pendente")) {
                    this.status = "Aprovada";
                    System.out.println("Sessão " + id + " aprovada com sucesso.");
                    return true;
                } else {
                    System.out.println("Sessão " + id + " não pode ser aprovada. Status atual: " + statusAtual);
                    return false;
                }

            case "recusada":
                if (statusAtual.equalsIgnoreCase("Pendente")) {
                    this.status = "Recusada";
                    System.out.println("Sessão " + id + " recusada. Motivo: " + motivo);
                    return true;
                } else {
                    System.out.println("Sessão " + id + " não pode ser recusada. Status atual: " + statusAtual);
                    return false;
                }

            case "iniciada":
                if (statusAtual.equalsIgnoreCase("Aprovada") && LocalDateTime.now().isAfter(dataHora.minusMinutes(15))) {
                    this.status = "Iniciada";
                    System.out.println("Sessão " + id + " iniciada com sucesso.");
                    return true;
                } else {
                    System.out.println("Sessão " + id + " não pode ser iniciada. Status atual: " + statusAtual);
                    return false;
                }

            case "finalizada":
                if (statusAtual.equalsIgnoreCase("Iniciada") && LocalDateTime.now().isAfter(dataHora.plusMinutes(30))) {
                    this.status = "Finalizada";
                    System.out.println("Sessão " + id + " finalizada com sucesso.");
                    return true;
                } else {
                    System.out.println("Não é possível finalizar a sessão. Status: " + statusAtual + " ou duração insuficiente.");
                    return false;
                }

            case "cancelada":
                if (statusAtual.equalsIgnoreCase("Pendente") || statusAtual.equalsIgnoreCase("Aprovada")) {
                    this.status = "Cancelada";
                    System.out.println("Sessão " + id + " cancelada. Motivo: " + (motivo != null ? motivo : ""));
                    return true;
                } else {
                    System.out.println("Não é possível cancelar uma sessão com status " + statusAtual);
                    return false;
                }

            default:
                System.out.println("Status desconhecido: " + novoStatus);
                return false;
        }
    }

    public Long getId() {
        return id;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public Mentorado getMentorado() {
        return mentorado;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public void setMentorado(Mentorado mentorado) {
        this.mentorado = mentorado;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessaoMentoria that = (SessaoMentoria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
