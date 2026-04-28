package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AccountPage;
import pages.HomePage;
import pages.LoginPage;
import pages.TabletsPage;

public class TabletsTests extends BaseTest {

    @Test
    public void testBreadcrumbAndSideMenu() {
        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.goToLogin();
        login.login("validtest100@gmail.com", "validpass");

        home.goToTablets();
        TabletsPage tablets = new TabletsPage(driver);

        Assert.assertEquals(tablets.getBreadcrumbText(), "Tablets");

        Assert.assertTrue(tablets.getActiveMenuText().contains("Tablets"));

        AccountPage account = new AccountPage(driver);
        account.clickLogout();
    }
}