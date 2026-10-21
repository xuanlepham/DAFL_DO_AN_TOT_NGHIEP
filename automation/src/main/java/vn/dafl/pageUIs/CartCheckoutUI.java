package vn.dafl.pageUIs;
import org.openqa.selenium.By;
import vn.dafl.commons.BasePage;
public class CartCheckoutUI {
    public static final By CART_DRAWER = BasePage.testId("cart-drawer");
    public static final By CART_BADGE_COUNT = BasePage.testId("cart-badge-count");
    public static final By CHECKOUT_NAME_INPUT = BasePage.testId("checkout-name-input");
    public static final By CHECKOUT_EMAIL_INPUT = BasePage.testId("checkout-email-input");
    public static final By CHECKOUT_PHONE_INPUT = BasePage.testId("checkout-phone-input");
    public static final By CHECKOUT_ADDRESS_INPUT = BasePage.testId("checkout-address-input");
    public static final By CHECKOUT_SUBMIT_BTN = BasePage.testId("checkout-submit-btn");
    public static final By CHECKOUT_PAYMENT_VIEW = BasePage.testId("checkout-payment-view");
    public static final By CHECKOUT_ORDER_CODE = BasePage.testId("checkout-order-code");
    public static final By PAYMENT_TOTAL_AMOUNT = BasePage.testId("payment-total-amount");
    public static final By SIMULATE_PAYOS_SUCCESS_BTN = BasePage.testId("simulate-payos-success-btn");
    public static final By ORDER_PAID_VIEW = BasePage.testId("order-paid-view");
    public static final By CONTINUE_SHOPPING_BTN = BasePage.testId("continue-shopping-btn");
    public static final By VOUCHER_CODE_INPUT = BasePage.testId("voucher-code-input");
    public static final By VOUCHER_APPLY_BTN = BasePage.testId("voucher-apply-btn");
    public static final By VOUCHER_ERROR_MSG = BasePage.testId("voucher-error-msg");
    public static final By VOUCHER_DISCOUNT = BasePage.testId("cart-voucher-discount");
    public static final By APPLIED_VOUCHER_BANNER = BasePage.testId("applied-voucher-banner");
}
