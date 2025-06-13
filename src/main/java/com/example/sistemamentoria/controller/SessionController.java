package com.example.sistemamentoria.controller;

import com.example.sistemamentoria.domain.*;
import com.example.sistemamentoria.repository.InMemoryDataStore;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;


@Controller
public class SessionController {

    private final InMemoryDataStore dataStore;

    public SessionController(InMemoryDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/create-session")
    public String showCreateSessionPage(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }
        model.addAttribute("mentors", dataStore.findAllMentors());
        return "create-session";
    }

    @PostMapping("/create-session")
    public String handleCreateSession(@RequestParam Long mentorId,
                                      @RequestParam("dataHora") String dataHoraStr,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (!(usuarioLogado instanceof Mentorado)) {
            redirectAttributes.addFlashAttribute("error", "Apenas mentorados podem solicitar sessões.");
            return "redirect:/dashboard";
        }
        Mentorado mentorado = (Mentorado) usuarioLogado;
        Mentor mentor = (Mentor) dataStore.findUserById(mentorId);
        LocalDateTime dataHora = LocalDateTime.parse(dataHoraStr);

        if (mentor != null) {
            SessaoMentoria novaSessao = new SessaoMentoria(null, mentor, mentorado, dataHora);
            dataStore.saveSession(novaSessao);
            redirectAttributes.addFlashAttribute("success", "Sessão de mentoria solicitada com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Mentor não encontrado.");
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/manage-session/{id}")
    public String showManageSessionPage(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        SessaoMentoria sessao = dataStore.findSessionById(id);

        if (usuarioLogado == null) {
            return "redirect:/login";
        }
        if (sessao == null) {
            redirectAttributes.addFlashAttribute("error", "Sessão não encontrada.");
            return "redirect:/dashboard";
        }

        if (!sessao.getMentor().getId().equals(usuarioLogado.getId())) {
            redirectAttributes.addFlashAttribute("error", "Você não tem permissão para gerenciar esta sessão.");
            return "redirect:/dashboard";
        }

        model.addAttribute("sessao", sessao);
        return "manage-session";
    }

    @PostMapping("/update-session-status")
    public String updateSessionStatus(@RequestParam Long sessaoId,
                                      @RequestParam String novoStatus,
                                      @RequestParam(required = false) String motivo,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        SessaoMentoria sessao = dataStore.findSessionById(sessaoId);

        if (usuarioLogado == null) {
            return "redirect:/login";
        }
        if (sessao == null || !sessao.getMentor().getId().equals(usuarioLogado.getId())) {
            redirectAttributes.addFlashAttribute("error", "Operação não permitida.");
            return "redirect:/dashboard";
        }

        boolean sucesso = sessao.atualizarStatusSessao(novoStatus, motivo);
        if (sucesso) {
            redirectAttributes.addFlashAttribute("success", "Status da sessão " + sessaoId + " atualizado com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Não foi possível atualizar o status da sessão.");
        }

        return "redirect:/dashboard";
    }
}