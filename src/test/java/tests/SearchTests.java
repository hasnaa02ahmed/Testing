package tests;

import base.BaseTest;
import configuration.CSVUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.PhonesPDASPage;
import pages.SearchPage;

import java.util.Arrays;
import java.util.List;

public class SearchTests extends BaseTest {

    @DataProvider(name = "searchAuthData")
    public Object[][] getData() throws Exception {

        Object[][] auth = CSVUtils.getTestData("src/test/java/resources/auth.csv");
        Object[][] tests = CSVUtils.getTestData("src/test/java/resources/test_data.csv");

        return Arrays.stream(auth)
                .flatMap(a -> Arrays.stream(tests)
                        .filter(t -> ((String) t[0]).startsWith("search"))
                        .map(t -> new Object[]{a[0], a[1], t[0], t[1], t[2], t[3]}))
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "searchAuthData")
    public void testSearch(String email, String password,
                           String scenario, String inputValue,
                           String category, String expected) {

        HomePage home = new HomePage(driver);
        home.goToLogin();

        pages.LoginPage login = new pages.LoginPage(driver);
        login.login(email, password);

        Assert.assertTrue(login.isLoggedIn(), "Login failed");

        if (scenario.equals("search_keyword")) {

            home.search(inputValue);

            PhonesPDASPage page = new PhonesPDASPage(driver);

            boolean contains = page.getProductNames()
                    .stream()
                    .anyMatch(name -> name.toLowerCase().contains(inputValue.toLowerCase()));

            Assert.assertTrue(contains, "Expected product not found!");
        }

        else if (scenario.equals("search_subcategories")) {

            home.search(inputValue);

            SearchPage search = new SearchPage(driver);

            search.selectCategory(category);
            search.enableSubCategory();
            search.clickSearch();

            PhonesPDASPage page = new PhonesPDASPage(driver);

            boolean found = page.getProductNames()
                    .stream()
                    .anyMatch(name -> name.contains(expected));

            Assert.assertTrue(found, "Expected product not found in subcategory!");
        }

        new pages.AccountPage(driver).clickLogout();
    }
}


