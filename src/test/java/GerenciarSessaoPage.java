import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class GerenciarSessaoPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By resultadoAprovacaoDiv = By.cssSelector("div.resultado p");

    public GerenciarSessaoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void gerenciarSessao(long idSessao) {
        String formAction = String.format("form[action='/sessoes/aprovar/%d']", idSessao);
        driver.findElement(By.cssSelector(formAction)).submit();
    }

    public String getResultado() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(resultadoAprovacaoDiv)).getText();
    }
}