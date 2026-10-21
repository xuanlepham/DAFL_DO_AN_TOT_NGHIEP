package vn.dafl.tests;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.pageObjects.AuthModalPageObject;
import vn.dafl.pageObjects.CartPageObject;
import vn.dafl.pageObjects.CheckoutPageObject;
import vn.dafl.pageObjects.HomePageObject;

@Epic("DAFL Web Application") @Feature("End-to-End Shopping & Checkout Flow")
public class CheckoutE2ETest extends BaseTest {
    @Test(description = "TC_UI_06: Full E2E Flow - Search, Add to Cart, Fake PayOS Checkout, Confirm Success and Back to Home")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Kiem thu toan trinh E2E: Tim kiem, them gio hang, dien form, Auth Guard, dang nhap demo, gia lap PayOS thanh cong, quay ve trang chu")
    public void testFullCheckoutE2EFlow() {
        // Buoc 1-3: Trang chu -> tim kiem -> them san pham dau tien vao gio
        HomePageObject home = new HomePageObject(driver).open(baseUrl);
        home.search("Áo").clickFirstAddToCart();

        // Buoc 4: Gio hang tu mo -> dien thong tin giao hang
        CartPageObject cart = new CartPageObject(driver);
        Assert.assertTrue(cart.isCartDrawerDisplayed(), "Ngan gio hang khong tu dong mo sau khi them san pham");
        cart.fillCheckoutForm("Nguyễn Văn Test", "tester@dafl.vn", "0987654321",
                "123 Đường Test E2E, Phường Bến Nghé, Quận 1, TP.HCM");

        // Buoc 4.1: Thanh toan khi chua dang nhap -> Auth Guard mo Auth Modal
        Assert.assertTrue(cart.isCheckoutSubmitEnabled(), "Nut thanh toan bi vo hieu hoa");
        cart.submitCheckout();
        AuthModalPageObject auth = new AuthModalPageObject(driver);
        Assert.assertTrue(auth.isAuthModalDisplayed(), "Khong dieu huong ve man hinh Dang nhap khi thanh toan khong co Auth");

        // Dang nhap demo -> quay lai gio hang -> thanh toan
        auth.loginWithDemoAccount();
        Assert.assertTrue(cart.isCartDrawerDisplayed(), "Khong quay lai gio hang sau khi dang nhap thanh cong");
        cart.submitCheckout();

        // Buoc 5-7: Man hinh PayOS -> gia lap webhook thanh cong -> man hinh da thanh toan
        CheckoutPageObject checkout = new CheckoutPageObject(driver);
        Assert.assertTrue(checkout.isPaymentViewDisplayed(), "Giao dien thanh toan PayOS khong hien thi");
        long orderCode = checkout.getOrderCode();
        Assert.assertTrue(orderCode > 0, "Khong nhan dien duoc ma don hang hop le");
        long amount = checkout.getTotalAmount();
        checkout.simulatePayOSWebhookSuccess(orderCode, amount);
        checkout.clickSimulateSuccessIfPresent();
        Assert.assertTrue(checkout.isOrderPaidDisplayed(), "Man hinh thong bao thanh toan thanh cong khong hien thi");
        Assert.assertTrue(checkout.getOrderPaidText().contains("Thanh toán thành công"), "Noi dung xac nhan khong chinh xac");

        // Buoc 8: Tiep tuc mua sam -> ve trang chu
        checkout.clickContinueShopping();
        Assert.assertTrue(home.isBrandDisplayed(), "Khong quay lai duoc trang chu sau khi hoan tat don hang");
    }
}
