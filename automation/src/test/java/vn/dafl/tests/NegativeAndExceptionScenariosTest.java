package vn.dafl.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.commons.GlobalConstants;
import vn.dafl.pageObjects.AuthModalPageObject;
import vn.dafl.pageObjects.CartDrawerPageObject;
import vn.dafl.pageObjects.CheckoutPageObject;
import vn.dafl.pageObjects.HomePageObject;
import vn.dafl.pageObjects.ProductCatalogPageObject;

/**
 * Kiem thu am tinh va ngoai le: du lieu sai/thieu, voucher sai, tim kiem rong,
 * thanh toan thieu thong tin. Ky vong chung: he thong chan thao tac va bao loi,
 * khong tao du lieu rac (don hang/don thanh toan) tu du lieu khong hop le.
 * Chinh sach mat khau cua SUT: it nhat 8 ky tu, co chu hoa va ky tu dac biet.
 */
public class NegativeAndExceptionScenariosTest extends BaseTest {

    // ---------- Nhom dang nhap ----------
    @Test(description = "NEG-AUTH-01: dang nhap bang email chua dang ky")
    public void loginWithUnregisteredEmail() {
        HomePageObject homePage = openHomePage();
        AuthModalPageObject authModal = homePage.openLoginModal();
        authModal.loginAs("chuatungdangky@dafl.vn", "Test@1234");
        Assert.assertTrue(authModal.isErrorMessageDisplayed(), "Dang nhap email ao nhung khong bao loi");
        Assert.assertTrue(authModal.getErrorMessage().contains(GlobalConstants.LOGIN_ERROR_MESSAGE),
                "Thong diep loi khong khop voi Backend");
    }

    @Test(description = "NEG-AUTH-02: dang nhap de trong email va mat khau")
    public void loginWithEmptyFields() {
        HomePageObject homePage = openHomePage();
        AuthModalPageObject authModal = homePage.openLoginModal();
        authModal.loginAs("", "");
        Assert.assertTrue(authModal.isModalStillOpen(), "De trong ca hai truong ma van dang nhap duoc");
        Assert.assertFalse(homePage.isUserLoggedIn(), "De trong thong tin nhung trang thai da dang nhap");
    }

    @Test(description = "NEG-AUTH-03: dang nhap bang email sai dinh dang")
    public void loginWithInvalidEmailFormat() {
        HomePageObject homePage = openHomePage();
        AuthModalPageObject authModal = homePage.openLoginModal();
        authModal.loginAs("emailsaidinhdang@", "Test@1234");
        Assert.assertTrue(authModal.isModalStillOpen(), "Email sai dinh dang ma van qua duoc form dang nhap");
        Assert.assertFalse(homePage.isUserLoggedIn(), "Email sai dinh dang nhung trang thai da dang nhap");
    }

    // ---------- Nhom dang ky ----------
    @Test(description = "NEG-REG-01: mat khau dang ky ngan hon 8 ky tu")
    public void registerWithTooShortPassword() {
        AuthModalPageObject authModal = openHomePage().openLoginModal();
        authModal.register("Test Ngan", randomEmail(), "0912345678", "Ha Noi", "Ab@1", "Ab@1");
        Assert.assertTrue(authModal.isModalStillOpen(),
                "Mat khau 4 ky tu van dang ky thanh cong (SUT yeu cau >= 8 ky tu)");
    }

    @Test(description = "NEG-REG-02: mat khau dang ky khong co chu hoa")
    public void registerWithPasswordMissingUppercase() {
        AuthModalPageObject authModal = openHomePage().openLoginModal();
        authModal.register("Test Hoa", randomEmail(), "0912345678", "Ha Noi", "test@1234", "test@1234");
        Assert.assertTrue(authModal.isModalStillOpen(),
                "Mat khau khong co chu hoa van dang ky thanh cong");
    }

    @Test(description = "NEG-REG-03: mat khau dang ky khong co ky tu dac biet")
    public void registerWithPasswordMissingSpecialChar() {
        AuthModalPageObject authModal = openHomePage().openLoginModal();
        authModal.register("Test Dac Biet", randomEmail(), "0912345678", "Ha Noi", "Test1234", "Test1234");
        Assert.assertTrue(authModal.isModalStillOpen(),
                "Mat khau khong co ky tu dac biet van dang ky thanh cong");
    }

    @Test(description = "NEG-REG-04: mat khau xac nhan khong khop")
    public void registerWithMismatchedConfirmPassword() {
        AuthModalPageObject authModal = openHomePage().openLoginModal();
        authModal.register("Test Khop", randomEmail(), "0912345678", "Ha Noi", "Test@1234", "Test@5678");
        Assert.assertTrue(authModal.isModalStillOpen(),
                "Mat khau xac nhan khac nhau van dang ky thanh cong");
    }

