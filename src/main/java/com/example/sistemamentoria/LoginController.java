package com.example.sistemamentoria;

import com.example.sistemamentoria.domain.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class LoginController {

    private final List<Usuario> usuariosDoSistema;

    public LoginController() {
        this.usuariosDoSistema = Stream.concat(
                MentorController.getTodosMentorados().stream(),
                Stream.of(MentorController.getMentorMock())
        ).collect(Collectors.toList());
    }


    @GetMapping("/login")
    public String mostrarPaginaDeLogin() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String processarLogin(@RequestParam String email, @RequestParam String senha, Model model) {
        for (Usuario usuario : usuariosDoSistema) {
            if (usuario.getEmail().equalsIgnoreCase(email) && usuario.getSenha().equals(senha)) {
                return "redirect:/dashboard";
            }
        }

        model.addAttribute("erro", "Usuário ou senha inválidos.");
        return "login";
    }

    @PostMapping("/logout")
    public String processarLogout() {
        return "redirect:/login";
    }

    @GetMapping("/test/reset")
    public String resetState() {
        MentorController.resetMockData();
        return "redirect:/login";
    }
}