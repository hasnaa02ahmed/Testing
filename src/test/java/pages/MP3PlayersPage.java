package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MP3PlayersPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By mp3MainTab = By.linkText("MP3 Players");

    private By showAllLink = By.cssSelector("li.dropdown.open .see-all");
    private By successAlert = By.cssSelector(".alert-success");

    public MP3PlayersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateToShowAll() {
        WebElement mainTab = wait.until(ExpectedConditions.elementToBeClickable(mp3MainTab));
        mainTab.click();

        WebElement showAll = wait.until(ExpectedConditions.elementToBeClickable(showAllLink));
        showAll.click();
    }

    public void addProductToCart(String productName) {
        By productAddBtn = By.xpath("//a[text()='" + productName + "']/ancestor::div[@class='product-thumb']//button[contains(@onclick, 'cart.add')]");
        wait.until(ExpectedConditions.elementToBeClickable(productAddBtn)).click();
    }

    public String getSuccessAlertText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert)).getText();
    }
}