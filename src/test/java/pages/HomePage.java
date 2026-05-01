package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    WebDriver driver;

    public HomePage(WebDriver driver){
        this.driver = driver;
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
        driver.findElement(searchBox).sendKeys(text);
        driver.findElement(searchBtn).click();
    }
}