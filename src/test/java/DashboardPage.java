import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DashboardPage {
    private WebDriver driver;

    private By linkNovaSessao = By.linkText("+ Agendar Nova Sessão");
    private By tabelaDeSessoes = By.tagName("table");
    private By linhasDaTabela = By.xpath("//table/tbody/tr");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clicarAgendarNovaSessao() {
        driver.findElement(linkNovaSessao).click();
    }

    public void gerenciarSessao(long idSessao) {
        String formAction = String.format("form[action='/sessoes/aprovar/%d']", idSessao);
        driver.findElement(By.cssSelector(formAction)).submit();
    }

    public String getResultadoAprovacao() {
        return driver.findElement(By.cssSelector("div.resultado p")).getText();
    }

    public boolean sessaoExisteNaTabela(String nomeMentorado, String dataHoraFormatada) {
        List<WebElement> linhas = driver.findElements(linhasDaTabela);
        for (WebElement linha : linhas) {
            List<WebElement> colunas = linha.findElements(By.tagName("td"));
            if (colunas.size() >= 3) {
                String mentoradoNaLinha = colunas.get(1).getText();
                String dataNaLinha = colunas.get(2).getText();

                if (mentoradoNaLinha.equals(nomeMentorado) && dataNaLinha.equals(dataHoraFormatada)) {
                    return true;
                }
            }
        }
        return false;
    }
    public void clicarLogout() {
        driver.findElement(By.cssSelector("form[action='/logout'] button")).click();
    }
}