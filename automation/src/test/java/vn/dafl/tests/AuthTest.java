package vn.dafl.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.commons.GlobalConstants;
import vn.dafl.pageObjects.AuthModalPageObject;
import vn.dafl.pageObjects.HomePageObject;

/**
 * Kiem thu xac thuc: dang nhap (nhap tay / nut tai khoan demo), dang xuat, dang ky.
 * Du lieu tai khoan demo va thong diep loi lay tu seed + Backend cua SUT.
 */
public class AuthTest extends BaseTest {

    @Test(description = "Dang nhap thanh cong bang tai khoan demo (nhap email/mat khau)")
    public void loginWithDemoCredentialsSuccessfully() {
        HomePageObject homePage = openHomePage();
        AuthModalPageObject authModal = homePage.openLoginModal();
        authModal.loginAs(GlobalConstants.DEMO_EMAIL, GlobalConstants.DEMO_PASSWORD);
        homePage.waitForLoggedIn();
        Assert.assertTrue(homePage.isUserLoggedIn(), "Dang nhap demo that bai");
    }

    @Test(description = "Dang nhap bang nut 'dung tai khoan demo' tren form")
    public void loginWithDemoAccountButtonSuccessfully() {
        HomePageObject homePage = openHomePage();
        AuthModalPageObject authModal = homePage.openLoginModal();
        authModal.clickDemoAccountButton();
        if (authModal.isModalStillOpen()) {
            authModal.submitLogin();
        }
        homePage.waitForLoggedIn();
        Assert.assertTrue(homePage.isUserLoggedIn(), "Dang nhap bang nut tai khoan demo that bai");
    }

    @Test(description = "Dang nhap sai mat khau -> hien thong diep loi tu Backend")
    public void loginWithWrongPasswordShowsError() {
        HomePageObject homePage = openHomePage();
        AuthModalPageObject authModal = homePage.openLoginModal();
        authModal.loginAs(GlobalConstants.DEMO_EMAIL, "SaiMatKhau@123");
        Assert.assertTrue(authModal.isErrorMessageDisplayed(), "Khong thay thong diep loi dang nhap");
        Assert.assertTrue(authModal.getErrorMessage().contains(GlobalConstants.LOGIN_ERROR_MESSAGE),
                "Thong diep loi khong khop voi Backend: " + authModal.getErrorMessage());
    }

    @Test(description = "Dang xuat thanh cong")
    public void logoutSuccessfully() {
        HomePageObject homePage = loginWithDemoAccount();
        Assert.assertTrue(homePage.isUserLoggedIn(), "Chua dang nhap duoc truoc khi dang xuat");
        homePage.logout();
        Assert.assertFalse(homePage.isUserLoggedIn(), "Van con trang thai dang nhap sau khi dang xuat");
    }

    @Test(description = "Dang ky tai khoan moi thanh cong va dang nhap lai duoc")
    public void registerNewAccountThenLogin() {
        String email = randomEmail();
        String password = "Test@1234";
        HomePageObject homePage = openHomePage();
        AuthModalPageObject authModal = homePage.openLoginModal();
        authModal.register("Người Dùng Kiểm Thử", email, "0912345678",
                "Số 2 Đại Cồ Việt, Hà Nội", password, password);
        Assert.assertFalse(authModal.isErrorMessageDisplayed(),
                "Dang ky bao loi: " + (authModal.isModalStillOpen() ? authModal.getErrorMessage() : ""));

        // Dang nhap lai bang tai khoan vua tao de chung minh dang ky da luu vao he thong
        if (!homePage.isUserLoggedIn()) {
            if (authModal.isModalStillOpen()) {
                authModal.closeModal();
            }
            authModal = homePage.openLoginModal();
            authModal.loginAs(email, password);
        }
        homePage.waitForLoggedIn();
        Assert.assertTrue(homePage.isUserLoggedIn(), "Khong dang nhap lai duoc bang tai khoan vua dang ky");
    }

