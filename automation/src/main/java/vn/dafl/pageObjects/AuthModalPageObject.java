package vn.dafl.pageObjects;

import vn.dafl.commons.BasePage;
import vn.dafl.pageUIs.AuthModalPageUI;

/** Page Object cho Auth Modal: dang nhap, dang ky, tai khoan demo, thong diep loi. */
public class AuthModalPageObject extends BasePage {

    public void waitForModalVisible() {
        waitForElementVisible(AuthModalPageUI.AUTH_MODAL);
    }

    public boolean isLoginFormDisplayed() {
        return isElementDisplayed(AuthModalPageUI.LOGIN_FORM);
    }

    public boolean isRegisterFormDisplayed() {
        return isElementDisplayed(AuthModalPageUI.REGISTER_FORM);
    }

    /** Tieu de modal thay doi theo tab: "Dang nhap tai khoan" / "Dang ky thanh vien moi". */
    public String getModalTitle() {
        return getElementText(AuthModalPageUI.AUTH_MODAL_TITLE);
    }

    public void loginAs(String email, String password) {
        switchToLoginTab();
        sendKeyToElement(AuthModalPageUI.LOGIN_EMAIL_INPUT, email);
        sendKeyToElement(AuthModalPageUI.LOGIN_PASSWORD_INPUT, password);
        clickToElement(AuthModalPageUI.LOGIN_SUBMIT_BTN);
    }

    /** Nut "dung tai khoan demo" tren form: tu dien thong tin tai khoan demo. */
    public void clickDemoAccountButton() {
        switchToLoginTab();
        clickToElement(AuthModalPageUI.DEMO_ACCOUNT_BTN);
    }

    public void submitLogin() {
        clickToElement(AuthModalPageUI.LOGIN_SUBMIT_BTN);
    }

    /** Chi go mat khau vao form dang nhap, chua bam nut Dang nhap. */
    public void enterLoginPassword(String password) {
        sendKeyToElement(AuthModalPageUI.LOGIN_PASSWORD_INPUT, password);
    }

    /** Bam nut con mat (hien/an mat khau) tren form dang nhap. */
    public void toggleLoginPasswordVisibility() {
        clickToElement(AuthModalPageUI.TOGGLE_LOGIN_PASSWORD_BTN);
    }

    /** Thuoc tinh type cua o mat khau: "password" khi dang an, "text" khi dang hien. */
    public String getLoginPasswordInputType() {
        return getElementAttribute(AuthModalPageUI.LOGIN_PASSWORD_INPUT, "type");
    }

    public void switchToLoginTab() {
        if (isElementDisplayed(AuthModalPageUI.AUTH_TAB_LOGIN)) {
            clickToElement(AuthModalPageUI.AUTH_TAB_LOGIN);
        }
        waitForElementVisible(AuthModalPageUI.LOGIN_FORM);
    }

    public void switchToRegisterTab() {
        clickToElement(AuthModalPageUI.AUTH_TAB_REGISTER);
        waitForElementVisible(AuthModalPageUI.REGISTER_FORM);
    }

    public void register(String fullName, String email, String phone, String address,
                         String password, String confirmPassword) {
        switchToRegisterTab();
        sendKeyToElement(AuthModalPageUI.REGISTER_NAME_INPUT, fullName);
        sendKeyToElement(AuthModalPageUI.REGISTER_EMAIL_INPUT, email);
        sendKeyToElement(AuthModalPageUI.REGISTER_PHONE_INPUT, phone);
        sendKeyToElement(AuthModalPageUI.REGISTER_ADDRESS_INPUT, address);
        sendKeyToElement(AuthModalPageUI.REGISTER_PASSWORD_INPUT, password);
        sendKeyToElement(AuthModalPageUI.REGISTER_CONFIRM_PASSWORD_INPUT, confirmPassword);
        clickToElement(AuthModalPageUI.REGISTER_SUBMIT_BTN);
    }

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayedWithWait(AuthModalPageUI.AUTH_ERROR_MSG, 5);
    }

    public String getErrorMessage() {
        waitForElementVisible(AuthModalPageUI.AUTH_ERROR_MSG);
        return getElementText(AuthModalPageUI.AUTH_ERROR_MSG);
    }

    public boolean isModalStillOpen() {
        return isElementDisplayed(AuthModalPageUI.AUTH_MODAL);
    }

    public void closeModal() {
        clickToElement(AuthModalPageUI.AUTH_CLOSE_BTN);
        waitForElementInvisible(AuthModalPageUI.AUTH_MODAL);
    }
}
