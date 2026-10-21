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
}
