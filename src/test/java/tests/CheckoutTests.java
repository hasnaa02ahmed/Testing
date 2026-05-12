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

        HomePage     homePage     = new HomePage(driver);
        LoginPage    loginPage    = new LoginPage(driver);
        ProductPage  productPage  = new ProductPage(driver);
        CartPage     cartPage     = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);


        Allure.step("Step 1: Login with valid credentials");
        homePage.goToLogin();
        loginPage.login(email, password);


        Allure.step("Step 2: Navigate to category → " + category);
        homePage.navigateToCategory(category);


        Allure.step("Step 3: Click on product: " + product);
        productPage.clickProduct(product);

        if (!deliveryDate.equalsIgnoreCase("none")) {
            Allure.step("Step 3.5: Set delivery date to " + deliveryDate);
            productPage.setDeliveryDate(deliveryDate);
        }

        Allure.step("Step 3 (cont.): Click 'Add to Cart'");
        productPage.addToCart();


        Allure.step("Step 4: Verify success message contains product name");
        String successMsg = productPage.getSuccessMessage();
        Assert.assertTrue(
                successMsg.contains(product),
                "Expected success message to contain '" + product + "' but got: " + successMsg
        );


        Allure.step("Step 5: Verify mini-cart shows at least 1 item");
        Assert.assertFalse(
                cartPage.getMiniCartText().contains("0 item(s)"),
                "Mini-cart still shows 0 items after adding '" + product + "' to cart!"
        );

        Allure.step("Step 6: Open full cart page and verify product '" + product + "' is listed");
        cartPage.goToCartPage();
        Assert.assertTrue(
                cartPage.isProductInCart(product),
                "Product '" + product + "' not found in the cart table!"
        );

        Allure.step("Checking for out-of-stock errors");
        if (cartPage.hasStockError()) {
            Allure.step("Product is out of stock — skipping checkout.");
            new AccountPage(driver).clickLogout();
            Assert.fail("Test failed: product is out of stock and checkout cannot proceed.");
            return;
        }

        String expectedSubTotal = cartPage.getCartSubTotal();
        Allure.step("Captured cart Sub-Total: " + expectedSubTotal);

        Allure.step("Step 7: Click 'Checkout'");
        driver.findElement(By.linkText("Checkout")).click();


        Allure.step("Step 8: Fill new billing address");
        checkoutPage.fillNewBillingAddress(fn, ln, address, city, post, country, region);

        Allure.step("Step 9: Verify Shipping Details section opened after billing Continue");
        Assert.assertTrue(
                checkoutPage.isShippingSectionOpen(),
                "Shipping Details section did not open — billing address may have been rejected!"
        );

        Allure.step("Step 9 (cont.): Verify address dropdown contains the newly saved address");
        Assert.assertTrue(
                checkoutPage.isNewAddressInShippingDropdown(fn, ln, city),
                "Shipping address dropdown does not contain the new address " +
                        "(name='" + fn + " " + ln + "', city='" + city + "')!"
        );

        Allure.step("Step 10: Select the newly added shipping address and click Continue");
        checkoutPage.selectExistingShippingAddressAndContinue(
                fn,ln,address,city, region, country );

        Allure.step("Step 11: Verify Delivery Method section is open");
        Assert.assertTrue(
                checkoutPage.isDeliveryMethodSectionOpen(),
                "Delivery Method section did not open after clicking Continue on shipping!"
        );

        Allure.step("Step 13: Add comment and click Continue on Delivery Method");
        checkoutPage.addCommentAndContinue(comment);

        Allure.step("Step 14: Accept Terms & Conditions and click Continue on Payment Method");
        checkoutPage.acceptTermsAndContinue();


        Allure.step("Step 15: Verify Confirm Order section is visible");
        Assert.assertTrue(
                checkoutPage.isConfirmOrderVisible(),
                "Confirm Order section did not appear!"
        );

        Allure.step("Step 15 (cont.): Verify Sub-Total on confirm page matches the cart Sub-Total");
        Assert.assertEquals(
                checkoutPage.getConfirmSubTotal(),
                expectedSubTotal,
                "Sub-total on Confirm Order page does not match the cart Sub-Total!"
        );


        Allure.step("Step 16: Verify Flat Shipping Rate row is present in order totals");
        Assert.assertTrue(
                checkoutPage.hasFlatShippingRate(),
                "Flat Shipping Rate row is missing from the Confirm Order totals!"
        );
        Allure.step("Step 16 (cont.): Flat Shipping Rate value = " + checkoutPage.getFlatShippingRateValue());


        Allure.step("Step 17: Click 'Confirm Order'");
        checkoutPage.confirmOrder();

        Allure.step("Step 18: Verify order success message");
        Assert.assertEquals(
                checkoutPage.getOrderSuccessMessage(),
                "Your order has been placed!",
                "Order success message did not match!"
        );

        Allure.step("Step 18 (cont.): Verify mini-cart shows 0 items after successful order");
        Assert.assertTrue(
                cartPage.getMiniCartText().contains("0 item(s)"),
                "Mini-cart still shows items after a successful order!"
        );


        Allure.step("Step 19: Logout");
        new AccountPage(driver).clickLogout();
    }
}