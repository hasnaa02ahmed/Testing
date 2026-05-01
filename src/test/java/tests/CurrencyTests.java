package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import configuration.CSVUtils;
import pages.AccountPage;
import pages.DesktopPage;
import pages.HomePage;
import pages.LoginPage;

public class CurrencyTests extends BaseTest {

    @DataProvider(name = "authData")
    public Object[][] getData() throws Exception {
        return CSVUtils.getTestData("src/test/java/resources/auth.csv");
    }

    @Test(dataProvider = "authData")
    public void testChangeCurrency(String email, String password) {

        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.goToLogin();
        login.login(email, password);

        Assert.assertTrue(login.isLoggedIn(), "Login failed");

        home.goToDesktops();
        DesktopPage desktops = new DesktopPage(driver);

        String priceDollar = desktops.getFirstProductPrice();
        Assert.assertTrue(priceDollar.contains("$"));

        home.changeCurrencyToEuro();

        String priceEuro = desktops.getFirstProductPrice();
        Assert.assertTrue(priceEuro.contains("€"));

        Assert.assertNotEquals(priceDollar, priceEuro);

        new AccountPage(driver).clickLogout();
    }
}