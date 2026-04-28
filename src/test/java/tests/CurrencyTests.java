package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AccountPage;
import pages.DesktopPage;
import pages.HomePage;
import pages.LoginPage;

public class CurrencyTests extends BaseTest {

    @Test
    public void testChangeCurrency() {

        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);
        
        home.goToLogin();
        login.login("validtest100@gmail.com", "validpass");

        home.goToDesktops();
        DesktopPage desktops = new DesktopPage(driver);

        String priceWithDollar = desktops.getFirstProductPrice();
        Assert.assertTrue(priceWithDollar.contains("$"), "Default currency is not Dollar!");

        home.changeCurrencyToEuro();

        String priceWithEuro = desktops.getFirstProductPrice();
        Assert.assertTrue(priceWithEuro.contains("€"), "Currency did not change to Euro!");

        AccountPage account = new AccountPage(driver);
        account.clickLogout();
    }
}