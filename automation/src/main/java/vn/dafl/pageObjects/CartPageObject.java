package vn.dafl.pageObjects;
import org.openqa.selenium.WebDriver;
import vn.dafl.commons.BasePage;
import vn.dafl.pageUIs.CartCheckoutUI;
public class CartPageObject extends BasePage {
    public CartPageObject(WebDriver driver) { super(driver); }
    public boolean isCartDrawerDisplayed() { return isElementDisplayed(CartCheckoutUI.CART_DRAWER); }
    public CartPageObject fillCheckoutForm(String name, String email, String phone, String address) {
        sendKeysToElement(CartCheckoutUI.CHECKOUT_NAME_INPUT, name);
        sendKeysToElement(CartCheckoutUI.CHECKOUT_EMAIL_INPUT, email);
        sendKeysToElement(CartCheckoutUI.CHECKOUT_PHONE_INPUT, phone);
        sendKeysToElement(CartCheckoutUI.CHECKOUT_ADDRESS_INPUT, address); return this;
    }
    public boolean isCheckoutSubmitEnabled() { return waitForVisible(CartCheckoutUI.CHECKOUT_SUBMIT_BTN).isEnabled(); }
    public void submitCheckout() { clickToElement(CartCheckoutUI.CHECKOUT_SUBMIT_BTN); }
    public CartPageObject applyVoucher(String code) {
        sendKeysToElement(CartCheckoutUI.VOUCHER_CODE_INPUT, code);
        clickToElement(CartCheckoutUI.VOUCHER_APPLY_BTN); return this;
    }
    public boolean isVoucherDiscountDisplayed() { return isElementDisplayed(CartCheckoutUI.VOUCHER_DISCOUNT); }
    public boolean isAppliedVoucherBannerDisplayed() { return isElementDisplayed(CartCheckoutUI.APPLIED_VOUCHER_BANNER); }
    public boolean isVoucherErrorDisplayed() { return isElementDisplayed(CartCheckoutUI.VOUCHER_ERROR_MSG); }
    public String getVoucherErrorText() { return getElementText(CartCheckoutUI.VOUCHER_ERROR_MSG); }
    public void clearCheckoutName() { org.openqa.selenium.WebElement e = waitForVisible(CartCheckoutUI.CHECKOUT_NAME_INPUT); e.clear(); }
    public boolean isPaymentViewDisplayed() { return isElementDisplayed(CartCheckoutUI.CHECKOUT_PAYMENT_VIEW); }
}