    @Test(description = "AUTH-06: chuyen qua lai giua tab Dang nhap / Dang ky, form va tieu de modal thay doi theo tab")
    public void switchBetweenLoginAndRegisterTabs() {
        HomePageObject homePage = openHomePage();
        AuthModalPageObject authModal = homePage.openLoginModal();
        Assert.assertTrue(authModal.isLoginFormDisplayed(), "Mo modal nhung khong thay form dang nhap");
        Assert.assertEquals(authModal.getModalTitle(), "Đăng nhập tài khoản",
                "Tieu de modal khi o tab dang nhap khong dung voi Frontend");
        authModal.switchToRegisterTab();
        Assert.assertTrue(authModal.isRegisterFormDisplayed(), "Chuyen sang tab dang ky nhung khong thay form dang ky");
        Assert.assertEquals(authModal.getModalTitle(), "Đăng ký thành viên mới",
                "Tieu de modal khi o tab dang ky khong dung voi Frontend");
        authModal.switchToLoginTab();
        Assert.assertTrue(authModal.isLoginFormDisplayed(), "Chuyen lai tab dang nhap nhung khong thay form dang nhap");
    }

    @Test(description = "AUTH-07: dong modal bang nut X, chua dang nhap, mo lai modal duoc")
    public void closeModalAndReopen() {
        HomePageObject homePage = openHomePage();
        AuthModalPageObject authModal = homePage.openLoginModal();
        Assert.assertTrue(authModal.isModalStillOpen(), "Modal chua mo");
        authModal.closeModal();
        Assert.assertFalse(authModal.isModalStillOpen(), "Da bam nut X nhung modal van con mo");
        Assert.assertFalse(homePage.isUserLoggedIn(), "Chi dong modal ma he thong da ghi nhan dang nhap");
        AuthModalPageObject reopenedModal = homePage.openLoginModal();
        Assert.assertTrue(reopenedModal.isLoginFormDisplayed(), "Mo lai modal nhung khong thay form dang nhap");
    }

    @Test(description = "AUTH-08: nut hien/an mat khau tren form dang nhap chuyen type cua o mat khau password <-> text")
    public void toggleLoginPasswordVisibility() {
        AuthModalPageObject authModal = openHomePage().openLoginModal();
        authModal.enterLoginPassword("Test@1234");
        Assert.assertEquals(authModal.getLoginPasswordInputType(), "password",
                "Mac dinh o mat khau phai o che do an (type=password)");
        authModal.toggleLoginPasswordVisibility();
        Assert.assertEquals(authModal.getLoginPasswordInputType(), "text",
                "Bam nut hien mat khau nhung o mat khau chua chuyen sang type=text");
        authModal.toggleLoginPasswordVisibility();
        Assert.assertEquals(authModal.getLoginPasswordInputType(), "password",
                "Bam nut an mat khau nhung o mat khau chua quay ve type=password");
    }

    @Test(description = "AUTH-09: dang nhap sai mat khau bao loi, nhap lai dung ngay trong cung phien modal thi thanh cong")
    public void loginFailsThenSucceedsInSameModalSession() {
        HomePageObject homePage = openHomePage();
        AuthModalPageObject authModal = homePage.openLoginModal();
        authModal.loginAs(GlobalConstants.DEMO_EMAIL, "SaiMatKhau@123");
        Assert.assertTrue(authModal.isErrorMessageDisplayed(), "Dang nhap sai nhung khong thay thong diep loi");
        authModal.loginAs(GlobalConstants.DEMO_EMAIL, GlobalConstants.DEMO_PASSWORD);
        homePage.waitForLoggedIn();
        Assert.assertTrue(homePage.isUserLoggedIn(),
                "Nhap lai dung mat khau trong cung phien modal nhung van khong dang nhap duoc");
    }

    @Test(description = "AUTH-10: dang ky bang email sai dinh dang -> form chan lai, modal van mo, khong tao tai khoan")
    public void registerWithInvalidEmailFormat() {
        AuthModalPageObject authModal = openHomePage().openLoginModal();
        authModal.register("Test Email", "emailsaidinhdang@", "0912345678",
                "Ha Noi", "Test@1234", "Test@1234");
        Assert.assertTrue(authModal.isModalStillOpen(), "Email sai dinh dang ma van dang ky thanh cong");
    }

    @Test(description = "AUTH-11: dang ky de trong cac truong bat buoc -> khong tao tai khoan, modal van mo")
    public void registerWithEmptyRequiredFields() {
        AuthModalPageObject authModal = openHomePage().openLoginModal();
        authModal.register("", "", "", "", "", "");
        Assert.assertTrue(authModal.isModalStillOpen(), "De trong cac truong bat buoc ma van dang ky thanh cong");
    }
}
