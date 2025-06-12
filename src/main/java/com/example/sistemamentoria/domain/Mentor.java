package com.example.sistemamentoria.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mentor extends Usuario {
    private String biografia;
    private List<Tecnologia> tecnologias;
    private Agenda agenda;
    private List<SessaoMentoria> minhasSessoes;

    public Mentor(Long id, String nome, String email, String senha, String biografia, Agenda agenda) {
        super(id, nome, email, senha);
        this.biografia = biografia;
        this.tecnologias = new ArrayList<>();
        this.agenda = agenda;
        this.minhasSessoes = new ArrayList<>();
    }

    public void adicionarTecnologia(Tecnologia tecnologia) {
        if (tecnologia != null && !tecnologias.contains(tecnologia)) {
            tecnologias.add(tecnologia);
        }
    }

    public List<AreaConhecimento> getAreasConhecimento() {
        if (this.tecnologias.isEmpty()) {
            return new ArrayList<>();
        }
        return this.tecnologias.stream()
                .map(Tecnologia::getAreaConhecimento)
                .distinct()
                .collect(Collectors.toList());
    }

    public void adicionarSessao(SessaoMentoria sessao) {
        if (sessao != null && !minhasSessoes.contains(sessao)) {
            minhasSessoes.add(sessao);
        }
    }

    public String gerenciarDisponibilidadeEAprovarSessao(LocalDateTime horarioProposto, SessaoMentoria sessao) {
        String statusAprovacao = "Pendente";
        boolean horarioValido = false;
        boolean sessaoExisteEPendente = false;

        if (horarioProposto == null || sessao == null) {
            return "Erro: Horário ou sessão inválidos.";
        }

        if (agenda != null) {
            for (LocalDateTime horario : agenda.getHorariosDisponiveis()) {
                if (horario.isEqual(horarioProposto)) {
                    horarioValido = true;
                    break;
                }
            }
        }

        if (!horarioValido) {
            return "Horário proposto não está disponível na agenda.";
        }

        for (SessaoMentoria s : minhasSessoes) {
            if (s.equals(sessao) && s.getStatus().equalsIgnoreCase("Pendente")) {
                sessaoExisteEPendente = true;
                break;
            }
        }

        if (!sessaoExisteEPendente) {
            return "Sessão não encontrada ou não está pendente.";
        }

        if (horarioProposto.isAfter(LocalDateTime.now().plusHours(24))) {
            if (sessao.getMentorado().getNome().startsWith("A")) {
                statusAprovacao = "Aprovada com Prioridade";
                sessao.setStatus("Aprovada com Prioridade");
            } else if (sessao.getMentorado().getNome().startsWith("B")) {
                statusAprovacao = "Aprovada Normal";
                sessao.setStatus("Aprovada Normal");
            } else {
                statusAprovacao = "Aprovada Condicional";
                sessao.setStatus("Aprovada Condicional");
            }
        } else if (horarioProposto.isBefore(LocalDateTime.now().plusHours(6))) {
            statusAprovacao = "Recusada (Muito em cima da hora)";
            sessao.setStatus("Recusada");
        } else {
            statusAprovacao = "Aprovada (Revisar disponibilidade)";
            sessao.setStatus("Aprovada");
        }

        if (statusAprovacao.contains("Prioridade")) {
            System.out.println("Sessão aprovada com prioridade para mentorado ");
        } else if (statusAprovacao.contains("Recusada")) {
            System.out.println("Penalidade aplicada ao mentor por sessão recusada.");
        }
        return "Status da aprovação: " + statusAprovacao;
    }

    public String getBiografia() {
        return biografia;
    }

    public List<Tecnologia> getTecnologias() {
        return new ArrayList<>(tecnologias);
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public List<SessaoMentoria> getMinhasSessoes() {
        return new ArrayList<>(minhasSessoes);
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
