package vn.dafl.tests;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.pageObjects.AuthModalPageObject;
import vn.dafl.pageObjects.HomePageObject;

@Epic("DAFL Web Application") @Feature("Authentication Modal")
public class AuthModalTest extends BaseTest {
    @Test(description = "TC_UI_05: Verify opening Auth Modal and fields on Login and Register tabs")
    @Severity(SeverityLevel.CRITICAL) @Description("Kiem tra mo Auth Modal va cac truong tren tab Dang nhap / Dang ky")
    public void testAuthModalFields() {
        new HomePageObject(driver).open(baseUrl).openLoginFromMenu();
        AuthModalPageObject auth = new AuthModalPageObject(driver);
        Assert.assertTrue(auth.isAuthModalDisplayed(), "Cua so dang nhap/dang ky khong hien thi");
        Assert.assertTrue(auth.isLoginFieldsDisplayed(), "Truong email/mat khau dang nhap khong hien thi");
        auth.switchToRegisterTab();
        Assert.assertTrue(auth.isRegisterConfirmPasswordDisplayed(), "Truong Nhap lai mat khau khong hien thi tren tab Dang ky");
    }
}
