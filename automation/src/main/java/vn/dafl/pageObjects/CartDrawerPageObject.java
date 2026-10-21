package vn.dafl.pageObjects;

import vn.dafl.commons.BasePage;
import vn.dafl.pageUIs.CartDrawerPageUI;

/** Page Object cho gio hang: dong san pham, so luong, tong tien va voucher. */
public class CartDrawerPageObject extends BasePage {

    public void waitForDrawerVisible() {
        waitForElementVisible(CartDrawerPageUI.CART_DRAWER);
    }

    public boolean isCartEmpty() {
        return isElementDisplayedWithWait(CartDrawerPageUI.CART_EMPTY_STATE, 5);
    }

    public boolean isItemDisplayed(String productId) {
        return isElementDisplayed(CartDrawerPageUI.DYNAMIC_CART_ITEM, productId);
    }

    public String getItemName(String productId) {
        return getElementText(CartDrawerPageUI.DYNAMIC_CART_ITEM_NAME, productId);
    }

    public int getItemQuantity(String productId) {
        return Integer.parseInt(getElementText(CartDrawerPageUI.DYNAMIC_CART_ITEM_QTY, productId));
    }

    public void increaseQuantity(String productId) {
        int before = getItemQuantity(productId);
        clickToElement(CartDrawerPageUI.DYNAMIC_CART_ITEM_PLUS_BTN, productId);
        waitForItemQuantity(productId, before + 1);
    }

    public void decreaseQuantity(String productId) {
        clickToElement(CartDrawerPageUI.DYNAMIC_CART_ITEM_MINUS_BTN, productId);
    }

    public void waitForItemQuantity(String productId, int expected) {
        long deadline = System.currentTimeMillis() + 15000;
        while (System.currentTimeMillis() < deadline) {
            if (isItemDisplayed(productId) && getItemQuantity(productId) == expected) {
                return;
            }
            sleepInSecond(1);
        }
        throw new AssertionError("So luong san pham " + productId + " khong dat " + expected + " sau 15 giay");
    }

    public String getSubtotalText() {
        return getElementText(CartDrawerPageUI.CART_RAW_SUBTOTAL);
    }

    public String getFinalTotalText() {
        return getElementText(CartDrawerPageUI.CART_FINAL_TOTAL);
    }

    public boolean isVoucherDiscountDisplayed() {
        return isElementDisplayedWithWait(CartDrawerPageUI.CART_VOUCHER_DISCOUNT, 5);
    }

    public void applyVoucher(String voucherCode) {
        waitForElementVisible(CartDrawerPageUI.VOUCHER_CODE_INPUT);
        sendKeyToElement(CartDrawerPageUI.VOUCHER_CODE_INPUT, voucherCode);
        clickToElement(CartDrawerPageUI.VOUCHER_APPLY_BTN);
    }

    public void applyVoucherByChip(String voucherCode) {
        clickToElement(CartDrawerPageUI.DYNAMIC_VOUCHER_CHIP, voucherCode);
    }

    public boolean isVoucherAppliedBannerDisplayed() {
        return isElementDisplayedWithWait(CartDrawerPageUI.APPLIED_VOUCHER_BANNER, 5);
    }

    public String getVoucherErrorMessage() {
        waitForElementVisible(CartDrawerPageUI.VOUCHER_ERROR_MSG);
        return getElementText(CartDrawerPageUI.VOUCHER_ERROR_MSG);
    }

    public boolean isVoucherErrorDisplayed() {
        return isElementDisplayedWithWait(CartDrawerPageUI.VOUCHER_ERROR_MSG, 5);
    }

    public void closeDrawer() {
        if (isElementDisplayed(CartDrawerPageUI.CART_CLOSE_BTN)) {
            clickToElement(CartDrawerPageUI.CART_CLOSE_BTN);
        }
    }
}
