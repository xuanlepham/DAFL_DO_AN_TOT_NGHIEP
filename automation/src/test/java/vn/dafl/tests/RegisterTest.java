package vn.dafl.tests;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.pageObjects.AuthModalPageObject;
import vn.dafl.pageObjects.HomePageObject;

/** Tuong ung tai lieu test case: TC_AUTH_06, TC_AUTH_07, TC_AUTH_08. */
@Epic("DAFL Web Application") @Feature("Registration")
public class RegisterTest extends BaseTest {
    private AuthModalPageObject openRegister() {
        new HomePageObject(driver).open(baseUrl).openLoginFromMenu();
        return new AuthModalPageObject(driver);
    }
    private String uniqueEmail() { return "auto" + System.currentTimeMillis() + "@dafl.vn"; }

    @Test(description = "TC_UI_10 (=TC_AUTH_06): Register succeeds with valid data and unique email")
    @Severity(SeverityLevel.CRITICAL) @Description("Dang ky tai khoan moi hop le thi tao tai khoan va dang nhap thanh cong")
    public void testRegisterSuccess() {
        AuthModalPageObject auth = openRegister();
        auth.register("Nguyen Van Auto", uniqueEmail(), "0987654321", "123 Duong Test, Quan 1, TP.HCM", "Abc@1234", "Abc@1234");
        Assert.assertTrue(auth.isModalClosed(), "Auth Modal khong dong sau khi dang ky thanh cong");
        HomePageObject home = new HomePageObject(driver);
        home.openAccountMenu();
        Assert.assertTrue(home.isLogoutDisplayed(), "Khong thay trang thai da dang nhap sau dang ky");
    }
    @Test(description = "TC_UI_11 (=TC_AUTH_07a): Register rejects password shorter than 8 characters")
    @Severity(SeverityLevel.NORMAL) @Description("Mat khau 7 ky tu phai bi tu choi voi thong bao dung quy tac")
    public void testRegisterPasswordTooShort() {
        AuthModalPageObject auth = openRegister();
        auth.register("Nguyen Van Auto", uniqueEmail(), "0987654321", "Dia chi test", "Abc@123", "Abc@123");
        Assert.assertEquals(auth.getErrorMessage(), "Mật khẩu phải có tối thiểu 8 ký tự.");
    }
    @Test(description = "TC_UI_12 (=TC_AUTH_07b): Register rejects password without uppercase letter")
    @Severity(SeverityLevel.NORMAL) @Description("Mat khau khong co chu hoa phai bi tu choi")
    public void testRegisterPasswordNoUppercase() {
        AuthModalPageObject auth = openRegister();
        auth.register("Nguyen Van Auto", uniqueEmail(), "0987654321", "Dia chi test", "abc@1234", "abc@1234");
        Assert.assertEquals(auth.getErrorMessage(), "Mật khẩu phải chứa ít nhất 1 ký tự chữ hoa (A-Z).");
    }
    @Test(description = "TC_UI_13 (=TC_AUTH_07c): Register rejects password without special character")
    @Severity(SeverityLevel.NORMAL) @Description("Mat khau khong co ky tu dac biet phai bi tu choi")
    public void testRegisterPasswordNoSpecialChar() {
        AuthModalPageObject auth = openRegister();
        auth.register("Nguyen Van Auto", uniqueEmail(), "0987654321", "Dia chi test", "Abc12345", "Abc12345");
        Assert.assertEquals(auth.getErrorMessage(), "Mật khẩu phải chứa ít nhất 1 ký tự đặc biệt (!@#$%^&*...).");
    }
    @Test(description = "TC_UI_14 (=TC_AUTH_08): Register rejects mismatched password confirmation")
    @Severity(SeverityLevel.NORMAL) @Description("Nhap lai mat khau khong khop phai bi tu choi")
    public void testRegisterConfirmMismatch() {
        AuthModalPageObject auth = openRegister();
        auth.register("Nguyen Van Auto", uniqueEmail(), "0987654321", "Dia chi test", "Abc@1234", "Abc@9999");
        Assert.assertEquals(auth.getErrorMessage(), "Mật khẩu xác nhận không khớp. Vui lòng kiểm tra lại.");
        Assert.assertTrue(auth.isAuthModalDisplayed(), "Modal phai con mo sau khi dang ky that bai");
    }
}
