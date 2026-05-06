package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    WebDriver driver;
    By email = By.id("input-email");
    By password = By.id("input-password");
    By loginBtn = By.xpath("//input[@value='Login']");
    By errorMessage = By.cssSelector(".alert-danger");
    By logoutBtn = By.xpath("//a[text()='Logout']");

    public LoginPage(WebDriver driver){
        this.driver = driver;
    }

    public void login(String user,String pass){
        driver.findElement(email).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(loginBtn).click();
    }

    public boolean isLoggedIn() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("a[href*='route=account/logout']")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage(){
        return driver.findElement(errorMessage).getText();
    }
}

