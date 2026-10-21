package vn.dafl.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import vn.dafl.commons.BaseTest;
import vn.dafl.commons.GlobalConstants;
import vn.dafl.pageObjects.CartDrawerPageObject;
import vn.dafl.pageObjects.HomePageObject;
import vn.dafl.pageObjects.ProductCatalogPageObject;

/** Kiem thu gio hang: them san pham, tang/giam so luong, tong tien, voucher. */
public class CartManagementTest extends BaseTest {

    @Test(description = "Them san pham vao gio -> badge tang va dong san pham xuat hien trong gio")
    public void addProductToCart() {
        HomePageObject homePage = openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        String productId = catalog.getFirstProductId();
        String productName = catalog.getProductName(productId);

        catalog.addProductToCart(productId);
        homePage.waitForCartBadgeCount(1);

        CartDrawerPageObject cart = homePage.openCartDrawer();
        Assert.assertTrue(cart.isItemDisplayed(productId), "San pham vua them khong co trong gio");
        Assert.assertEquals(cart.getItemName(productId), productName, "Ten san pham trong gio khong khop");
        Assert.assertEquals(cart.getItemQuantity(productId), 1, "So luong mac dinh phai la 1");
    }

    @Test(description = "Tang so luong -> tong tien tang tuong ung")
    public void increaseQuantityUpdatesTotal() {
        HomePageObject homePage = openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        String productId = catalog.getFirstProductId();
        catalog.addProductToCart(productId);
        homePage.waitForCartBadgeCount(1);

        CartDrawerPageObject cart = homePage.openCartDrawer();
        long unitPrice = parseMoney(cart.getElementText(
                "css=[data-testid='cart-item-price-" + productId + "']"));
        cart.increaseQuantity(productId);
        Assert.assertEquals(cart.getItemQuantity(productId), 2, "So luong sau khi tang phai la 2");
        long subtotal = parseMoney(cart.getSubtotalText());
        Assert.assertEquals(subtotal, unitPrice * 2, "Tam tinh phai bang 2 lan don gia");
    }

    @Test(description = "Giam so luong tu 1 -> san pham bi xoa khoi gio / gio trong")
    public void decreaseQuantityFromOneRemovesItem() {
        HomePageObject homePage = openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        String productId = catalog.getFirstProductId();
        catalog.addProductToCart(productId);
        homePage.waitForCartBadgeCount(1);

        CartDrawerPageObject cart = homePage.openCartDrawer();
        cart.decreaseQuantity(productId);
        cart.sleepInSecond(2);
        Assert.assertTrue(!cart.isItemDisplayed(productId) || cart.isCartEmpty(),
                "Giam so luong tu 1 xuong 0 nhung san pham van con trong gio");
    }

    @Test(description = "Ap voucher DAFL20 (giam thang 20.000d) -> co dong giam gia va tong tien giam")
    public void applyVoucherReducesTotal() {
        HomePageObject homePage = openHomePage();
        ProductCatalogPageObject catalog = new ProductCatalogPageObject();
        catalog.waitForProductsLoaded();
        catalog.addFirstProductToCart();
        homePage.waitForCartBadgeCount(1);

        CartDrawerPageObject cart = homePage.openCartDrawer();
        long subtotal = parseMoney(cart.getSubtotalText());
        cart.applyVoucher(GlobalConstants.VOUCHER_FIXED_20K);
        Assert.assertTrue(cart.isVoucherAppliedBannerDisplayed() || cart.isVoucherDiscountDisplayed(),
                "Ap voucher DAFL20 thanh cong nhung khong thay dau hieu giam gia");
        long finalTotal = parseMoney(cart.getFinalTotalText());
        Assert.assertTrue(finalTotal < subtotal,
                "Tong tien sau voucher (" + finalTotal + ") phai nho hon tam tinh (" + subtotal + ")");
    }
}
