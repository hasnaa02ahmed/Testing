package tests;

import base.BaseTest;
import configuration.CSVUtils;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;

@Epic("Regression Tests")
@Feature("Checkout Process")
public class CheckoutTests extends BaseTest {

    @DataProvider(name = "checkoutData")
    public Object[][] getCheckoutData() throws Exception {
        return CSVUtils.getTestData("src/test/java/resources/checkout_data.csv");
    }

    @Test(dataProvider = "checkoutData", description = "Dynamic End-to-End Checkout")
    public void testEndToEndCheckout(String category, String email, String password, String product,
                                     String deliveryDate, String fn, String ln, String address,
                                     String city, String post, String country, String region, String comment) {

        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        LaptopsPage productPage = new LaptopsPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        Allure.step("Step 1: Login");
        homePage.goToLogin();
        loginPage.login(email, password);

        Allure.step("Step 2: Navigate to Category: " + category);
        homePage.navigateToCategory(category);

        Allure.step("Step 3: Select Product: " + product);
        productPage.clickProduct(product);

        if (!deliveryDate.equalsIgnoreCase("none")) {
            Allure.step("Step 3.5: Set Delivery Date");
            productPage.setDeliveryDate(deliveryDate);
        }
        productPage.addToCart();

        Allure.step("Step 4-6: Verify Cart");
        Assert.assertTrue(productPage.getSuccessMessage().contains(product));
        cartPage.goToCartPage();
        Allure.step("Checking for Out of Stock items");
        if (cartPage.hasStockError()) {
            Allure.step("Product is out of stock! Skipping checkout.");

            new AccountPage(driver).clickLogout();
            Assert.fail("Test failed because the product is out of stock and checkout is blocked.");
            return;
        }
        String expectedSubTotal = cartPage.getCartSubTotal();
        Allure.step("Captured Sub-Total: " + expectedSubTotal);
        driver.findElement(By.linkText("Checkout")).click();


        Allure.step("Step 7: Click Checkout");
        driver.findElement(By.linkText("Checkout")).click();

        checkoutPage.fillNewBillingAddress(fn, ln, address, city, post, country, region);

        checkoutPage.fillNewShippingAddress(fn, ln, address, city, post, country, region);

        checkoutPage.addCommentAndContinue(comment);
        checkoutPage.acceptTermsAndContinue();

        Allure.step("Step 15-16: Verify final prices");
        Assert.assertTrue(checkoutPage.isConfirmOrderVisible());
        Assert.assertEquals(checkoutPage.getConfirmSubTotal(), expectedSubTotal, "Sub-total mismatch!");
        Assert.assertTrue(checkoutPage.hasFlatShippingRate());

        checkoutPage.confirmOrder();
        Assert.assertEquals(checkoutPage.getOrderSuccessMessage(), "Your order has been placed!");
        Assert.assertTrue(cartPage.getMiniCartText().contains("0 item(s)"), "Cart is not empty after checkout!");

        // Step 19: Logout
        new AccountPage(driver).clickLogout();
    }
}