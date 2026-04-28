package org.example;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class FirstTest {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.get("https://www.google.com");
        System.out.println(driver.getTitle());


        WebElement element = driver.findElement(By.id("username"));
        driver.findElement(By.name("email"));
        driver.findElement(By.className("login-btn"));
        driver.findElement(By.tagName("input"));
        driver.findElement(By.xpath("//input[@type='text']"));
        driver.findElement(By.cssSelector(".login-btn"));

        element.sendKeys("Hello World");
        element.click();
        element.clear();



        driver.quit();
    }
}


