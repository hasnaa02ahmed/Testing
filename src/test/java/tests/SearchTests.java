package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.PhonesPDASPage;
import pages.SearchPage;

import java.util.List;

public class SearchTests extends BaseTest{
    @Test
    public void testSearchMac() {
        HomePage home = new HomePage(driver);
        home.search("Mac");

        PhonesPDASPage page = new PhonesPDASPage(driver);
        List<String> results = page.getProductNames();

        boolean containsMac = results.stream()
                .anyMatch(name -> name.toLowerCase().contains("mac"));

        Assert.assertTrue(containsMac, "No Mac products found in search results!");
    }
    @Test
    public void testSearchInSubCategory() {

        HomePage home = new HomePage(driver);
        home.search("Apple");

        SearchPage search = new SearchPage(driver);

        search.selectCategory("Components");

        search.enableSubCategory();

        search.clickSearch();

        PhonesPDASPage page = new PhonesPDASPage(driver);
        List<String> results = page.getProductNames();

        System.out.println("DEBUG Results: " + results);

        Assert.assertTrue(results.stream()
                        .anyMatch(name -> name.contains("Apple")),
                "Expected Apple product not found!");
    }
}
