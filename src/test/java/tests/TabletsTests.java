package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import configuration.CSVUtils;
import pages.AccountPage;
import pages.HomePage;
import pages.LoginPage;
import pages.TabletsPage;

public class TabletsTests extends BaseTest {

    @DataProvider(name = "authData")
    public Object[][] getData() throws Exception {
        return CSVUtils.getTestData("src/test/java/resources/auth.csv");
    }

    @Test(dataProvider = "authData")
    public void testBreadcrumbAndSideMenu(String email, String password) {

        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.goToLogin();
        login.login(email, password);

        Assert.assertTrue(login.isLoggedIn(), "Login failed");

        home.goToTablets();
        TabletsPage tablets = new TabletsPage(driver);

        Assert.assertEquals(tablets.getBreadcrumbText(), "Tablets");
        Assert.assertTrue(tablets.getActiveMenuText().contains("Tablets"));

        new AccountPage(driver).clickLogout();
    }
}