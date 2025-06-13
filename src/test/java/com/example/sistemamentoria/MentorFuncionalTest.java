package com.example.sistemamentoria;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MentorFuncionalTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void submeterFormulario(String horario, String sessaoId) {
        driver.get(baseUrl + "/gerenciar-sessao.html");

        WebElement horarioInput = driver.findElement(By.id("horarioProposto"));
        horarioInput.clear();
        horarioInput.sendKeys(horario);

        WebElement sessaoInput = driver.findElement(By.id("sessaoId"));
        sessaoInput.clear();
        sessaoInput.sendKeys(sessaoId);

        driver.findElement(By.tagName("button")).click();
    }

    @Test
    void testAprovacaoPrioritaria() {
        String horario = LocalDateTime.now().plusHours(25).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        submeterFormulario(horario, "1"); // ID 1 é a sessão com mentorado "Ana test"

        WebElement resultado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultado")));
        assertTrue(resultado.getText().contains("Aprovada com Prioridade"));
    }

    @Test
    void testAprovacaoNormal() {
        String horario = LocalDateTime.now().plusHours(25).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        submeterFormulario(horario, "2"); // ID 2 é a sessão com mentorado "Bruno test"

        WebElement resultado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultado")));
        assertTrue(resultado.getText().contains("Aprovada Normal"));
    }

    @Test
    void testAprovacaoCondicional() {
        String horario = LocalDateTime.now().plusHours(25).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        submeterFormulario(horario, "3"); // ID 3 é a sessão com mentorado "Carlos test"

        WebElement resultado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultado")));
        assertTrue(resultado.getText().contains("Aprovada Condicional"));
    }

    @Test
    void testRecusaPorAntecedencia() {
        String horario = LocalDateTime.now().plusHours(5).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        submeterFormulario(horario, "1");

        WebElement resultado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultado")));
        assertTrue(resultado.getText().contains("Recusada (Muito em cima da hora)"));
    }

    @Test
    void testAprovacaoComRevisao() {
        String horario = LocalDateTime.now().plusHours(12).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        submeterFormulario(horario, "1");

        WebElement resultado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultado")));
        assertTrue(resultado.getText().contains("Aprovada (Revisar disponibilidade)"));
    }

    @Test
    void testSessaoNaoEncontrada() {
        String horario = LocalDateTime.now().plusHours(30).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        submeterFormulario(horario, "999"); // ID que não existe

        WebElement resultado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultado")));
        assertTrue(resultado.getText().contains("Sessão não encontrada"));
    }
}