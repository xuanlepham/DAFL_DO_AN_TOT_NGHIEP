package vn.dafl.pageObjects;

import vn.dafl.commons.BasePage;
import vn.dafl.commons.GlobalConstants;
import vn.dafl.pageUIs.CheckoutPageUI;

/** Page Object cho trang /checkout va trang thanh toan /payment/:orderCode. */
public class CheckoutPageObject extends BasePage {

    public void openCheckoutPage() {
        openPageUrl(GlobalConstants.getAppUrl() + "/checkout");
    }

    public boolean isCheckoutFormDisplayed() {
        return isElementDisplayedWithWait(CheckoutPageUI.CHECKOUT_FORM, 10);
    }

    public boolean isAuthRequiredNoticeDisplayed() {
        return isElementDisplayedWithWait(CheckoutPageUI.CHECKOUT_AUTH_REQUIRED_NOTICE, 10);
    }

    public void fillCheckoutForm(String fullName, String email, String phone, String address) {
        waitForElementVisible(CheckoutPageUI.CHECKOUT_FORM);
        sendKeyToElement(CheckoutPageUI.CHECKOUT_NAME_INPUT, fullName);
        sendKeyToElement(CheckoutPageUI.CHECKOUT_EMAIL_INPUT, email);
        sendKeyToElement(CheckoutPageUI.CHECKOUT_PHONE_INPUT, phone);
        sendKeyToElement(CheckoutPageUI.CHECKOUT_ADDRESS_INPUT, address);
    }

    public void submitOrder() {
        clickToElement(CheckoutPageUI.CHECKOUT_SUBMIT_BTN);
    }

    /** Sau khi dat hang, SUT hien ma don hang ngay trong giao dien thanh toan. */
    public boolean isOrderCodeDisplayed() {
        return isElementDisplayedWithWait(CheckoutPageUI.CHECKOUT_ORDER_CODE, 15);
    }

    public String getOrderCode() {
        waitForElementVisible(CheckoutPageUI.CHECKOUT_ORDER_CODE);
        return getElementText(CheckoutPageUI.CHECKOUT_ORDER_CODE).trim();
    }

    public boolean isPaymentViewDisplayed() {
        return isElementDisplayedWithWait(CheckoutPageUI.CHECKOUT_PAYMENT_VIEW, 15);
    }

    // ---------- Trang thanh toan PayOS (mock) ----------
    public boolean isPaymentPageDisplayed() {
        return isElementDisplayedWithWait(CheckoutPageUI.PAYMENT_PAGE, 15);
    }

    public String getPaymentTotalText() {
        waitForElementVisible(CheckoutPageUI.PAYMENT_TOTAL_AMOUNT);
        return getElementText(CheckoutPageUI.PAYMENT_TOTAL_AMOUNT);
    }

    /** Gia lap thanh toan PayOS thanh cong bang nut co san tren cong mock. */
    public void simulatePayosSuccess() {
        waitForElementVisible(CheckoutPageUI.SIMULATE_PAYOS_SUCCESS_BTN);
        clickToElement(CheckoutPageUI.SIMULATE_PAYOS_SUCCESS_BTN);
    }

    public boolean isOrderPaidViewDisplayed() {
        return isElementDisplayedWithWait(CheckoutPageUI.ORDER_PAID_VIEW, 15);
    }

    public OrdersPageObject clickViewOrders() {
        clickToElement(CheckoutPageUI.VIEW_ORDERS_BTN);
        return new OrdersPageObject();
    }
}
