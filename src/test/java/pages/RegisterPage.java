package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterPage {

    WebDriver driver;
    WebDriverWait wait;

    public RegisterPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    By firstName = By.id("input-firstname");
    By lastName = By.id("input-lastname");
    By email = By.id("input-email");
    By telephone = By.id("input-telephone");
    By password = By.id("input-password");
    By confirmPassword = By.id("input-confirm");

    By privacyPolicy = By.name("agree");
    By continueBtn = By.xpath("//input[@value='Continue']");

    By emailError = By.xpath("//input[@id='input-email']/following-sibling::div[@class='text-danger']");
    By telephoneError = By.xpath("//input[@id='input-telephone']/following-sibling::div[@class='text-danger']");
    By passwordError = By.xpath("//input[@id='input-password']/following-sibling::div[@class='text-danger']");
   
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

        wait.until(
            ExpectedConditions.elementToBeClickable(continueBtn)
        ).click();
    }

    public String getEmailError() {
        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(emailError)
        ).getText();
    }

    public String getTelephoneError() {
        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(telephoneError)
        ).getText();
    }

    public String getPasswordError() {
        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(passwordError)
        ).getText();
    }

}