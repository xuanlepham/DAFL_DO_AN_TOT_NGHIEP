package vn.dafl.tests;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.pageObjects.HomePageObject;

/** Tuong ung tai lieu test case: TC_HOME_04, TC_HOME_05. */
@Epic("DAFL Web Application") @Feature("Search & Category Filter")
public class CatalogFilterTest extends BaseTest {
    @Test(description = "TC_UI_17 (=TC_HOME_04): Search with non-existent keyword shows empty result")
    @Severity(SeverityLevel.NORMAL) @Description("Tim kiem tu khoa khong ton tai phai hien thong bao khong tim thay va 0 san pham")
    public void testSearchNoResult() {
        HomePageObject home = new HomePageObject(driver).open(baseUrl);
        home.search("zzz-khong-ton-tai-123");
        Assert.assertTrue(home.hasNoResultMessage(), "Khong hien thong bao 'Khong tim thay san pham'");
        Assert.assertEquals(home.getProductCardCount(), 0, "Van con san pham hien thi khi tim tu khoa khong ton tai");
    }
    @Test(description = "TC_UI_18 (=TC_HOME_05): Category filter narrows results and 'All' restores them")
    @Severity(SeverityLevel.CRITICAL) @Description("Loc theo danh muc giam danh sach; chon Tat ca khoi phuc day du")
    public void testCategoryFilterAndReset() {
        HomePageObject home = new HomePageObject(driver).open(baseUrl);
        int total = home.getProductCardCount();
        Assert.assertTrue(total > 0, "Trang chu khong co san pham de loc");
        home.clickFirstCategoryPill();
        // Cho danh sach cap nhat: so the phai khac tong ban dau hoac trang thai loc da ap dung
        try { wait.until(d -> new HomePageObject(driver).getProductCardCount() != total); } catch (Exception ignored) {}
        int filtered = home.getProductCardCount();
        Assert.assertTrue(filtered > 0, "Danh muc duoc chon khong co san pham nao");
        Assert.assertTrue(filtered <= total, "So san pham sau loc lon hon truoc loc - bat hop ly");
        home.clickCategoryAll();
        try { wait.until(d -> new HomePageObject(driver).getProductCardCount() == total); } catch (Exception ignored) {}
        Assert.assertEquals(home.getProductCardCount(), total, "Chon 'Tat ca' khong khoi phuc duoc danh sach ban dau");
    }
}
