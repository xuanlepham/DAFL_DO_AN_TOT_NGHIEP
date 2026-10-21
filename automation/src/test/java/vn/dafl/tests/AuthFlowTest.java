package vn.dafl.tests;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.commons.GlobalConstants;
import vn.dafl.pageObjects.AuthModalPageObject;
import vn.dafl.pageObjects.HomePageObject;

/** Test moi bo sung theo nhu cau bao phu Auth flow that (ban cu chi kiem tra hien thi form). */
@Epic("DAFL Web Application") @Feature("Authentication Flow")
public class AuthFlowTest extends BaseTest {
    @Test(description = "TC_UI_07: Login succeeds with seeded demo account")
    @Severity(SeverityLevel.BLOCKER) @Description("Dang nhap thanh cong bang tai khoan demo duoc seed san trong Backend")
    public void testLoginWithDemoAccountSucceeds() {
        HomePageObject home = new HomePageObject(driver).open(baseUrl);
        home.openLoginFromMenu();
        AuthModalPageObject auth = new AuthModalPageObject(driver);
        auth.loginWithDemoAccount();
        Assert.assertTrue(auth.isModalClosed(), "Auth Modal khong dong sau khi dang nhap thanh cong");
        home.openAccountMenu();
        Assert.assertTrue(home.isLogoutDisplayed(), "Khong thay nut Dang xuat sau khi dang nhap");
    }
    @Test(description = "TC_UI_08: Login fails with wrong password and shows error message")
    @Severity(SeverityLevel.CRITICAL) @Description("Dang nhap sai mat khau phai hien thi thong bao loi, khong dang nhap duoc")
    public void testLoginWithWrongPasswordFails() {
        new HomePageObject(driver).open(baseUrl).openLoginFromMenu();
        AuthModalPageObject auth = new AuthModalPageObject(driver);
        auth.login("tester@dafl.vn", "WrongPassword!2026");
        Assert.assertTrue(auth.isErrorDisplayed(), "Khong hien thi thong bao loi khi dang nhap sai");
        Assert.assertFalse(auth.getErrorMessage().isBlank(), "Thong bao loi dang nhap dang rong");
        Assert.assertTrue(auth.isAuthModalDisplayed(), "Auth Modal phai con mo sau khi dang nhap that bai");
    }
    @Test(description = "TC_UI_15 (=TC_AUTH_03): Login succeeds with manually typed demo credentials")
    @Severity(SeverityLevel.CRITICAL) @Description("Nhap tay dung email/mat khau tai khoan demo phai dang nhap thanh cong")
    public void testLoginWithTypedCredentials() {
        HomePageObject home = new HomePageObject(driver).open(baseUrl);
        home.openLoginFromMenu();
        AuthModalPageObject auth = new AuthModalPageObject(driver);
        auth.login(GlobalConstants.DEMO_EMAIL, GlobalConstants.getDemoPassword());
        Assert.assertTrue(auth.isModalClosed(), "Auth Modal khong dong sau khi nhap tay dang nhap dung");
        home.openAccountMenu();
        Assert.assertTrue(home.isLogoutDisplayed(), "Khong thay nut Dang xuat sau dang nhap nhap tay");
    }
    @Test(description = "TC_UI_16 (=TC_AUTH_09): Logout returns to guest state")
    @Severity(SeverityLevel.NORMAL) @Description("Dang xuat xong phai quay ve trang thai khach (hien lai nut Dang nhap)")
    public void testLogout() {
        HomePageObject home = new HomePageObject(driver).open(baseUrl);
        home.openLoginFromMenu();
        new AuthModalPageObject(driver).loginWithDemoAccount();
        home.logout();
        home.openAccountMenu();
        Assert.assertTrue(home.isLoginEntryDisplayed(), "Sau dang xuat khong thay lai nut Dang nhap");
    }
    @Test(description = "TC_UI_09: Login with demo email but wrong password does not authenticate via API path")
    @Severity(SeverityLevel.NORMAL) @Description("Email demo nhung sai mat khau phai bi Backend tu choi (401) va hien thi loi")
    public void testDemoEmailWrongPassword() {
        new HomePageObject(driver).open(baseUrl).openLoginFromMenu();
        AuthModalPageObject auth = new AuthModalPageObject(driver);
        auth.login(GlobalConstants.DEMO_EMAIL, "SaiMatKhau!2026");
        // Backend dang chay se tra 401 (json.success=false) -> FE hien loi qua auth-error-msg.
        // Fallback demo trong FE chi kich hoat o nhanh catch (mat ket noi), khong ap dung o day.
        Assert.assertTrue(auth.isErrorDisplayed(), "Khong hien loi khi email demo nhung sai mat khau");
        Assert.assertTrue(auth.isAuthModalDisplayed(), "Auth Modal phai con mo sau dang nhap that bai");
    }
}
