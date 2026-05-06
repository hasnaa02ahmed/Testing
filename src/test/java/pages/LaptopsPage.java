package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LaptopsPage {

    WebDriver driver;
    WebDriverWait wait;

    By laptopsNotebooksMenu = By.linkText("Laptops & Notebooks");
    By showAllLaptops       = By.linkText("Show AllLaptops & Notebooks");

    By productCards = By.cssSelector(".product-thumb h4 a");

    By addToCartBtn = By.id("button-cart");
    By successAlert = By.cssSelector(".alert-success");


    By deliveryDateInput = By.cssSelector("#product input[type='date'], " +
            "#product input[type='text'][id^='input-option']");

    public LaptopsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateToLaptops() {
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(laptopsNotebooksMenu));
        menu.click();
        wait.until(ExpectedConditions.elementToBeClickable(showAllLaptops)).click();
    }

    public void clickProduct(String partialName) {
        List<WebElement> products = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(productCards));
        for (WebElement p : products) {
            if (p.getText().toLowerCase().contains(partialName.toLowerCase())) {
                try {
                    p.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", p);
                }
                return;
            }
        }
        throw new RuntimeException("Product not found: " + partialName);
    }


    public void setDeliveryDate(String date) {
        try {
            WebElement field = wait.until(
                    ExpectedConditions.presenceOfElementLocated(deliveryDateInput));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView(true);", field);

            String[] parts = date.split("-");           // [2027, 12, 31]
            String chromeFormat = parts[1] + parts[2] + parts[0]; // 12312027

            field.click();
            field.sendKeys(org.openqa.selenium.Keys.chord(
                    org.openqa.selenium.Keys.CONTROL, "a"));
            field.sendKeys(chromeFormat);

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new Event('change'));", field);

            System.out.println("Delivery date set to: " + date);

        } catch (Exception e) {
            System.out.println("No delivery date field found, skipping. " + e.getMessage());
        }
    }

    public void addToCart() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));
        btn.click();
    }

    public String getSuccessMessage() {
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
        return alert.getText();
    }
}