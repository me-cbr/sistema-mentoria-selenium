import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;

    private By emailInput = By.id("email");
    private By senhaInput = By.id("senha");
    private By botaoEntrar = By.tagName("button");
    private By mensagemErro = By.cssSelector("div.error");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void fazerLogin(String email, String senha) {
        driver.findElement(emailInput).sendKeys(email);
        driver.findElement(senhaInput).sendKeys(senha);
        driver.findElement(botaoEntrar).click();
    }

    public void submeterFormularioVazio() {
        driver.findElement(botaoEntrar).click();
    }

    public String getMensagemDeErro() {
        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(mensagemErro)).getText();
    }
}