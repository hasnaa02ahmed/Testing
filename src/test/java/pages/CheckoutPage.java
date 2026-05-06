package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators - Billing
    private By newAddressRadio = By.xpath("//input[@name='payment_address' and @value='new']");
    private By firstNameField = By.id("input-payment-firstname");
    private By lastNameField = By.id("input-payment-lastname");
    private By addressField = By.id("input-payment-address-1");
    private By cityField = By.id("input-payment-city");
    private By postcodeField = By.id("input-payment-postcode");
    private By countryDropdown = By.id("input-payment-country");
    private By zoneDropdown = By.id("input-payment-zone");
    private By continueBillingBtn = By.id("button-payment-address");

    // Locators - Shipping & Payment
    private By continueShippingBtn = By.id("button-shipping-address");
    private By commentArea = By.xpath("//textarea[@name='comment']");
    private By continueMethodBtn = By.id("button-shipping-method");
    private By termsCheckbox = By.name("agree");
    private By continuePaymentBtn = By.id("button-payment-method");
    private By confirmOrderBtn = By.id("button-confirm");
    private By successHeader = By.cssSelector("#content h1");

    private By confirmSection = By.id("collapse-checkout-confirm");
    private By subtotalLocator = By.xpath("//table[@class='table table-bordered table-hover']//tfoot/tr[1]/td[2]");
    private By shippingRateLocator = By.xpath("//table[@class='table table-bordered table-hover']//tfoot/tr[2]/td[2]");
    private By totalLocator = By.xpath("//table[@class='table table-bordered table-hover']//tfoot/tr[3]/td[2]");
    // New Locators for Step 15-16
    private By confirmTableRows = By.cssSelector("#collapse-checkout-confirm table tfoot tr");
    private By footerRows = By.cssSelector("#collapse-checkout-confirm tfoot tr");
    // New Locators - Shipping Address (Step 11)
    private By newShippingAddressRadio = By.xpath("//input[@name='shipping_address' and @value='new']");
    private By shipFirstNameField = By.id("input-shipping-firstname");
    private By shipLastNameField = By.id("input-shipping-lastname");
    private By shipAddressField = By.id("input-shipping-address-1");
    private By shipCityField = By.id("input-shipping-city");
    private By shipPostcodeField = By.id("input-shipping-postcode");
    private By shipCountryDropdown = By.id("input-shipping-country");
    private By shipZoneDropdown = By.id("input-shipping-zone");
    private By continueShippingAddressBtn = By.id("button-shipping-address");

    public boolean isConfirmOrderVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmSection)).isDisplayed();
    }


    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void fillNewBillingAddress(String fn, String ln, String add, String city, String post, String country, String region) {
        wait.until(ExpectedConditions.elementToBeClickable(newAddressRadio)).click();
        driver.findElement(firstNameField).sendKeys(fn);
        driver.findElement(lastNameField).sendKeys(ln);
        driver.findElement(addressField).sendKeys(add);
        driver.findElement(cityField).sendKeys(city);
        driver.findElement(postcodeField).sendKeys(post);

        new Select(driver.findElement(countryDropdown)).selectByVisibleText(country);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@id='input-payment-zone']/option[text()='" + region + "']")));
        new Select(driver.findElement(zoneDropdown)).selectByVisibleText(region);

        driver.findElement(continueBillingBtn).click();
    }
    public void fillNewShippingAddress(String fn, String ln, String add, String city, String post, String country, String region) {
        wait.until(ExpectedConditions.elementToBeClickable(newShippingAddressRadio)).click();

        driver.findElement(shipFirstNameField).sendKeys(fn);
        driver.findElement(shipLastNameField).sendKeys(ln);
        driver.findElement(shipAddressField).sendKeys(add);
        driver.findElement(shipCityField).sendKeys(city);
        driver.findElement(shipPostcodeField).sendKeys(post);

        new Select(driver.findElement(shipCountryDropdown)).selectByVisibleText(country);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//select[@id='input-shipping-zone']/option[text()='" + region + "']")));
        new Select(driver.findElement(shipZoneDropdown)).selectByVisibleText(region);

        driver.findElement(continueShippingAddressBtn).click();
    }




    public void addCommentAndContinue(String comment) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(commentArea)).sendKeys(comment);
        driver.findElement(continueMethodBtn).click();
    }

    public void acceptTermsAndContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(termsCheckbox)).click();
        driver.findElement(continuePaymentBtn).click();
    }



    public void confirmOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmOrderBtn)).click();
    }

    public String getOrderSuccessMessage() {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            longWait.until(ExpectedConditions.urlContains("success"));
            return longWait.until(ExpectedConditions.visibilityOfElementLocated(successHeader)).getText();
        } catch (Exception e) {
            return driver.findElement(By.cssSelector("h1")).getText();
        }}

    private String getAmountByLabel(String labelName) {
        java.util.List<WebElement> rows = driver.findElements(footerRows);
        for (WebElement row : rows) {
            String rowText = row.getText();
            if (rowText.contains(labelName)) {
                // OpenCart table structure: <td>Label:</td> <td>$Value</td>
                // We grab the last <td> in that row
                java.util.List<WebElement> cells = row.findElements(By.tagName("td"));
                return cells.get(cells.size() - 1).getText().trim();
            }
        }
        return "Label Not Found";
    }

    public String getConfirmSubTotal() {
        return getAmountByLabel("Sub-Total");
    }

    public boolean hasFlatShippingRate() {
        String rate = getAmountByLabel("Flat Shipping Rate");
        return !rate.equals("Label Not Found");
    }
}