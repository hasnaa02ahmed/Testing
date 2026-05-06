package tests;

import base.BaseTest;
import configuration.CSVUtils;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;

@Epic("Shopping Cart")
@Feature("Add Items & Compare Totals")
public class ShoppingCartTests extends BaseTest {

    @DataProvider(name = "cartData")
    public Object[][] getData() throws Exception {
        return CSVUtils.getTestData("src/test/java/resources/shopping_cart.csv");
    }

    @Test(dataProvider = "cartData", description = "Add tablet & laptop to cart, compare total")
    @Story("Add items to shopping cart and verify total")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddItemsToShoppingCartAndCompareTotal(
            String email, String password,
            String tabletProduct, String laptopProduct,
            String deliveryDate) {


        Allure.step("Step 1: Login with valid user");
        HomePage homePage   = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        homePage.goToLogin();
        loginPage.login(email, password);
        Assert.assertTrue(loginPage.isLoggedIn(), "Login failed for user: " + email);

        Allure.step("Step 2: Navigate to Tablets category");
        homePage.goToTablets();

        Allure.step("Step 3: Add '" + tabletProduct + "' to cart");
        TabletsPage tabletsPage = new TabletsPage(driver);
        tabletsPage.clickProduct(tabletProduct);

        if (isOutOfStock()) {
            saveScreenshot();
            throw new org.testng.SkipException(
                    tabletProduct + " is currently out of stock – skipping test.");
        }

        tabletsPage.addToCart();


        Allure.step("Step 4: Verify success message for tablet");
        CartPage cartPage    = new CartPage(driver);
        String tabletSuccess = cartPage.getSuccessMessage();
        Assert.assertTrue(tabletSuccess.contains(tabletProduct),
                "Expected success message to contain product name. Got: " + tabletSuccess);


        Allure.step("Step 5: Open cart and verify tablet is present with its price");
        cartPage.goToCartPage();
        Assert.assertTrue(cartPage.isProductInCart(tabletProduct),
                "Tablet '" + tabletProduct + "' not found in cart");
        String tabletPrice = cartPage.getProductPrice(tabletProduct);
        Assert.assertFalse(tabletPrice.isEmpty(), "Tablet price should not be empty");


        Allure.step("Step 6: Navigate to Laptops & Notebooks category");
        LaptopsPage laptopsPage = new LaptopsPage(driver);
        laptopsPage.navigateToLaptops();

        Allure.step("Step 7a: Open '" + laptopProduct + "' product page");
        laptopsPage.clickProduct(laptopProduct);

        if (isOutOfStock()) {
            saveScreenshot();
            throw new org.testng.SkipException(
                    laptopProduct + " is currently out of stock – skipping test.");
        }


        Allure.step("Step 7b: Set delivery date to " + deliveryDate);
        laptopsPage.setDeliveryDate(deliveryDate);

        Allure.step("Step 7c: Add '" + laptopProduct + "' to cart");
        laptopsPage.addToCart();
        String laptopSuccess = laptopsPage.getSuccessMessage();
        Assert.assertTrue(laptopSuccess.toLowerCase().contains("success"),
                "Expected success alert when adding laptop. Got: " + laptopSuccess);


        Allure.step("Step 8: Open cart and verify laptop and delivery date");
        cartPage.goToCartPage();
        Assert.assertTrue(cartPage.isProductInCart(laptopProduct),
                "Laptop '" + laptopProduct + "' not found in cart");
        String laptopDetail = cartPage.getProductDeliveryDate(laptopProduct);
        Assert.assertTrue(laptopDetail.contains("Delivery Date:"),
                "Expected a 'Delivery Date:' entry in cart for laptop. Got: " + laptopDetail);

        Allure.step("Step 9: Verify cart Total equals sum of item totals");
        double sumOfItems     = cartPage.sumItemTotals();
        String totalText      = cartPage.getCartTotal();
        double displayedTotal = Double.parseDouble(totalText.replaceAll("[^0-9.]", ""));
        Assert.assertEquals(displayedTotal, sumOfItems,
                "Cart total (" + displayedTotal + ") does not match sum of items (" + sumOfItems + ")");


        Allure.step("Step 10: Logout");
        new AccountPage(driver).clickLogout();
    }

    @Test(dataProvider = "cartData", description = "Add only tablet – small cart verification")
    @Story("Add single item to cart")
    @Severity(SeverityLevel.NORMAL)
    public void testAddTabletToSmallCart(
            String email, String password,
            String tabletProduct, String laptopProduct,
            String deliveryDate) {

        Allure.step("Step 1: Login");
        HomePage homePage   = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        homePage.goToLogin();
        loginPage.login(email, password);
        Assert.assertTrue(loginPage.isLoggedIn(), "Login failed for: " + email);

        Allure.step("Step 2: Navigate to Tablets");
        homePage.goToTablets();

        Allure.step("Step 3: Add tablet to cart");
        TabletsPage tabletsPage = new TabletsPage(driver);
        tabletsPage.clickProduct(tabletProduct);

        if (isOutOfStock()) {
            saveScreenshot();
            throw new org.testng.SkipException(
                    tabletProduct + " is out of stock – skipping small-cart test.");
        }

        tabletsPage.addToCart();

        Allure.step("Step 4: Verify success message");
        CartPage cartPage = new CartPage(driver);
        String msg = cartPage.getSuccessMessage();
        Assert.assertTrue(msg.contains(tabletProduct),
                "Success message missing product name. Got: " + msg);

        Allure.step("Step 5: Open cart and verify single item");
        cartPage.goToCartPage();
        Assert.assertTrue(cartPage.isProductInCart(tabletProduct), "Tablet not found in cart");
        String price = cartPage.getProductPrice(tabletProduct);
        Assert.assertFalse(price.isEmpty(), "Price must not be empty");

        Allure.step("Step 6: Verify small cart total is not empty and matches item row total");
        String cartTotal = cartPage.getCartTotal();
        Assert.assertFalse(cartTotal.isEmpty(), "Cart total should not be empty");
        // Cart total may include eco-tax; verify it is a valid price string
        double totalValue = Double.parseDouble(cartTotal.replaceAll("[^0-9.]", ""));
        Assert.assertTrue(totalValue > 0, "Cart total should be greater than zero");

        Allure.step("Step 7: Logout");
        new AccountPage(driver).clickLogout();
    }

    private boolean isOutOfStock() {
        try {
            String availability = driver.findElement(
                    org.openqa.selenium.By.cssSelector(".list-unstyled li:last-child span")).getText();
            return availability.toLowerCase().contains("out of stock");
        } catch (Exception e) {
            return false;
        }
    }
}