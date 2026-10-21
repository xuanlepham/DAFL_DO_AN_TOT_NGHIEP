package vn.dafl.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.commons.GlobalConstants;
import vn.dafl.pageObjects.CartDrawerPageObject;
import vn.dafl.pageObjects.CheckoutPageObject;
import vn.dafl.pageObjects.HomePageObject;
import vn.dafl.pageObjects.OrdersPageObject;
import vn.dafl.pageObjects.ProductCatalogPageObject;

/** Kiem thu dat hang: dien form, tao don, thanh toan PayOS (mock), xem lich su don. */
public class CheckoutAndOrderTest extends BaseTest {

    @Test(description = "Dat hang thanh cong -> sinh ma don va man hinh thanh toan")
    public void placeOrderSuccessfully() {
        loginWithDemoAccount();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        catalog.addFirstProductToCart();

        CheckoutPageObject checkout = new CheckoutPageObject();
        checkout.openCheckoutPage();
        Assert.assertTrue(checkout.isCheckoutFormDisplayed(), "Khong hien thi form thanh toan");
        checkout.fillCheckoutForm(GlobalConstants.DEMO_FULL_NAME, GlobalConstants.DEMO_EMAIL,
                GlobalConstants.DEMO_PHONE, GlobalConstants.DEMO_ADDRESS);
        checkout.submitOrder();
        Assert.assertTrue(checkout.isOrderCodeDisplayed() || checkout.isPaymentViewDisplayed()
                        || checkout.isPaymentPageDisplayed(),
                "Dat hang xong khong thay ma don / man hinh thanh toan");
    }

    @Test(description = "Thanh toan PayOS (mock) thanh cong -> don xuat hien trong lich su")
    public void payWithMockPayosAndSeeOrderInHistory() {
        HomePageObject homePage = loginWithDemoAccount();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        catalog.addFirstProductToCart();
        homePage.waitForCartBadgeCount(1);

        CheckoutPageObject checkout = new CheckoutPageObject();
        checkout.openCheckoutPage();
        checkout.fillCheckoutForm(GlobalConstants.DEMO_FULL_NAME, GlobalConstants.DEMO_EMAIL,
                GlobalConstants.DEMO_PHONE, GlobalConstants.DEMO_ADDRESS);
        checkout.submitOrder();

        String orderCode = null;
        if (checkout.isOrderCodeDisplayed()) {
            orderCode = checkout.getOrderCode();
        }
        Assert.assertTrue(checkout.isPaymentPageDisplayed() || checkout.isPaymentViewDisplayed(),
                "Khong vao duoc man hinh thanh toan PayOS");
        if (orderCode == null && checkout.getCurrentPageUrl().contains("/payment/")) {
            orderCode = checkout.getCurrentPageUrl().substring(
                    checkout.getCurrentPageUrl().lastIndexOf('/') + 1);
        }

        checkout.simulatePayosSuccess();
        Assert.assertTrue(checkout.isOrderPaidViewDisplayed(), "Thanh toan xong khong thay trang thai da thanh toan");

        OrdersPageObject ordersPage = homePage.navigateToOrdersPage();
        Assert.assertTrue(ordersPage.isOrdersPageDisplayed(), "Khong mo duoc trang lich su don hang");
        if (orderCode != null && !orderCode.isEmpty()) {
            Assert.assertTrue(ordersPage.isOrderDisplayed(orderCode),
                    "Don hang " + orderCode + " khong xuat hien trong lich su");
        }
    }

    @Test(description = "Chua dang nhap vao trang thanh toan -> hien thong bao yeu cau dang nhap")
    public void checkoutRequiresLogin() {
        HomePageObject homePage = openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        catalog.addFirstProductToCart();
        homePage.waitForCartBadgeCount(1);

        CheckoutPageObject checkout = new CheckoutPageObject();
        checkout.openCheckoutPage();
        Assert.assertTrue(checkout.isAuthRequiredNoticeDisplayed(),
                "Khach chua dang nhap vao /checkout nhung khong thay thong bao yeu cau dang nhap");
    }
}
