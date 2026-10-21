package vn.dafl.pageUIs;
import org.openqa.selenium.By;
import vn.dafl.commons.BasePage;
/** Locator trang chu/header - tach rieng theo mau WebRA pageUIs. Gia tri data-testid lay tu FE (src/Frontend). */
public class HomePageUI {
    public static final By BRAND_LOGO = BasePage.testId("brand-logo");
    public static final By SEARCH_INPUT = BasePage.testId("search-input");
    public static final By THEME_TOGGLE_BTN = BasePage.testId("theme-toggle-btn");
    public static final By ACCOUNT_TOGGLE_BTN = BasePage.testId("account-toggle-btn");
    public static final By DROPDOWN_LOGIN_BTN = BasePage.testId("dropdown-login-btn");
    public static final By DROPDOWN_LOGOUT_BTN = BasePage.testId("dropdown-logout-btn");
    public static final By CART_BTN = BasePage.testId("cart-btn");
    public static final By PRODUCT_CARDS = By.cssSelector(".product-card");
    public static final By ADD_TO_CART_BTNS = By.cssSelector("button[data-testid^='add-to-cart-btn-']");
    public static final By CATEGORY_PILL_ALL = BasePage.testId("category-pill-all");
    public static final By CATEGORY_PILLS = By.cssSelector("[data-testid^='category-pill-']:not([data-testid='category-pill-all'])");
    public static final By DROPDOWN_LOGIN_BTN_VIS = BasePage.testId("dropdown-login-btn");
}
