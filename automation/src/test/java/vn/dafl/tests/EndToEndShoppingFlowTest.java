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

/**
 * Luong mua hang dau-cuoi (E2E) tren cung mot phien trinh duyet:
 * dang nhap -> duyet san pham -> them 2 san pham -> ap voucher -> thanh toan
 * -> tra tien PayOS (mock) -> kiem tra don trong lich su.
 */
public class EndToEndShoppingFlowTest extends BaseTest {

    @Test(description = "E2E: dang nhap -> mua 2 san pham -> voucher -> dat hang -> thanh toan -> thay don")
    public void endToEndShoppingFlow() {
        // Buoc 1: dang nhap tai khoan demo
        HomePageObject homePage = loginWithDemoAccount();
        Assert.assertTrue(homePage.isUserLoggedIn(), "Buoc 1: dang nhap that bai");

        // Buoc 2: duyet danh muc va chon 2 san pham dau tien
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        Assert.assertTrue(catalog.getProductCount() >= 2, "Buoc 2: can it nhat 2 san pham de chay luong E2E");
        String firstId = catalog.getProductIdAt(0);
        String secondId = catalog.getProductIdAt(1);

        // Buoc 3: them ca 2 san pham vao gio
        catalog.addProductToCart(firstId);
        homePage.waitForCartBadgeCount(1);
        catalog.addProductToCart(secondId);
        homePage.waitForCartBadgeCount(2);

        // Buoc 4: mo gio, kiem tra 2 dong san pham va ap voucher DAFL20
        CartDrawerPageObject cart = homePage.openCartDrawer();
        Assert.assertTrue(cart.isItemDisplayed(firstId), "Buoc 4: thieu san pham 1 trong gio");
        Assert.assertTrue(cart.isItemDisplayed(secondId), "Buoc 4: thieu san pham 2 trong gio");
        long subtotal = parseMoney(cart.getSubtotalText());
        Assert.assertTrue(subtotal > 0, "Buoc 4: tam tinh phai lon hon 0");
        cart.applyVoucher(GlobalConstants.VOUCHER_FIXED_20K);
        Assert.assertTrue(cart.isVoucherAppliedBannerDisplayed() || cart.isVoucherDiscountDisplayed(),
                "Buoc 4: ap voucher DAFL20 khong thay dau hieu giam gia");
        cart.closeDrawer();

        // Buoc 5: vao trang thanh toan, dien thong tin va dat hang
        CheckoutPageObject checkout = new CheckoutPageObject();
        checkout.openCheckoutPage();
        Assert.assertTrue(checkout.isCheckoutFormDisplayed(), "Buoc 5: khong thay form thanh toan");
        checkout.fillCheckoutForm(GlobalConstants.DEMO_FULL_NAME, GlobalConstants.DEMO_EMAIL,
                GlobalConstants.DEMO_PHONE, GlobalConstants.DEMO_ADDRESS);
        checkout.submitOrder();

        // Buoc 6: thanh toan qua cong PayOS (mock) bang nut gia lap thanh cong
        String orderCode = null;
        if (checkout.isOrderCodeDisplayed()) {
            orderCode = checkout.getOrderCode();
        }
        Assert.assertTrue(checkout.isPaymentPageDisplayed() || checkout.isPaymentViewDisplayed(),
                "Buoc 6: khong vao duoc man hinh thanh toan");
        if (orderCode == null && checkout.getCurrentPageUrl().contains("/payment/")) {
            orderCode = checkout.getCurrentPageUrl().substring(
                    checkout.getCurrentPageUrl().lastIndexOf('/') + 1);
        }
        checkout.simulatePayosSuccess();
        Assert.assertTrue(checkout.isOrderPaidViewDisplayed(), "Buoc 6: thanh toan khong thanh cong");

        // Buoc 7: kiem tra don hang vua tao trong lich su don hang
        OrdersPageObject ordersPage = homePage.navigateToOrdersPage();
        Assert.assertTrue(ordersPage.isOrdersPageDisplayed(), "Buoc 7: khong mo duoc trang lich su don");
        if (orderCode != null && !orderCode.isEmpty()) {
            Assert.assertTrue(ordersPage.isOrderDisplayed(orderCode),
                    "Buoc 7: khong thay don " + orderCode + " trong lich su");
        }
    }
}
