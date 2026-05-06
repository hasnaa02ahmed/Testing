package tests;

import base.BaseTest;
import configuration.CSVUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.PhonesPDASPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortTests extends BaseTest {

    @DataProvider(name = "sortAuthData")
    public Object[][] getData() throws Exception {

        Object[][] auth = CSVUtils.getTestData("src/test/java/resources/auth.csv");
        Object[][] tests = CSVUtils.getTestData("src/test/java/resources/test_data.csv");

        return Arrays.stream(auth)
                .flatMap(a -> Arrays.stream(tests)
                        .filter(t -> ((String) t[0]).startsWith("sort"))
                        .map(t -> new Object[]{a[0], a[1], t[0], t[1], t[2], t[3]}))
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "sortAuthData")
    public void testSort(String email, String password,
                         String scenario, String inputValue,
                         String category, String expected) {

        HomePage home = new HomePage(driver);
        home.goToLogin();

        pages.LoginPage login = new pages.LoginPage(driver);
        login.login(email, password);

        Assert.assertTrue(login.isLoggedIn(), "Login failed");

        home.navigateToPhones();

        PhonesPDASPage phones = new PhonesPDASPage(driver);
        phones.selectSortOption(inputValue);

        List<String> actual = phones.getProductNames();
        List<String> expectedList = new ArrayList<>(actual);

        if (scenario.equals("sort_ascending")) {
            expectedList.sort(String.CASE_INSENSITIVE_ORDER);
        }

        else if (scenario.equals("sort_descending")) {
            expectedList.sort(String.CASE_INSENSITIVE_ORDER.reversed());
        }

        System.out.println("DEBUG Actual: " + actual);
        System.out.println("DEBUG Expected: " + expectedList);

        Assert.assertEquals(actual, expectedList, "Sorting validation failed!");

        new pages.AccountPage(driver).clickLogout();
    }
}
