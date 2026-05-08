package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import java.time.Duration;

public class HomePage {

    WebDriver driver;
    private WebDriverWait wait;
    public HomePage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    By myAccount = By.xpath("//span[text()='My Account']");
    By register = By.linkText("Register");
    By login = By.linkText("Login");

    By desktopsMenu = By.linkText("Desktops");
    By showAllDesktops = By.linkText("Show AllDesktops");

    By currencyDropdown = By.xpath("//span[text()='Currency']");
    By euroButton = By.name("EUR");

    By tabletsMenu = By.linkText("Tablets");
    By phonesMenu = By.linkText("Phones & PDAs");

    By searchBox = By.name("search");
    By searchBtn = By.cssSelector("button.btn.btn-default");

    public void navigateToCategory(String categoryName) {

        WebElement topMenu = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(categoryName)));
        topMenu.click();

        By showAllLocator = By.linkText("Show All" + categoryName);

      //  try {
            WebElement showAll = wait.until(ExpectedConditions.visibilityOfElementLocated(showAllLocator));
            showAll.click();
   //     } catch (Exception e) {

          //  topMenu.click();}
    }

    public void goToRegister(){

        driver.findElement(myAccount).click();
        driver.findElement(register).click();
    }

    public void goToLogin(){

        driver.findElement(myAccount).click();
        driver.findElement(login).click();
    }

    public void goToDesktops() {
        driver.findElement(desktopsMenu).click();
        driver.findElement(showAllDesktops).click();
    }

    public void changeCurrencyToEuro() {
        driver.findElement(currencyDropdown).click();
        driver.findElement(euroButton).click();
    }

    public void goToTablets() {
        driver.findElement(tabletsMenu).click();
    }

    public void navigateToPhones() {
        driver.findElement(phonesMenu).click();
    }

    public void search(String text) {
        WebElement input = driver.findElement(searchBox);
        input.clear();
        input.sendKeys(text);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(searchBtn)
        );

        try {
            searchButton.click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", searchButton);
        }
    }
}