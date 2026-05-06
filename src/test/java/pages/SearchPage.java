package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class SearchPage {
    WebDriver driver;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
    }

    By categoryDropdown = By.name("category_id");
    By subCategoryCheckbox = By.name("sub_category");
    By searchBox = By.name("search");
    By searchBtn = By.id("button-search");

    public void enterSearch(String text) {
        driver.findElement(searchBox).clear();
        driver.findElement(searchBox).sendKeys(text);
    }

    public void selectCategory(String category) {
        Select select = new Select(driver.findElement(categoryDropdown));
        select.selectByVisibleText(category);
    }

    public void enableSubCategory() {
        WebElement checkbox = driver.findElement(subCategoryCheckbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void clickSearch() {
        driver.findElement(searchBtn).click();
    }
}
