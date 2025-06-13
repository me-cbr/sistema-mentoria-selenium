package com.example.sistemamentoria.repository;

import com.example.sistemamentoria.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryDataStore {

    private static final Map<Long, Usuario> USERS = new ConcurrentHashMap<>();
    private static final Map<Long, SessaoMentoria> SESSIONS = new ConcurrentHashMap<>();

    private static final AtomicLong userCounter = new AtomicLong(3);
    private static final AtomicLong sessionCounter = new AtomicLong(3);

    static {
        Agenda agenda1 = new Agenda(1L);
        agenda1.adicionarHorario(LocalDateTime.now().plusHours(25));
        agenda1.adicionarHorario(LocalDateTime.now().plusHours(5));
        agenda1.adicionarHorario(LocalDateTime.now().plusHours(12));
        Mentor mentor1 = new Mentor(1L, "Ana Pereira", "ana.p@mentor.com", "senha123", "Especialista em Java e Spring.", agenda1);

        Mentor mentor2 = new Mentor(2L, "Bruno Costa", "bruno@mentor.com", "senha123", "Desenvolvedor Full-Stack com foco em React.", new Agenda(2L));

        Mentorado mentorado1 = new Mentorado(3L, "Carlos Dias", "carlos.d@mentorado.com", "senha123");

        SessaoMentoria sessao1 = new SessaoMentoria(1L, mentor1, mentorado1, LocalDateTime.now().plusHours(25));
        sessao1.getMentorado().setNome("Ana");
        SessaoMentoria sessao2 = new SessaoMentoria(2L, mentor1, mentorado1, LocalDateTime.now().plusHours(25));
        sessao2.getMentorado().setNome("Bruno");
        SessaoMentoria sessao3 = new SessaoMentoria(3L, mentor1, mentorado1, LocalDateTime.now().plusHours(25));
        sessao3.getMentorado().setNome("Carlos");

        mentor1.adicionarSessao(sessao1);
        mentor1.adicionarSessao(sessao2);
        mentor1.adicionarSessao(sessao3);

        USERS.put(1L, mentor1);
        USERS.put(2L, mentor2);
        USERS.put(3L, mentorado1);

        SESSIONS.put(1L, sessao1);
        SESSIONS.put(2L, sessao2);
        SESSIONS.put(3L, sessao3);
    }

    public Usuario findUserByEmail(String email) {
        return USERS.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public Usuario findUserById(Long id) {
        return USERS.get(id);
    }

    public void saveUser(Usuario user) {
        if (user.getId() == null) {
            user.setId(userCounter.incrementAndGet());
        }
        USERS.put(user.getId(), user);
    }

    public Collection<Mentor> findAllMentors() {
        return USERS.values().stream()
                .filter(Mentor.class::isInstance)
                .map(Mentor.class::cast)
                .collect(Collectors.toList());
    }

    public void saveSession(SessaoMentoria session) {
        if (session.getId() == null) {
            session.setId(sessionCounter.incrementAndGet());
        }
        SESSIONS.put(session.getId(), session);
        session.getMentor().adicionarSessao(session);
    }

    public SessaoMentoria findSessionById(Long id) {
        return SESSIONS.get(id);
    }

    public Collection<SessaoMentoria> findSessionsByMentoradoId(Long mentoradoId) {
        return SESSIONS.values().stream()
                .filter(sessao -> sessao.getMentorado().getId().equals(mentoradoId))
                .collect(Collectors.toList());
    }

    public Collection<SessaoMentoria> findSessionsByMentorId(Long mentorId) {
        return SESSIONS.values().stream()
                .filter(sessao -> sessao.getMentor().getId().equals(mentorId))
                .collect(Collectors.toList());
    }
}