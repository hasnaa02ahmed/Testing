package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {

    WebDriver driver;
    WebDriverWait wait;

    By cartBtn      = By.id("cart");
    By viewCartLink = By.linkText("View Cart");

    By cartPageTable = By.cssSelector("#content form table");
    By cartRows      = By.cssSelector("#content form table tbody tr");


    By successAlert  = By.cssSelector(".alert-success");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public String getSuccessMessage() {
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
        return alert.getText();
    }

    public void goToCartPage() {
        WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(cartBtn));
        cartButton.click();

        By viewCartSelector = By.cssSelector("ul.dropdown-menu a[href*='checkout/cart']");

        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(viewCartSelector));
        link.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(cartPageTable));
    }
    public boolean isProductInCart(String partialName) {
        List<WebElement> rows = driver.findElements(cartRows);
        for (WebElement row : rows) {
            if (row.getText().toLowerCase().contains(partialName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public String getProductPrice(String partialName) {
        List<WebElement> rows = driver.findElements(cartRows);
        for (WebElement row : rows) {
            if (row.getText().toLowerCase().contains(partialName.toLowerCase())) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() >= 5) {
                    return cells.get(4).getText().trim();
                }
            }
        }
        return "";
    }


    public String getProductDeliveryDate(String partialName) {
        List<WebElement> rows = driver.findElements(cartRows);
        for (WebElement row : rows) {
            if (row.getText().toLowerCase().contains(partialName.toLowerCase())) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() >= 2) {
                    return cells.get(1).getText().trim();
                }
            }
        }
        return "";
    }

    public String getCartTotal() {
        List<WebElement> tfootRows = driver.findElements(
                By.cssSelector("#content table tfoot tr"));
        if (!tfootRows.isEmpty()) {
            WebElement lastRow = tfootRows.get(tfootRows.size() - 1);
            List<WebElement> cells = lastRow.findElements(By.tagName("td"));
            if (cells.size() >= 2) {
                return cells.get(cells.size() - 1).getText().trim();
            }
        }

        List<WebElement> allRows = driver.findElements(
                By.cssSelector("#content table tr"));
        String lastPrice = "";
        for (WebElement row : allRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() == 2) {
                String label = cells.get(0).getText().trim();
                String value = cells.get(1).getText().trim();
                if (!value.isEmpty()) {
                    lastPrice = value;               // keep updating — last row = Total
                    if (label.equalsIgnoreCase("Total")) {
                        return value;
                    }
                }
            }
        }
        return lastPrice;
    }

    public String getCartSubTotal() {
        List<WebElement> rows = driver.findElements(By.cssSelector("#content .row table tr, #content table tfoot tr"));
        for (WebElement row : rows) {
            if (row.getText().contains("Sub-Total")) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                return cells.get(cells.size() - 1).getText().trim();
            }
        }
        return "";
    }
    public boolean hasStockError() {
        try {
            WebElement alert = driver.findElement(By.cssSelector(".alert-danger"));
            return alert.getText().contains("Products marked with *** are not available");
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }


    public double sumItemTotals() {
        List<WebElement> rows = driver.findElements(cartRows);
        double sum = 0.0;
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 6) {
                String text = cells.get(5).getText().trim().replaceAll("[^0-9.]", "");
                try { sum += Double.parseDouble(text); }
                catch (NumberFormatException ignored) { }
            }
        }
        return sum;
    }
    public String getMiniCartText() {
        return driver.findElement(By.id("cart-total")).getText();
    }
}