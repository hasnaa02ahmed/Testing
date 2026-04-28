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
        return CSVUtils.getTestData("data.csv");
    }

   @Test
   public void invalidLogin(){

       HomePage home = new HomePage(driver);
       home.goToLogin();

       LoginPage login = new LoginPage(driver);
       login.login("wrong@email.com","wrongpass");

       Assert.assertTrue(login.getErrorMessage()
               .contains("Warning: No match for E-Mail Address"));
   }

   @Test
   public void validLogin(){
       HomePage home = new HomePage(driver);
       home.goToLogin();

       LoginPage login = new LoginPage(driver);
       login.login("validtest100@gmail.com","validpass");

       Assert.assertTrue(login.isLoggedIn());
   }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, String expected) {
        HomePage home = new HomePage(driver);
        home.goToLogin();

        LoginPage login = new LoginPage(driver);
        login.login(username, password);

        if (expected.equals("valid")) {
            Assert.assertTrue(login.isLoggedIn());
        } else {
            Assert.assertTrue(login.getErrorMessage()
                    .contains("Warning: No match for E-Mail Address"));
        }
    }
}

