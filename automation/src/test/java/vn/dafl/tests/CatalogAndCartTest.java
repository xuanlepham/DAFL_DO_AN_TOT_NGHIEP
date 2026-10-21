package vn.dafl.tests;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.pageObjects.CartPageObject;
import vn.dafl.pageObjects.HomePageObject;

@Epic("DAFL Web Application") @Feature("Product Catalog and Shopping Cart")
public class CatalogAndCartTest extends BaseTest {
    @Test(description = "TC_UI_03: Verify product catalog displays product cards")
    @Severity(SeverityLevel.CRITICAL) @Description("Kiem tra danh muc san pham tai va hien thi danh sach the san pham")
    public void testProductCatalogDisplaysCards() {
        HomePageObject home = new HomePageObject(driver).open(baseUrl);
        Assert.assertTrue(home.getProductCardCount() > 0, "Khong co san pham nao duoc hien thi");
    }
    @Test(description = "TC_UI_04: Verify clicking cart button opens Cart Drawer")
    @Severity(SeverityLevel.CRITICAL) @Description("Kiem tra nut Gio hang tren Header mo ngan gio hang thanh cong")
    public void testOpenCartDrawer() {
        new HomePageObject(driver).open(baseUrl).openCart();
        Assert.assertTrue(new CartPageObject(driver).isCartDrawerDisplayed(), "Ngan gio hang khong hien thi sau khi click");
    }
}
