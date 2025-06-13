package com.example.sistemamentoria.controller;

import com.example.sistemamentoria.domain.Agenda;
import com.example.sistemamentoria.domain.Mentor;
import com.example.sistemamentoria.domain.Mentorado;
import com.example.sistemamentoria.domain.Usuario;
import com.example.sistemamentoria.repository.InMemoryDataStore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final InMemoryDataStore dataStore;

    public AuthController(InMemoryDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email,
                              @RequestParam String senha,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        Usuario usuario = dataStore.findUserByEmail(email);

        if (usuario != null && usuario.getSenha().equals(senha)) {
            session.setAttribute("usuarioLogado", usuario);
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Email ou senha inválidos. Tente novamente.");
            return "redirect:/login";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        model.addAttribute("nomeUsuario", usuarioLogado.getNome());
        model.addAttribute("isMentor", usuarioLogado instanceof Mentor);

        if (usuarioLogado instanceof Mentor) {
            model.addAttribute("sessoes", dataStore.findSessionsByMentorId(usuarioLogado.getId()));
        } else if (usuarioLogado instanceof Mentorado) {
            model.addAttribute("sessoes", dataStore.findSessionsByMentoradoId(usuarioLogado.getId()));
        }
        return "dashboard";
    }

    @PostMapping("/logout")
    public String handleLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam String nome,
                                 @RequestParam String email,
                                 @RequestParam String senha,
                                 @RequestParam String userType,
                                 @RequestParam(required = false) String biografia,
                                 RedirectAttributes redirectAttributes) {

        if (dataStore.findUserByEmail(email) != null) {
            redirectAttributes.addFlashAttribute("error", "Este email já está em uso.");
            return "redirect:/register";
        }

        Usuario novoUsuario;
        if ("mentor".equals(userType)) {
            novoUsuario = new Mentor(null, nome, email, senha, biografia, new Agenda(null));
        } else {
            novoUsuario = new Mentorado(null, nome, email, senha);
        }

        dataStore.saveUser(novoUsuario);

        redirectAttributes.addFlashAttribute("success", "Conta criada com sucesso! Faça o login.");
        return "redirect:/login";
    }
}