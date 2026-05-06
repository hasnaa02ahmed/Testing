package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import java.util.List;
import java.util.stream.Collectors;

public class PhonesPDASPage {
    WebDriver driver;

    public PhonesPDASPage(WebDriver driver) {
        this.driver = driver;
    }

    By sortDropdown = By.id("input-sort");
    By productNames = By.cssSelector(".product-thumb h4 a");

    public void selectSortOption(String option) {
        WebElement firstProductBefore = driver.findElement(productNames);

        Select select = new Select(driver.findElement(sortDropdown));
        select.selectByVisibleText(option);

        // 3. Wait until the old element is no longer attached to the DOM (the page refreshed)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.stalenessOf(firstProductBefore));
    }

    public List<String> getProductNames() {
        return driver.findElements(productNames)
                .stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }
}
