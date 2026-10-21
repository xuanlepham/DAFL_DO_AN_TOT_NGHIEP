package vn.dafl.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.pageObjects.HomePageObject;
import vn.dafl.pageObjects.ProductCatalogPageObject;

/** Kiem thu danh muc san pham: hien thi, tim kiem, loc danh muc, chi tiet san pham. */
public class CatalogAndSearchTest extends BaseTest {

    @Test(description = "Trang chu hien thi danh sach san pham")
    public void homePageDisplaysProducts() {
        openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        Assert.assertTrue(catalog.getProductCount() > 0, "Trang chu khong co san pham nao");
        String firstId = catalog.getFirstProductId();
        Assert.assertFalse(catalog.getProductName(firstId).isEmpty(), "San pham dau tien khong co ten");
        Assert.assertTrue(parseMoney(catalog.getProductPriceText(firstId)) > 0, "Gia san pham phai lon hon 0");
    }

    @Test(description = "Tim kiem theo tu khoa lay tu ten san pham that")
    public void searchByProductKeyword() {
        HomePageObject homePage = openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        String productName = catalog.getProductName(catalog.getFirstProductId());
        String keyword = productName.split(" ")[0];

        homePage.enterSearchKeyword(keyword);
        catalog.sleepInSecond(2); // cho ket qua tim kiem cap nhat
        Assert.assertTrue(catalog.getProductCount() > 0,
                "Tim kiem '" + keyword + "' khong tra ve san pham nao");
        boolean found = false;
        for (int i = 0; i < catalog.getProductCount(); i++) {
            if (catalog.getProductName(catalog.getProductIdAt(i)).contains(keyword)) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found, "Ket qua tim kiem khong chua san pham co ten chua '" + keyword + "'");
    }

    @Test(description = "Tim kiem tu khoa khong ton tai -> khong co san pham / trang thai rong")
    public void searchWithNoMatchingKeyword() {
        HomePageObject homePage = openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        homePage.enterSearchKeyword("xyzkhongtontai123456");
        catalog.sleepInSecond(2);
        Assert.assertTrue(catalog.getProductCount() == 0 || catalog.isCategoryEmptyStateDisplayed(),
                "Van hien thi san pham khi tim kiem tu khoa khong ton tai");
    }

    @Test(description = "Loc san pham theo danh muc dau tien")
    public void filterByFirstCategory() {
        HomePageObject homePage = openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        String categoryId = homePage.getFirstCategoryId();
        homePage.clickCategoryPillById(categoryId);
        catalog.sleepInSecond(2);
        Assert.assertTrue(catalog.getProductCount() > 0,
                "Loc theo danh muc " + categoryId + " khong co san pham nao");
    }

    @Test(description = "Mo trang chi tiet san pham va quay lai")
    public void openProductDetailAndBack() {
        openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        catalog.openProductDetail(catalog.getFirstProductId());
        Assert.assertTrue(catalog.isProductDetailDisplayed(), "Khong mo duoc trang chi tiet san pham");
        catalog.backToCatalog();
        catalog.waitForProductsLoaded();
        Assert.assertTrue(catalog.getProductCount() > 0, "Quay lai danh muc khong thay san pham");
    }
}
