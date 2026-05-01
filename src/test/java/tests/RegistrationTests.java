package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import configuration.CSVUtils;
import pages.AccountPage;
import pages.HomePage;
import pages.RegisterPage;

public class RegistrationTests extends BaseTest {

    @DataProvider(name = "registrationData")
    public Object[][] getRegistrationData() throws Exception {
        return CSVUtils.getTestData("src/test/java/resources/registration.csv");
    }
    @Test(dataProvider = "registrationData")
    public void testRegistration(String firstName,
                                String lastName,
                                String email,
                                String telephone,
                                String password,
                                String confirmPassword,
                                String agree,
                                String expected,
                                String emailError,
                                String telephoneError,
                                String passwordError) {

        HomePage home = new HomePage(driver);
        home.goToRegister();

        RegisterPage register = new RegisterPage(driver);

        if (email.equalsIgnoreCase("auto")) {
            email = "user" + System.currentTimeMillis() + "@mail.com";
        }

        if (!firstName.isEmpty()) register.enterFirstName(firstName);
        if (!lastName.isEmpty()) register.enterLastName(lastName);
        if (!email.isEmpty()) register.enterEmail(email);
        if (!telephone.trim().isEmpty()) register.enterTelephone(telephone);
        if (!password.isEmpty()) register.enterPassword(password);
        if (!confirmPassword.isEmpty()) register.confirmPassword(confirmPassword);

        if (agree.equalsIgnoreCase("true")) {
            register.acceptPolicy();
        }

        register.clickContinue();

        // Validation
        if (expected.equalsIgnoreCase("success")) {

            AccountPage account = new AccountPage(driver);

            Assert.assertTrue(account.isRegistrationSuccessful());
            Assert.assertTrue(account.isLogoutDisplayed());

            account.clickLogout();

        } else {

            if (!emailError.isEmpty()) {
                Assert.assertEquals(register.getEmailError(), emailError);
            }

            if (!telephoneError.isEmpty()) {
                Assert.assertEquals(register.getTelephoneError(), telephoneError);
            }

            if (!passwordError.isEmpty()) {
                Assert.assertEquals(register.getPasswordError(), passwordError);
            }
        }
    }
}