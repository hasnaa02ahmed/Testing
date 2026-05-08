package tests;

import base.BaseTest;
import configuration.CSVUtils;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;

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

        tabletsPage.addToCart();
        CartPage cartPage = new CartPage(driver);

        if (cartPage.hasStockError()) {
            saveScreenshot();
            throw new org.testng.SkipException(
                    tabletProduct + " is currently unavailable – skipping test.");
        }


        Allure.step("Step 4: Verify success message for tablet");
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

        Allure.step("Step 7b: Set delivery date to " + deliveryDate);
        laptopsPage.setDeliveryDate(deliveryDate);

        Allure.step("Step 7c: Add '" + laptopProduct + "' to cart");
        laptopsPage.addToCart();

        if (cartPage.hasStockError()) {
            saveScreenshot();
            throw new org.testng.SkipException(
                    laptopProduct + " is currently unavailable – skipping test.");
        }
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



}