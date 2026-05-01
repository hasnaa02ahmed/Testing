package base;

import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import configuration.ConfigReader;
import io.qameta.allure.Attachment;

public class BaseTest {
    protected WebDriver driver;
    protected ConfigReader config = new ConfigReader();

    @BeforeMethod
    public void setup() {
        String browser = config.get("browser").toLowerCase();

        // Dynamic browser 
        switch (browser) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                throw new RuntimeException("Browser type not supported: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(config.get("url"));
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            saveScreenshot();
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}