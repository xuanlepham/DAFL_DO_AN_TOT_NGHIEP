package vn.dafl.tests;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.pageObjects.CartPageObject;
import vn.dafl.pageObjects.HomePageObject;

/** Tuong ung tai lieu test case: TC_CART_03, TC_CART_04, TC_CART_05. Ma voucher lay tu du lieu seed cua BE. */
@Epic("DAFL Web Application") @Feature("Voucher")
public class VoucherTest extends BaseTest {
    private CartPageObject cartWithOneProduct() {
        new HomePageObject(driver).open(baseUrl).clickFirstAddToCart();
        CartPageObject cart = new CartPageObject(driver);
        Assert.assertTrue(cart.isCartDrawerDisplayed(), "Gio hang khong mo sau khi them san pham");
        return cart;
    }
    @Test(description = "TC_UI_19 (=TC_CART_03): Apply percentage voucher DAFL10 shows discount")
    @Severity(SeverityLevel.CRITICAL) @Description("Ap ma DAFL10 (giam 10%) phai hien thi dong giam gia trong gio")
    public void testApplyPercentageVoucher() {
        CartPageObject cart = cartWithOneProduct();
        cart.applyVoucher("DAFL10");
        Assert.assertTrue(cart.isVoucherDiscountDisplayed() || cart.isAppliedVoucherBannerDisplayed(),
                "Ap ma DAFL10 thanh cong nhung khong hien thi giam gia/banner xac nhan");
    }
    @Test(description = "TC_UI_20 (=TC_CART_05): Apply non-existent voucher shows error")
    @Severity(SeverityLevel.NORMAL) @Description("Ap ma khong ton tai phai bao loi va khong giam gia")
    public void testApplyInvalidVoucher() {
        CartPageObject cart = cartWithOneProduct();
        cart.applyVoucher("KHONGTONTAI");
        Assert.assertTrue(cart.isVoucherErrorDisplayed(), "Khong hien thi loi khi ap ma voucher sai");
        Assert.assertFalse(cart.getVoucherErrorText().isBlank(), "Thong bao loi voucher dang rong");
    }
    @Test(description = "TC_UI_21 (=TC_CART_04): Apply fixed voucher DAFL20 shows discount")
    @Severity(SeverityLevel.NORMAL) @Description("Ap ma DAFL20 (giam co dinh 20.000d) phai hien thi giam gia")
    public void testApplyFixedVoucher() {
        CartPageObject cart = cartWithOneProduct();
        cart.applyVoucher("DAFL20");
        Assert.assertTrue(cart.isVoucherDiscountDisplayed() || cart.isAppliedVoucherBannerDisplayed(),
                "Ap ma DAFL20 thanh cong nhung khong hien thi giam gia/banner xac nhan");
    }
}
