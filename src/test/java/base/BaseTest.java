package base;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import configuration.ConfigReader;
import io.qameta.allure.Attachment;

public class BaseTest {
    public WebDriver driver;
    public WebDriver getDriver() {
        return driver;
    }
    protected ConfigReader config = new ConfigReader();

    @BeforeMethod
    public void setup() {
        config = new ConfigReader();

        // System.setProperty("webdriver.chrome.driver","chromedriver.exe");

        // This automatically checks your Chrome version and downloads the matching driver
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(config.get("url"));
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }

    // Allure integration method
    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }


}