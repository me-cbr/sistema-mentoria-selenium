import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GerenciarSessaoTest {

    private WebDriver driver;
    private GerenciarSessaoPage gerenciarSessaoPage;
    private final String DASHBOARD_URL = "http://localhost:8080/dashboard";

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.get(DASHBOARD_URL);
        gerenciarSessaoPage = new GerenciarSessaoPage(driver);
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("CT01 - Deve aprovar sessão com prioridade para mentorado com nome 'A'")
    public void testeAprovacaoPrioritaria() {
        gerenciarSessaoPage.gerenciarSessao(1L);
        String resultado = gerenciarSessaoPage.getResultado();
        assertEquals("Status da aprovação: Aprovada com Prioridade", resultado);
    }

    @Test
    @DisplayName("CT02 - Deve aprovar sessão normalmente para mentorado com nome 'B'")
    public void testeAprovacaoNormal() {
        gerenciarSessaoPage.gerenciarSessao(2L);
        String resultado = gerenciarSessaoPage.getResultado();
        assertEquals("Status da aprovação: Aprovada Normal", resultado);
    }

    @Test
    @DisplayName("CT03 - Deve aprovar sessão condicionalmente (prazo entre 6 e 24 horas)")
    public void testeAprovacaoCondicional() {
        gerenciarSessaoPage.gerenciarSessao(3L);
        String resultado = gerenciarSessaoPage.getResultado();
        assertEquals("Status da aprovação: Aprovada (Revisar disponibilidade)", resultado);
    }

    @Test
    @DisplayName("CT04 - Deve recusar sessão por curto prazo (menos de 6 horas)")
    public void testeRecusaCurtoPrazo() {
        gerenciarSessaoPage.gerenciarSessao(4L);
        String resultado = gerenciarSessaoPage.getResultado();
        assertEquals("Status da aprovação: Recusada (Muito em cima da hora)", resultado);
    }

    @Test
    @DisplayName("CT05 - Deve retornar erro para horário indisponível na agenda")
    public void testeErroHorarioIndisponivel() {
        gerenciarSessaoPage.gerenciarSessao(5L);
        String resultado = gerenciarSessaoPage.getResultado();
        assertEquals("Horário proposto não está disponível na agenda.", resultado);
    }
}