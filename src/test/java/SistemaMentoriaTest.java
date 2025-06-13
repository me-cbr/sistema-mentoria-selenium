import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SistemaMentoriaTest {

    private WebDriver driver;
    private DashboardPage dashboardPage;
    private NovaSessaoPage novaSessaoPage;
    private LoginPage loginPage;

    private final String BASE_URL = "http://localhost:8080";
    private final String LOGIN_URL = BASE_URL + "/login";
    private final String DASHBOARD_URL = BASE_URL + "/dashboard";

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.get(BASE_URL + "/test/reset");
        driver.get(LOGIN_URL);

        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        novaSessaoPage = new NovaSessaoPage(driver);
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void realizarLoginPadrao() {
        loginPage.fazerLogin("maria@mentor.com", "123");
    }

    @Test
    public void testLoginMentorSucesso() {
        loginPage.fazerLogin("maria@mentor.com", "123");
        assertEquals(DASHBOARD_URL, driver.getCurrentUrl(), "Deveria ser redirecionado para o dashboard após login.");
    }

    @Test
    public void testLoginMentoradoSucesso() {
        loginPage.fazerLogin("alice@email.com", "123");
        assertEquals(DASHBOARD_URL, driver.getCurrentUrl());
    }

    @Test
    public void testLoginSenhaIncorreta() {
        loginPage.fazerLogin("maria@mentor.com", "senhaerrada");
        assertEquals("Usuário ou senha inválidos.", loginPage.getMensagemDeErro());
        assertEquals(LOGIN_URL, driver.getCurrentUrl(), "Deveria permanecer na página de login.");
    }

    @Test
    public void testLoginEmailInexistente() {
        loginPage.fazerLogin("naoexiste@email.com", "123");
        assertEquals("Usuário ou senha inválidos.", loginPage.getMensagemDeErro());
    }

    @Test
    public void testValidacaoFormularioLoginVazio() {
        realizarLoginPadrao();
        loginPage.submeterFormularioVazio();
        assertEquals(LOGIN_URL, driver.getCurrentUrl(), "O formulário não deveria ter sido enviado com campos vazios.");
    }


    @Test
    public void testAprovacaoPrioritaria() {
        realizarLoginPadrao();
        dashboardPage.gerenciarSessao(1L);
        assertEquals("Status da aprovação: Aprovada com Prioridade", dashboardPage.getResultadoAprovacao());
    }

    @Test
    public void testAprovacaoNormal() {
        realizarLoginPadrao();
        dashboardPage.gerenciarSessao(2L);
        assertEquals("Status da aprovação: Aprovada Normal", dashboardPage.getResultadoAprovacao());
    }

    @Test
    public void testAprovacaoCondicional() {
        realizarLoginPadrao();
        dashboardPage.gerenciarSessao(3L);
        assertEquals("Status da aprovação: Aprovada (Revisar disponibilidade)", dashboardPage.getResultadoAprovacao());
    }

    @Test
    public void testRecusaCurtoPrazo() {
        realizarLoginPadrao();
        dashboardPage.gerenciarSessao(4L);
        assertEquals("Status da aprovação: Recusada (Muito em cima da hora)", dashboardPage.getResultadoAprovacao());
    }

    @Test
    public void testErroHorarioIndisponivel() {
        realizarLoginPadrao();
        dashboardPage.gerenciarSessao(5L);
        assertEquals("Horário proposto não está disponível na agenda.", dashboardPage.getResultadoAprovacao());
    }

    @Test
    public void testAgendamentoNovaSessaoComSucesso() {
        realizarLoginPadrao();
        dashboardPage.clicarAgendarNovaSessao();

        String mentoradoIdParaAgendar = "101";
        String nomeMentorado = "Alice Souza";
        LocalDateTime dataHoraParaAgendar = LocalDateTime.now().plusWeeks(1);

        String dataHoraFormatoInput = dataHoraParaAgendar.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        String dataHoraFormatoTabela = dataHoraParaAgendar.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        novaSessaoPage.agendarNovaSessao(mentoradoIdParaAgendar, dataHoraFormatoInput);

        assertEquals(DASHBOARD_URL, driver.getCurrentUrl());
        assertTrue(dashboardPage.sessaoExisteNaTabela(nomeMentorado, dataHoraFormatoTabela));
    }

    @Test
    public void testValidacaoFormularioNovaSessao() {
        realizarLoginPadrao();
        dashboardPage.clicarAgendarNovaSessao();
        String urlFormulario = driver.getCurrentUrl();

        novaSessaoPage.submeterFormularioVazio();

        assertEquals(urlFormulario, driver.getCurrentUrl(), "O formulário não deveria ter sido enviado com campos vazios.");
    }

    @Test
    public void testLogoutComSucesso() {
        realizarLoginPadrao();
        assertEquals(DASHBOARD_URL, driver.getCurrentUrl(), "Login falhou, não foi possível iniciar o teste de logout.");

        dashboardPage.clicarLogout();

        assertTrue(driver.getCurrentUrl().contains(LOGIN_URL), "Não foi redirecionado para a página de login após o logout.");
    }
}