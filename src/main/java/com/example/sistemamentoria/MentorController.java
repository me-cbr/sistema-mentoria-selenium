package com.example.sistemamentoria;

import com.example.sistemamentoria.domain.*;
import com.example.sistemamentoria.dto.SessaoRequestDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class MentorController {

    private static List<SessaoMentoria> sessoesMock = new ArrayList<>();
    private static Mentor mentorMock;
    private static List<Mentorado> todosMentorados = new ArrayList<>();


    static {
        LocalDateTime dataAlice = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime dataBruno = LocalDateTime.now().plusDays(3).withHour(14).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime dataDiana = LocalDateTime.now().plusHours(12).withSecond(0).withNano(0);
        LocalDateTime dataFernando = LocalDateTime.now().plusHours(4).withSecond(0).withNano(0);
        LocalDateTime dataGisele = LocalDateTime.now().plusDays(4).withSecond(0).withNano(0);

        Agenda agenda = new Agenda(1L);
        agenda.adicionarHorario(dataAlice);
        agenda.adicionarHorario(dataBruno);
        agenda.adicionarHorario(dataDiana);
        agenda.adicionarHorario(dataFernando);

        mentorMock = new Mentor(1L, "Carlos", "carlos@mentor.com", "123", "Bio do Carlos", agenda);

        Mentorado mentoradoAlice = new Mentorado(101L, "Alice Souza", "alice@email.com", "123");
        Mentorado mentoradoBruno = new Mentorado(102L, "Bruno Lima", "bruno@email.com", "123");
        Mentorado mentoradoDiana = new Mentorado(103L, "Diana Costa", "diana@email.com", "123");
        Mentorado mentoradoFernando = new Mentorado(104L, "Fernando Dias", "fernando@email.com", "123");
        Mentorado mentoradoGisele = new Mentorado(105L, "Gisele Alves", "gisele@email.com", "123");

        sessoesMock.add(new SessaoMentoria(1L, mentorMock, mentoradoAlice, dataAlice));
        sessoesMock.add(new SessaoMentoria(2L, mentorMock, mentoradoBruno, dataBruno));
        sessoesMock.add(new SessaoMentoria(3L, mentorMock, mentoradoDiana, dataDiana));
        sessoesMock.add(new SessaoMentoria(4L, mentorMock, mentoradoFernando, dataFernando));
        sessoesMock.add(new SessaoMentoria(5L, mentorMock, mentoradoGisele, dataGisele));

        sessoesMock.forEach(mentorMock::adicionarSessao);
        todosMentorados.add(mentoradoAlice);
        todosMentorados.add(mentoradoBruno);
        todosMentorados.add(mentoradoDiana);
        todosMentorados.add(mentoradoFernando);
        todosMentorados.add(mentoradoGisele);

    }

    @GetMapping("/sessoes/nova")
    public String mostrarFormularioNovaSessao(Model model) {
        model.addAttribute("sessaoDTO", new SessaoRequestDTO());
        model.addAttribute("todosMentorados", todosMentorados);
        return "form";
    }

    @PostMapping("/sessoes")
    public String criarNovaSessao(@ModelAttribute SessaoRequestDTO sessaoDTO) {

        Mentorado mentoradoSelecionado = todosMentorados.stream()
                .filter(m -> m.getId().equals(sessaoDTO.getMentoradoId()))
                .findFirst()
                .orElse(null);

        if (mentoradoSelecionado != null) {
            long novoId = sessoesMock.size() + 1;
            SessaoMentoria novaSessao = new SessaoMentoria(novoId, mentorMock, mentoradoSelecionado, sessaoDTO.getDataHora());

            sessoesMock.add(novaSessao);
            mentorMock.adicionarSessao(novaSessao);

            mentorMock.getAgenda().adicionarHorario(sessaoDTO.getDataHora());
        }

        return "redirect:/dashboard";
    }


    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        model.addAttribute("sessoes", sessoesMock);
        return "dashboard";
    }

    @PostMapping("/sessoes/aprovar/{id}")
    public String aprovarSessao(@PathVariable("id") Long idSessao, Model model) {
        Optional<SessaoMentoria> sessaoOptional = sessoesMock.stream()
                .filter(s -> s.getId().equals(idSessao))
                .findFirst();

        if (sessaoOptional.isPresent()) {
            SessaoMentoria sessaoParaAprovar = sessaoOptional.get();
            String resultado = mentorMock.gerenciarDisponibilidadeEAprovarSessao(sessaoParaAprovar.getDataHora(), sessaoParaAprovar);
            model.addAttribute("resultadoAprovacao", resultado);
        } else {
            model.addAttribute("resultadoAprovacao", "Erro: Sessão com ID " + idSessao + " não encontrada.");
        }

        model.addAttribute("sessoes", sessoesMock);
        return "dashboard";
    }
}