    @Test(description = "NEG-REG-05: dang ky bang email da ton tai (tai khoan demo)")
    public void registerWithExistingEmail() {
        AuthModalPageObject authModal = openHomePage().openLoginModal();
        authModal.register("Trung Email", GlobalConstants.DEMO_EMAIL, "0912345678",
                "Ha Noi", "Test@1234", "Test@1234");
        Assert.assertTrue(authModal.isModalStillOpen() || authModal.isErrorMessageDisplayed(),
                "Dang ky trung email cua tai khoan demo van thanh cong");
    }

    // ---------- Nhom voucher ----------
    @Test(description = "NEG-VCH-01: ap ma giam gia khong ton tai")
    public void applyInvalidVoucherCode() {
        HomePageObject homePage = openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        catalog.addFirstProductToCart();
        homePage.waitForCartBadgeCount(1);

        CartDrawerPageObject cart = homePage.openCartDrawer();
        cart.applyVoucher("INVALID123");
        Assert.assertTrue(cart.isVoucherErrorDisplayed(), "Ap ma sai nhung khong thay thong diep loi voucher");
        Assert.assertTrue(cart.getVoucherErrorMessage().contains(GlobalConstants.VOUCHER_ERROR_MESSAGE),
                "Thong diep loi voucher khong khop voi Backend: " + cart.getVoucherErrorMessage());
    }

    @Test(description = "NEG-VCH-02: ap ma giam gia rong")
    public void applyEmptyVoucherCode() {
        HomePageObject homePage = openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        catalog.addFirstProductToCart();
        homePage.waitForCartBadgeCount(1);

        CartDrawerPageObject cart = homePage.openCartDrawer();
        cart.applyVoucher("");
        cart.sleepInSecond(1);
        Assert.assertFalse(cart.isVoucherAppliedBannerDisplayed(),
                "Ap ma rong ma he thong van ghi nhan voucher");
    }

    // ---------- Nhom tim kiem ----------
    @Test(description = "NEG-SRCH-01: tim kiem san pham khong ton tai")
    public void searchForNonExistentProduct() {
        HomePageObject homePage = openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        homePage.enterSearchKeyword("sanphamkhonghecosan987");
        catalog.sleepInSecond(2);
        Assert.assertEquals(catalog.getProductCount(), 0,
                "Tim san pham khong ton tai nhung van co ket qua");
    }

    // ---------- Nhom thanh toan ----------
    @Test(description = "NEG-CHK-01: thanh toan voi so dien thoai khong hop le")
    public void checkoutWithInvalidPhone() {
        loginWithDemoAccount();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        catalog.addFirstProductToCart();

        CheckoutPageObject checkout = new CheckoutPageObject();
        checkout.openCheckoutPage();
        checkout.fillCheckoutForm(GlobalConstants.DEMO_FULL_NAME, GlobalConstants.DEMO_EMAIL,
                "123", GlobalConstants.DEMO_ADDRESS);
        checkout.submitOrder();
        checkout.sleepInSecond(2);
        Assert.assertFalse(checkout.isPaymentPageDisplayed() || checkout.isOrderCodeDisplayed(),
                "So dien thoai '123' van dat hang duoc");
    }

    @Test(description = "NEG-CHK-02: thanh toan de trong dia chi giao hang")
    public void checkoutWithEmptyAddress() {
        loginWithDemoAccount();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        catalog.addFirstProductToCart();

        CheckoutPageObject checkout = new CheckoutPageObject();
        checkout.openCheckoutPage();
        checkout.fillCheckoutForm(GlobalConstants.DEMO_FULL_NAME, GlobalConstants.DEMO_EMAIL,
                GlobalConstants.DEMO_PHONE, "");
        checkout.submitOrder();
        checkout.sleepInSecond(2);
        Assert.assertFalse(checkout.isPaymentPageDisplayed() || checkout.isOrderCodeDisplayed(),
                "De trong dia chi van dat hang duoc");
    }

    @Test(description = "NEG-CHK-03: vao trang thanh toan khi gio dang trong (da dang nhap)")
    public void checkoutWithEmptyCart() {
        loginWithDemoAccount();
        CheckoutPageObject checkout = new CheckoutPageObject();
        checkout.openCheckoutPage();
        if (checkout.isCheckoutFormDisplayed()) {
            checkout.fillCheckoutForm(GlobalConstants.DEMO_FULL_NAME, GlobalConstants.DEMO_EMAIL,
                    GlobalConstants.DEMO_PHONE, GlobalConstants.DEMO_ADDRESS);
            checkout.submitOrder();
            checkout.sleepInSecond(2);
        }
        Assert.assertFalse(checkout.isPaymentPageDisplayed() || checkout.isOrderCodeDisplayed(),
                "Gio trong van tao duoc don hang/thanh toan");
    }
}
