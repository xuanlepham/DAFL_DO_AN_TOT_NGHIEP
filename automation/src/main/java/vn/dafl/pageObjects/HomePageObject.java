package vn.dafl.pageObjects;

import org.openqa.selenium.WebElement;
import vn.dafl.commons.BasePage;
import vn.dafl.pageUIs.HomePageUI;

import java.util.List;

/** Page Object cho Header va cac dieu huong chung cua SUT. */
public class HomePageObject extends BasePage {

    public void waitForHomePageLoaded() {
        waitForElementVisible(HomePageUI.BRAND_LOGO);
        waitForElementVisible(HomePageUI.CART_BTN);
    }

    public boolean isBrandLogoDisplayed() {
        return isElementDisplayed(HomePageUI.BRAND_LOGO);
    }

    // ---------- Tim kiem (o tim kiem nam o header) ----------
    public void enterSearchKeyword(String keyword) {
        sendKeyToElement(HomePageUI.SEARCH_INPUT, keyword);
    }

    public void clearSearchKeyword() {
        if (isElementDisplayed(HomePageUI.SEARCH_CLEAR_BTN)) {
            clickToElement(HomePageUI.SEARCH_CLEAR_BTN);
        }
    }

    // ---------- Tai khoan ----------
    public void openAccountDropdown() {
        // Chi bam nut toggle khi menu dang dong: nut nay co tinh chat bat/tat nen
        // neu menu dang mo ma bam nua se thanh dong lai, roi cho vo ich 30 giay.
        if (!isElementDisplayed(HomePageUI.ACCOUNT_DROPDOWN_MENU)) {
            clickToElement(HomePageUI.ACCOUNT_TOGGLE_BTN);
        }
        waitForElementVisible(HomePageUI.ACCOUNT_DROPDOWN_MENU);
    }

    public AuthModalPageObject openLoginModal() {
        if (!isElementDisplayed(HomePageUI.ACCOUNT_DROPDOWN_MENU)) {
            openAccountDropdown();
        }
        clickToElement(HomePageUI.DROPDOWN_LOGIN_BTN);
        AuthModalPageObject authModal = new AuthModalPageObject();
        authModal.waitForModalVisible();
        return authModal;
    }

    /** Da dang nhap hay chua: sau dang nhap, menu tai khoan co nut "Dang xuat". */
    public boolean isUserLoggedIn() {
        openAccountDropdown();
        return isElementDisplayed(HomePageUI.DROPDOWN_LOGOUT_BTN);
    }

    public void waitForLoggedIn() {
        openAccountDropdown();
        waitForElementVisible(HomePageUI.DROPDOWN_LOGOUT_BTN);
        clickToElement(HomePageUI.ACCOUNT_TOGGLE_BTN); // dong dropdown lai
    }

    public void logout() {
        openAccountDropdown();
        clickToElement(HomePageUI.DROPDOWN_LOGOUT_BTN);
        openAccountDropdown();
        waitForElementVisible(HomePageUI.DROPDOWN_LOGIN_BTN);
        clickToElement(HomePageUI.ACCOUNT_TOGGLE_BTN);
    }

    public OrdersPageObject navigateToOrdersPage() {
        openAccountDropdown();
        clickToElement(HomePageUI.DROPDOWN_ORDERS_BTN);
        return new OrdersPageObject();
    }

    public ProfilePageObject navigateToProfilePage() {
        openAccountDropdown();
        clickToElement(HomePageUI.DROPDOWN_PROFILE_BTN);
        return new ProfilePageObject();
    }

    // ---------- Gio hang ----------
    public CartDrawerPageObject openCartDrawer() {
        clickToElement(HomePageUI.CART_BTN);
        CartDrawerPageObject cartDrawer = new CartDrawerPageObject();
        cartDrawer.waitForDrawerVisible();
        return cartDrawer;
    }

    public int getCartBadgeCount() {
        if (!isElementDisplayed(HomePageUI.CART_BADGE_COUNT)) {
            return 0;
        }
        String text = getElementText(HomePageUI.CART_BADGE_COUNT);
        return text.isEmpty() ? 0 : Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }

    public void waitForCartBadgeCount(int expected) {
        long deadline = System.currentTimeMillis() + 15000;
        while (System.currentTimeMillis() < deadline) {
            if (getCartBadgeCount() == expected) {
                return;
            }
            sleepInSecond(1);
        }
        throw new AssertionError("Badge gio hang khong dat gia tri " + expected + " sau 15 giay");
    }

    // ---------- Danh muc ----------
    public List<WebElement> getCategoryPills() {
        waitForElementVisible(HomePageUI.CATEGORIES_BAR);
        return getListWebElements(HomePageUI.ANY_CATEGORY_PILL);
    }

    public void clickCategoryPillById(String categoryId) {
        clickToElement(HomePageUI.DYNAMIC_CATEGORY_PILL, categoryId);
    }

    public String getFirstCategoryId() {
        for (WebElement pill : getCategoryPills()) {
            String testId = pill.getAttribute("data-testid");
            if (testId != null && !testId.equals("category-pill-all")) {
                return testId.replace("category-pill-", "");
            }
        }
        throw new IllegalStateException("Khong tim thay danh muc nao tren trang chu");
    }
}
