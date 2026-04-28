package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DesktopPage {
    WebDriver driver;

    By firstProductPrice = By.cssSelector(".price");

    public DesktopPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getFirstProductPrice() {
        return driver.findElement(firstProductPrice).getText();
    }
}