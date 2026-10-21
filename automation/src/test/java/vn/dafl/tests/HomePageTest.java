package vn.dafl.tests;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.pageObjects.HomePageObject;

@Epic("DAFL Web Application") @Feature("Home Page & Header Verification")
public class HomePageTest extends BaseTest {
    @Test(description = "TC_UI_01: Verify home page loads and brand header is displayed")
    @Severity(SeverityLevel.BLOCKER) @Description("Kiem tra trang chu tai thanh cong va hien thi logo thuong hieu DAFL Store")
    public void testHomePageLoadsSuccessfully() {
        HomePageObject home = new HomePageObject(driver).open(baseUrl);
        Assert.assertTrue(home.isBrandDisplayed(), "Logo thuong hieu khong tim thay");
        Assert.assertTrue(home.getBrandText().contains("DAFL"), "Ten thuong hieu khong chua DAFL");
    }
    @Test(description = "TC_UI_02: Verify search input and theme toggle are interactive")
    @Severity(SeverityLevel.NORMAL) @Description("Kiem tra thanh tim kiem san pham va nut chuyen doi giao dien sang/toi")
    public void testSearchInputAndThemeToggle() {
        HomePageObject home = new HomePageObject(driver).open(baseUrl);
        home.search("Áo");
        Assert.assertEquals(home.getSearchValue(), "Áo");
        Assert.assertTrue(home.isThemeToggleDisplayed(), "Nut chuyen theme khong hien thi");
    }
}
