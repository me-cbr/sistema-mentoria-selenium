package com.example.sistemamentoria;

import com.example.sistemamentoria.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SistemaMentoriaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void testExibirPaginaDeLogin() throws Exception {
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("<h1>Sistema de Mentoria</h1>")));
	}

	@Test
	void testRealizarLoginComSucessoERedirecionarParaDashboard() throws Exception {
		mockMvc.perform(post("/login")
						.param("email", "ana.p@mentor.com")
						.param("senha", "senha123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dashboard"));
	}

	@Test
	void testFalharLoginComCredenciaisInvalidas() throws Exception {
		mockMvc.perform(post("/login")
						.param("email", "ana.p@exemplo.com")
						.param("senha", "senha_errada"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"))
				.andExpect(flash().attributeExists("error"));
	}

	@Test
	void testRegistrarNovoUsuarioComSucesso() throws Exception {
		mockMvc.perform(post("/register")
						.param("nome", "Novo Mentorado")
						.param("email", "novo.mentorado@exemplo.com")
						.param("senha", "senhaforte")
						.param("userType", "mentorado"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"))
				.andExpect(flash().attribute("success", "Conta criada com sucesso! Faça o login."));
	}

	@Test
	void testBloquearAcessoAoDashboardSemLogin() throws Exception {
		mockMvc.perform(get("/dashboard"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"));
	}

	@Test
	void testPermitirAcessoAoDashboardComLogin() throws Exception {
		Usuario usuarioLogado = new com.example.sistemamentoria.domain.Mentor(99L, "Mentor de Teste", "mentor.teste@exemplo.com", "senha123", "Bio", null);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("usuarioLogado", usuarioLogado);

		mockMvc.perform(get("/dashboard").session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("dashboard"))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Olá, Mentor de Teste")));
	}

	@Test
	void testRealizarLogoutERedirecionarParaLogin() throws Exception {
		Usuario usuarioLogado = new com.example.sistemamentoria.domain.Mentor(99L, "Mentor de Teste", "mentor.teste@exemplo.com", "senha123", "Bio", null);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("usuarioLogado", usuarioLogado);

		mockMvc.perform(post("/logout").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"));
	}
}