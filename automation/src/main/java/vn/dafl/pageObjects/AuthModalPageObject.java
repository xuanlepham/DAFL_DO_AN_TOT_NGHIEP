package vn.dafl.pageObjects;
import org.openqa.selenium.WebDriver;
import vn.dafl.commons.BasePage;
import vn.dafl.commons.GlobalConstants;
import vn.dafl.pageUIs.AuthModalUI;
public class AuthModalPageObject extends BasePage {
    public AuthModalPageObject(WebDriver driver) { super(driver); }
    public boolean isAuthModalDisplayed() { return isElementDisplayed(AuthModalUI.AUTH_MODAL); }
    public boolean isLoginFieldsDisplayed() {
        return isElementDisplayed(AuthModalUI.LOGIN_EMAIL_INPUT) && isElementDisplayed(AuthModalUI.LOGIN_PASSWORD_INPUT);
    }
    public AuthModalPageObject switchToRegisterTab() { clickToElement(AuthModalUI.AUTH_TAB_REGISTER); return this; }
    public boolean isRegisterConfirmPasswordDisplayed() { return isElementDisplayed(AuthModalUI.REGISTER_CONFIRM_PASSWORD_INPUT); }
    public AuthModalPageObject inputLoginCredentials(String email, String password) {
        sendKeysToElement(AuthModalUI.LOGIN_EMAIL_INPUT, email);
        sendKeysToElement(AuthModalUI.LOGIN_PASSWORD_INPUT, password); return this;
    }
    public AuthModalPageObject clickDemoAccount() { clickToElement(AuthModalUI.DEMO_ACCOUNT_BTN); return this; }
    public void submitLogin() { clickToElement(AuthModalUI.LOGIN_SUBMIT_BTN); }
    /** Dang nhap bang tai khoan demo seed: bam nut dien san thong tin roi submit. */
    public void loginWithDemoAccount() { clickDemoAccount(); submitLogin(); }
    public void login(String email, String password) { inputLoginCredentials(email, password); submitLogin(); }
    public String getErrorMessage() { return getElementText(AuthModalUI.AUTH_ERROR_MSG); }
    public boolean isErrorDisplayed() { return isElementDisplayed(AuthModalUI.AUTH_ERROR_MSG); }
    public boolean isModalClosed() { return isElementUndisplayed(AuthModalUI.AUTH_MODAL); }
    public String demoEmail() { return GlobalConstants.DEMO_EMAIL; }
    public AuthModalPageObject fillRegisterForm(String name, String email, String phone, String address, String password, String confirm) {
        sendKeysToElement(AuthModalUI.REGISTER_NAME_INPUT, name);
        sendKeysToElement(AuthModalUI.REGISTER_EMAIL_INPUT, email);
        sendKeysToElement(AuthModalUI.REGISTER_PHONE_INPUT, phone);
        sendKeysToElement(AuthModalUI.REGISTER_ADDRESS_INPUT, address);
        sendKeysToElement(AuthModalUI.REGISTER_PASSWORD_INPUT, password);
        sendKeysToElement(AuthModalUI.REGISTER_CONFIRM_PASSWORD_INPUT, confirm); return this;
    }
    public void submitRegister() { clickToElement(AuthModalUI.REGISTER_SUBMIT_BTN); }
    public void register(String name, String email, String phone, String address, String password, String confirm) {
        switchToRegisterTab(); fillRegisterForm(name, email, phone, address, password, confirm); submitRegister();
    }
}
