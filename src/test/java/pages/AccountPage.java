package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountPage {

    WebDriver driver;
    By successHeader = By.xpath("//h1[text()='Your Account Has Been Created!']");
    By myAccountDropdown = By.xpath("//span[text()='My Account']");
    By logoutLink = By.linkText("Logout");

    public AccountPage(WebDriver driver){
        this.driver = driver;
    }

    public boolean isRegistrationSuccessful(){
        return driver.findElement(successHeader).isDisplayed();
    }

    public void clickLogout() {
        driver.findElement(myAccountDropdown).click(); 
        driver.findElement(logoutLink).click();
    }

    public boolean isLogoutDisplayed(){
        driver.findElement(myAccountDropdown).click(); 
        return driver.findElement(logoutLink).isDisplayed();
    }

}