package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage {

    WebDriver driver;

    public RegisterPage(WebDriver driver){
        this.driver = driver;
    }

    By firstName = By.id("input-firstname");
    By lastName = By.id("input-lastname");
    By email = By.id("input-email");
    By telephone = By.id("input-telephone");
    By password = By.id("input-password");
    By confirmPassword = By.id("input-confirm");

    By privacyPolicy = By.name("agree");
    By continueBtn = By.xpath("//input[@value='Continue']");

    By emailError = By.cssSelector("#input-email + .text-danger");
    By telephoneError = By.cssSelector("#input-telephone + .text-danger");
    By passwordError = By.cssSelector("#input-password + .text-danger");
    
    public void enterFirstName(String value){
        driver.findElement(firstName).sendKeys(value);
    }

    public void enterLastName(String value){
        driver.findElement(lastName).sendKeys(value);
    }

    public void enterEmail(String value){
        driver.findElement(email).sendKeys(value);
    }

    public void enterTelephone(String value){
        driver.findElement(telephone).sendKeys(value);
    }

    public void enterPassword(String value){
        driver.findElement(password).sendKeys(value);
    }

    public void confirmPassword(String value){
        driver.findElement(confirmPassword).sendKeys(value);
    }

    public void acceptPolicy(){
        driver.findElement(privacyPolicy).click();
    }

    public void clickContinue(){
        driver.findElement(continueBtn).click();
    }

    public String getEmailError() {
        return driver.findElement(emailError).getText();
    }

    public String getTelephoneError() {
        return driver.findElement(telephoneError).getText();
    }

    public String getPasswordError() {
        return driver.findElement(passwordError).getText();
    }

}