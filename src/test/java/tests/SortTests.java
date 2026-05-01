package tests;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.PhonesPDASPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortTests extends BaseTest {
    @Test
    public void testSortAZ() {
        HomePage home = new HomePage(driver);
        home.navigateToPhones();

        PhonesPDASPage phones = new PhonesPDASPage(driver);
        phones.selectSortOption("Name (A - Z)");

        List<String> actual = phones.getProductNames();
        List<String> expected = new ArrayList<>(actual);

        expected.sort(String.CASE_INSENSITIVE_ORDER);

        System.out.println("DEBUG: Actual List from Web: " + actual);
        System.out.println("DEBUG: Expected List (Sorted): " + expected);

        Assert.assertEquals(actual, expected, "The lists do not match!");
    }

    @Test
    public void testSortZA() {
        HomePage home = new HomePage(driver);
        home.navigateToPhones();
        PhonesPDASPage phones = new PhonesPDASPage(driver);

        phones.selectSortOption("Name (Z - A)");

        List<String> actual = phones.getProductNames();
        List<String> expected = new ArrayList<>(actual);

        expected.sort(String.CASE_INSENSITIVE_ORDER.reversed());

        System.out.println("DEBUG: Actual List from Web: " + actual);
        System.out.println("DEBUG: Expected List: " + expected);

        Assert.assertEquals(actual, expected, "The list was not sorted Z-A!");
    }

}
