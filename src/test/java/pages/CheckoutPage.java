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

    private By newAddressRadio          = By.cssSelector("input[name='payment_address'][value='new']");
    private By firstNameField           = By.id("input-payment-firstname");
    private By lastNameField            = By.id("input-payment-lastname");
    private By addressField             = By.id("input-payment-address-1");
    private By cityField                = By.id("input-payment-city");
    private By postcodeField            = By.id("input-payment-postcode");
    private By countryDropdown          = By.id("input-payment-country");
    private By zoneDropdown             = By.id("input-payment-zone");
    private By continueBillingBtn       = By.id("button-payment-address");

    private By newShippingAddressRadio      = By.cssSelector("input[name='shipping_address'][value='new']");
    private By existingShippingAddressRadio = By.cssSelector("input[name='shipping_address'][value='existing']");
    private By existingShippingAddressDropdown = By.cssSelector("#shipping-existing select[name='address_id']");
    private By shipFirstNameField       = By.id("input-shipping-firstname");
    private By shipLastNameField        = By.id("input-shipping-lastname");
    private By shipAddressField         = By.id("input-shipping-address-1");
    private By shipCityField            = By.id("input-shipping-city");
    private By shipPostcodeField        = By.id("input-shipping-postcode");
    private By shipCountryDropdown      = By.id("input-shipping-country");
    private By shipZoneDropdown         = By.id("input-shipping-zone");

    private By continueShippingAddressBtn = By.id("button-shipping-address");
    private By continueMethodBtn          = By.id("button-shipping-method");
    private By continuePaymentBtn         = By.id("button-payment-method");

    private By commentArea      = By.cssSelector("textarea[name='comment']");
    private By termsCheckbox    = By.name("agree");
    private By confirmOrderBtn  = By.id("button-confirm");
    private By successHeader    = By.cssSelector("#content h1");

    private By shippingSection           = By.id("collapse-shipping-address");
    private By deliveryMethodSection     = By.id("collapse-shipping-method");
    private By confirmSection            = By.id("collapse-checkout-confirm");
    private By billingAddressConfirmPanel = By.cssSelector("#collapse-checkout-payment");

    private By footerRows = By.cssSelector("#collapse-checkout-confirm tfoot tr");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }


    public void clickContinueShippingAddress() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShippingAddressBtn)).click();
    }


    public void fillNewBillingAddress(String fn, String ln, String add,
                                      String city, String post,
                                      String country, String region) {
        WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(newAddressRadio));
        radio.click();

        driver.findElement(firstNameField).sendKeys(fn);
        driver.findElement(lastNameField).sendKeys(ln);
        driver.findElement(addressField).sendKeys(add);
        driver.findElement(cityField).sendKeys(city);
        driver.findElement(postcodeField).sendKeys(post);

        new Select(driver.findElement(countryDropdown)).selectByVisibleText(country);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//select[@id='input-payment-zone']/option[text()='" + region + "']")));
        new Select(driver.findElement(zoneDropdown)).selectByVisibleText(region);

        driver.findElement(continueBillingBtn).click();
    }


    public boolean isNewAddressInShippingDropdown(String firstName, String lastName, String city) {
        try {
            WebElement dropdown = wait.until(
                    ExpectedConditions.presenceOfElementLocated(existingShippingAddressDropdown)
            );
            Select select = new Select(dropdown);
            return select.getOptions().stream()
                    .map(WebElement::getText)
                    .anyMatch(text ->
                            text.toLowerCase().contains(firstName.toLowerCase()) &&
                                    text.toLowerCase().contains(lastName.toLowerCase())  &&
                                    text.toLowerCase().contains(city.toLowerCase())
                    );
        } catch (Exception e) {
            return false;
        }
    }


    public void selectExistingShippingAddressAndContinue(
            String firstName, String lastName,
            String address,
            String city,
            String region,
            String country
    ) {

        WebElement radio = wait.until(
                ExpectedConditions.elementToBeClickable(existingShippingAddressRadio)
        );

        if (!radio.isSelected()) {
            radio.click();
        }

        WebElement dropdownElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(existingShippingAddressDropdown)
        );

        Select dropdown = new Select(dropdownElement);

        String expected =
                firstName.toLowerCase().trim() +lastName.toLowerCase().trim()+ "," +
                        address.toLowerCase().trim() + "," +
                        city.toLowerCase().trim() + "," +
                        region.toLowerCase().trim() + "," +
                        country.toLowerCase().trim();

        boolean found = false;

        for (WebElement option : dropdown.getOptions()) {

            String actual = option.getText()
                    .toLowerCase()
                    .replace(" ", "")
                    .trim();

            String expectedFormatted =
                    expected.replace(" ", "");

//            System.out.println("OPTION = " + actual);

            if (actual.equals(expectedFormatted)) {

                dropdown.selectByVisibleText(option.getText());

                System.out.println(
                        "SELECTED = " +
                                dropdown.getFirstSelectedOption().getText()
                );

                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("Correct address not found!");
        }

        clickContinueShippingAddress();
    }
    public boolean isShippingSectionOpen() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(shippingSection)).isDisplayed();
    }

    public boolean isDeliveryMethodSectionOpen() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(deliveryMethodSection)).isDisplayed();
    }

    public boolean isConfirmOrderVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmSection)).isDisplayed();
    }

    // ── Delivery Method ──────────────────────────────────────────────────────
    public void addCommentAndContinue(String comment) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(commentArea)).sendKeys(comment);
        driver.findElement(continueMethodBtn).click();
    }

    // ── Payment Method ───────────────────────────────────────────────────────
    public void acceptTermsAndContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(termsCheckbox)).click();
        driver.findElement(continuePaymentBtn).click();
    }

    // ── Confirm Order ────────────────────────────────────────────────────────
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
        }
    }

    // ── Totals helpers ───────────────────────────────────────────────────────
    private String getAmountByLabel(String labelName) {
        java.util.List<WebElement> rows = driver.findElements(footerRows);
        for (WebElement row : rows) {
            if (row.getText().contains(labelName)) {
                java.util.List<WebElement> cells = row.findElements(By.tagName("td"));
                return cells.get(cells.size() - 1).getText().trim();
            }
        }
        return "Label Not Found";
    }

    public String getConfirmSubTotal() {
        return getAmountByLabel("Sub-Total");
    }

    public String getFlatShippingRateValue() {
        return getAmountByLabel("Flat Shipping Rate");
    }

    public String getConfirmTotal() {
        return getAmountByLabel("Total");
    }

    public boolean hasFlatShippingRate() {
        return !getAmountByLabel("Flat Shipping Rate").equals("Label Not Found");
    }

