package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import configuration.CSVUtils;
import pages.HomePage;
import pages.LoginPage;


public class LoginTests extends BaseTest {
    @DataProvider(name = "loginData")
    public Object[][] getData() throws Exception {
        return CSVUtils.getTestData("src/test/java/resources/login.csv");
    }

@Test(dataProvider = "loginData")
    public void testLogin(String email, String password, String expected) {

        HomePage home = new HomePage(driver);
        home.goToLogin();

        LoginPage login = new LoginPage(driver);
        login.login(email, password);

        if (expected.equalsIgnoreCase("valid")) {
            Assert.assertTrue(login.isLoggedIn(), "Login should succeed");
        } else {
            Assert.assertTrue(
                login.getErrorMessage().contains("No match"),
                "Error message not displayed for invalid login"
            );
        }
    }
}

