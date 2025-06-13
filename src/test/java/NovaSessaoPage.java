import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class NovaSessaoPage {
    private WebDriver driver;

    private By mentoradoDropdown = By.id("mentorado");
    private By dataHoraInput = By.id("dataHora");
    private By botaoAgendar = By.tagName("button");

    public NovaSessaoPage(WebDriver driver) {
        this.driver = driver;
    }

    public void agendarNovaSessao(String idMentorado, String dataHora) {
        new Select(driver.findElement(mentoradoDropdown)).selectByValue(idMentorado);
        driver.findElement(dataHoraInput).sendKeys(dataHora);
        driver.findElement(botaoAgendar).submit();
    }

    public void submeterFormularioVazio() {
        driver.findElement(botaoAgendar).submit();
    }
}