package vn.dafl.pageObjects;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import vn.dafl.commons.BasePage;
import vn.dafl.commons.GlobalConstants;
import vn.dafl.pageUIs.CartCheckoutUI;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckoutPageObject extends BasePage {
    public CheckoutPageObject(WebDriver driver) { super(driver); }
    public boolean isPaymentViewDisplayed() { return isElementDisplayed(CartCheckoutUI.CHECKOUT_PAYMENT_VIEW); }
    public long getOrderCode() {
        Matcher m = Pattern.compile("(\\d+)").matcher(getElementText(CartCheckoutUI.CHECKOUT_ORDER_CODE));
        return m.find() ? Long.parseLong(m.group(1)) : 0;
    }
    public long getTotalAmount() {
        String clean = getElementText(CartCheckoutUI.PAYMENT_TOTAL_AMOUNT).replaceAll("[^0-9]", "");
        return clean.isEmpty() ? 0 : Long.parseLong(clean);
    }
    /** Gia lap webhook PayOS thanh cong voi chu ky HMAC-SHA256 (chi moi truong test, nhu ban goc). */
    public void simulatePayOSWebhookSuccess(long orderCode, long amount) {
        String reference = "TEST_E2E_" + System.currentTimeMillis();
        String description = "DH " + orderCode;
        String dataString = "amount=" + amount + "&description=" + description + "&orderCode=" + orderCode + "&reference=" + reference;
        String signature = computeHmacSha256(dataString, GlobalConstants.getPayosChecksumKey());
        String js = String.format(
            "fetch('/api/payments/payos-webhook', {method:'POST', headers:{'Content-Type':'application/json'},"
          + " body: JSON.stringify({code:'00', desc:'success', data:{orderCode:%d, amount:%d, description:'%s',"
          + " accountNumber:'9999888877', reference:'%s'}, signature:'%s'})}).catch(console.warn);",
            orderCode, amount, description, reference, signature);
        ((JavascriptExecutor) driver).executeScript(js);
    }
    public void clickSimulateSuccessIfPresent() {
        if (driver.findElements(CartCheckoutUI.SIMULATE_PAYOS_SUCCESS_BTN).size() > 0
                && isElementDisplayed(CartCheckoutUI.SIMULATE_PAYOS_SUCCESS_BTN)) clickByJs(CartCheckoutUI.SIMULATE_PAYOS_SUCCESS_BTN);
    }
    public boolean isOrderPaidDisplayed() { return isElementDisplayed(CartCheckoutUI.ORDER_PAID_VIEW); }
    public String getOrderPaidText() { return getElementText(CartCheckoutUI.ORDER_PAID_VIEW); }
    public void clickContinueShopping() { clickToElement(CartCheckoutUI.CONTINUE_SHOPPING_BTN); }
    private static String computeHmacSha256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { throw new RuntimeException("Loi tinh HMAC-SHA256 webhook PayOS", e); }
    }
}
