package vn.dafl.tests;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.pageObjects.AuthModalPageObject;
import vn.dafl.pageObjects.CartPageObject;
import vn.dafl.pageObjects.HomePageObject;

/** Tuong ung tai lieu test case: TC_CO_03. */
@Epic("DAFL Web Application") @Feature("Checkout Validation")
public class CheckoutValidationTest extends BaseTest {
    @Test(description = "TC_UI_22 (=TC_CO_03): Checkout with empty required name must not create an order")
    @Severity(SeverityLevel.CRITICAL) @Description("De trong truong bat buoc (Ho ten) thi khong duoc tao don/khong sang man hinh thanh toan")
    public void testCheckoutWithEmptyRequiredField() {
        HomePageObject home = new HomePageObject(driver).open(baseUrl);
        home.clickFirstAddToCart();
        CartPageObject cart = new CartPageObject(driver);
        Assert.assertTrue(cart.isCartDrawerDisplayed(), "Gio hang khong mo");
        // Dang nhap demo de loai tru yeu to Auth Guard khoi test validation
        cart.fillCheckoutForm("Nguyễn Văn Test", "tester@dafl.vn", "0987654321", "123 Đường Test, Quận 1, TP.HCM");
        cart.submitCheckout();
        AuthModalPageObject auth = new AuthModalPageObject(driver);
        Assert.assertTrue(auth.isAuthModalDisplayed(), "Auth Guard khong mo Auth Modal");
        auth.loginWithDemoAccount();
        Assert.assertTrue(cart.isCartDrawerDisplayed(), "Khong quay lai gio hang sau dang nhap");
        // Xoa trang truong Ho ten (bat buoc) roi submit lai
        cart.clearCheckoutName();
        cart.submitCheckout();
        Assert.assertFalse(cart.isPaymentViewDisplayed(), "Van tao don/sang man hinh thanh toan du thieu truong bat buoc");
    }
}
