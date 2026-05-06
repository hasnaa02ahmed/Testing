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
        LaptopsPage productPage = new LaptopsPage(driver); // We can treat this as a generic Product Listing page
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        // Step 1: Login
        Allure.step("Step 1: Login");
        homePage.goToLogin();
        loginPage.login(email, password);

        // Step 2: DYNAMIC NAVIGATION
        Allure.step("Step 2: Navigate to Category: " + category);
        homePage.navigateToCategory(category);

        // Step 3: Select Product
        Allure.step("Step 3: Select Product: " + product);
        productPage.clickProduct(product);

        // Step 3.5: Conditional Logic for Products with Dates (like HP Laptop)
        if (!deliveryDate.equalsIgnoreCase("none")) {
            Allure.step("Step 3.5: Set Delivery Date");
            productPage.setDeliveryDate(deliveryDate);
        }
        productPage.addToCart();

        // Step 4-6: Verify Success and Capture Sub-Total
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
// 2. CAPTURE the Sub-Total while you are still definitely on the Cart Page
        String expectedSubTotal = cartPage.getCartSubTotal();
        Allure.step("Captured Sub-Total: " + expectedSubTotal);
// Only proceed if there is no stock error
        driver.findElement(By.linkText("Checkout")).click();


        // Step 7: Checkout
        Allure.step("Step 7: Click Checkout");
        driver.findElement(By.linkText("Checkout")).click();

        // Steps 8-10: Billing (New Address)
        checkoutPage.fillNewBillingAddress(fn, ln, address, city, post, country, region);

        // Step 11: Shipping (New Address - Requirement fulfillment)
        checkoutPage.fillNewShippingAddress(fn, ln, address, city, post, country, region);

        // Step 12-14: Method, Comment, and Terms
        checkoutPage.addCommentAndContinue(comment);
        checkoutPage.acceptTermsAndContinue();

        // Step 15-16: PRICE VALIDATION
        Allure.step("Step 15-16: Verify final prices");
        Assert.assertTrue(checkoutPage.isConfirmOrderVisible());
        // Now comparing dynamic sub-total captured from cart
        Assert.assertEquals(checkoutPage.getConfirmSubTotal(), expectedSubTotal, "Sub-total mismatch!");
        Assert.assertTrue(checkoutPage.hasFlatShippingRate());

        // Step 17-18: Finalize
        checkoutPage.confirmOrder();
        Assert.assertEquals(checkoutPage.getOrderSuccessMessage(), "Your order has been placed!");

        // Step 19: Logout
        new AccountPage(driver).clickLogout();
    }
}