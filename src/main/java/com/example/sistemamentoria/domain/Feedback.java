package com.example.sistemamentoria.domain;

import java.util.Objects;

public class Feedback {
    private Long id;
    private SessaoMentoria sessao;
    private Usuario autor;
    private Avaliacao avaliacao;
    private String comentario;

    public Feedback(Long id, SessaoMentoria sessao, Usuario autor, int nota, String comentario) throws FeedbackException {
        if (sessao == null) {
            throw new IllegalArgumentException("O Feedback deve estar associado a uma sessão.");
        }

        if (autor == null) {
            throw new IllegalArgumentException("O Feedback deve estar associado a um autor.");
        }

        if (!autor.equals(sessao.getMentor()) && !autor.equals(sessao.getMentorado())) {
            throw new SecurityException("O autor do feedback não participa da sessão de mentoria.");
        }

        boolean comentarioInvalido = (comentario == null || comentario.trim().isEmpty());
        if ((nota == 0 || nota == 1 || nota == 5) && comentarioInvalido) {
            throw new FeedbackException("Para notas 0, 1 ou 5, um comentário é obrigatório.");
        }

        this.id = id;
        this.sessao = sessao;
        this.comentario = comentario;
        this.autor = autor;
        this.avaliacao = new Avaliacao(null, nota);
    }

    public Usuario getAutor() {
        return autor;
    }

    public Long getId() {
        return id;
    }

    public SessaoMentoria getSessao() {
        return sessao;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public String getComentario() {
        return comentario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(id, feedback.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
