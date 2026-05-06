package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class TabletsPage {
    WebDriver driver;
    WebDriverWait wait;

    // ── existing locators (unchanged) ────────────────────────────────────────
    By breadcrumbActive = By.cssSelector(".breadcrumb li:last-child a");
    By activeSideMenu   = By.cssSelector(".list-group a.active");

    // ── new locators for product interaction ─────────────────────────────────
    By productTitles = By.cssSelector(".product-thumb h4 a");
    By addToCartBtn  = By.id("button-cart");
    By successAlert  = By.cssSelector(".alert-success");

    public TabletsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── existing methods (unchanged) ─────────────────────────────────────────

    public String getBreadcrumbText() {
        return driver.findElement(breadcrumbActive).getText();
    }

    public String getActiveMenuText() {
        return driver.findElement(activeSideMenu).getText();
    }

    // ── new methods ───────────────────────────────────────────────────────────

    /** Clicks the product whose title contains the given partial name. */
    public void clickProduct(String partialName) {
        List<WebElement> products = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(productTitles));
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
        throw new RuntimeException("Product not found in Tablets listing: " + partialName);
    }

    /** Clicks "Add to Cart" on a product detail page. */
    public void addToCart() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));
        btn.click();
    }

    /** Waits for and returns the success alert text after adding to cart. */
    public String getSuccessMessage() {
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
        return alert.getText();
    }
}