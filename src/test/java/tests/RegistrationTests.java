package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AccountPage;
import pages.HomePage;
import pages.RegisterPage;

public class RegistrationTests extends BaseTest {

    @Test
    public void registrationWithoutErrors(){

        HomePage home = new HomePage(driver);
        home.goToRegister();

        RegisterPage register = new RegisterPage(driver);

        String email = "user" + System.currentTimeMillis() + "@mail.com";

        register.enterFirstName("John");
        register.enterLastName("Doe");
        register.enterEmail(email);
        register.enterTelephone("1234567890");
        register.enterPassword("12345");
        register.confirmPassword("12345");

        register.acceptPolicy();
        register.clickContinue();

        AccountPage account = new AccountPage(driver);

        Assert.assertTrue(account.isRegistrationSuccessful());

        Assert.assertTrue(account.isLogoutDisplayed());
        account.clickLogout();
    }

    @Test
    public void registrationWithErrors() {
        HomePage home = new HomePage(driver);
        home.goToRegister();

        RegisterPage register = new RegisterPage(driver);
        
        register.enterFirstName("John");
        register.enterLastName("Doe");
        register.clickContinue();

        Assert.assertEquals(register.getEmailError(), "E-Mail Address does not appear to be valid!");
        Assert.assertEquals(register.getTelephoneError(), "Telephone must be between 3 and 32 characters!");
    
        register.enterEmail("test" + System.currentTimeMillis() + "@gmail.com");
        register.enterTelephone("0123456789");

        register.enterPassword("123"); 
        register.confirmPassword("123");
        register.clickContinue();

        Assert.assertEquals(register.getPasswordError(), "Password must be between 4 and 20 characters!");
    }
}