//    // ── Legacy / unused — kept for backward compatibility ────────────────────
//    public boolean isBillingAddressConfirmed(String city, String postcode) {
//        try {
//            WebElement panel = wait.until(
//                    ExpectedConditions.visibilityOfElementLocated(billingAddressConfirmPanel));
//            String panelText = panel.getText();
//            return panelText.toLowerCase().contains(city.toLowerCase())
//                    || panelText.toLowerCase().contains(postcode.toLowerCase());
//        } catch (Exception e) {
//            return false;
//        }
//    }

//    public void fillNewShippingAddress(String fn, String ln, String add,
//                                       String city, String post,
//                                       String country, String region) {
//        WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(newShippingAddressRadio));
//        radio.click();
//
//        driver.findElement(shipFirstNameField).sendKeys(fn);
//        driver.findElement(shipLastNameField).sendKeys(ln);
//        driver.findElement(shipAddressField).sendKeys(add);
//        driver.findElement(shipCityField).sendKeys(city);
//        driver.findElement(shipPostcodeField).sendKeys(post);
//
//        new Select(driver.findElement(shipCountryDropdown)).selectByVisibleText(country);
//        wait.until(ExpectedConditions.presenceOfElementLocated(
//                By.xpath("//select[@id='input-shipping-zone']/option[text()='" + region + "']")));
//        new Select(driver.findElement(shipZoneDropdown)).selectByVisibleText(region);
//
//        // Uses the extracted method instead of a raw driver.findElement call
//        clickContinueShippingAddress();
//    }

}