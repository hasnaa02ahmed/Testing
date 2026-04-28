package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TabletsPage {
    WebDriver driver;

    By breadcrumbActive = By.cssSelector(".breadcrumb li:last-child a");
    By activeSideMenu = By.cssSelector(".list-group a.active");

    public TabletsPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getBreadcrumbText() {
        return driver.findElement(breadcrumbActive).getText();
    }

    public String getActiveMenuText() {
        return driver.findElement(activeSideMenu).getText();
    }
}