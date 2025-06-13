package com.example.sistemamentoria;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessaoMentoriaFuncionalTest {

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

    private void submeterFormularioStatus(String sessaoId, String novoStatus, String motivo) {
        driver.get(baseUrl + "/atualizar-status.html");

        WebElement sessaoInput = driver.findElement(By.id("sessaoId"));
        sessaoInput.clear();
        sessaoInput.sendKeys(sessaoId);

        Select statusDropdown = new Select(driver.findElement(By.id("novoStatus")));
        statusDropdown.selectByValue(novoStatus);

        WebElement motivoInput = driver.findElement(By.id("motivo"));
        motivoInput.clear();
        motivoInput.sendKeys(motivo);

        driver.findElement(By.tagName("button")).click();
    }

    @Test
    void testAprovarSessaoPendente() {
        submeterFormularioStatus("1", "aprovada", "");
        WebElement resultado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultado")));
        assertTrue(resultado.getText().contains("Status da sessão 1 atualizado para Aprovada."));
    }

    @Test
    void testRecusarSessaoPendente() {
        submeterFormularioStatus("1", "recusada", "Conflito de agenda.");
        WebElement resultado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultado")));
        assertTrue(resultado.getText().contains("Status da sessão 1 atualizado para Recusada."));
    }

    @Test
    void testCancelarSessaoAprovada() {
        // Primeiro, aprova a sessão
        submeterFormularioStatus("1", "aprovada", "");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("resultado"), "Aprovada"));

        // Depois, cancela
        submeterFormularioStatus("1", "cancelada", "Emergência.");
        WebElement resultado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultado")));
        assertTrue(resultado.getText().contains("Status da sessão 1 atualizado para Cancelada."));
    }

    @Test
    void testIniciarSessaoAprovada() {
        // Primeiro, aprova a sessão
        submeterFormularioStatus("1", "aprovada", "");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("resultado"), "Aprovada"));

        // Depois, inicia
        submeterFormularioStatus("1", "iniciada", "");
        WebElement resultado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultado")));
        // A sessão no InMemoryDataStore tem um horário no passado, então deve iniciar com sucesso.
        assertTrue(resultado.getText().contains("Status da sessão 1 atualizado para Iniciada."));
    }

    @Test
    void testFalhaAoAprovarSessaoJaAprovada() {
        // Primeiro, aprova a sessão
        submeterFormularioStatus("1", "aprovada", "");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("resultado"), "Aprovada"));

        // Tenta aprovar novamente
        submeterFormularioStatus("1", "aprovada", "");
        WebElement resultado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultado")));
        assertTrue(resultado.getText().contains("não pode ser atualizada para aprovada. Status atual: Aprovada"));
    }
}