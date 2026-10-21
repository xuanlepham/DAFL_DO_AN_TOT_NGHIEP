package vn.dafl.pageObjects;
import org.openqa.selenium.WebDriver;
import vn.dafl.commons.BasePage;
import vn.dafl.pageUIs.HomePageUI;
public class HomePageObject extends BasePage {
    public HomePageObject(WebDriver driver) { super(driver); }
    public HomePageObject open(String baseUrl) { openUrl(baseUrl); return this; }
    public String getBrandText() { return getElementText(HomePageUI.BRAND_LOGO); }
    public boolean isBrandDisplayed() { return isElementDisplayed(HomePageUI.BRAND_LOGO); }
    public HomePageObject search(String keyword) { sendKeysToElement(HomePageUI.SEARCH_INPUT, keyword); return this; }
    public String getSearchValue() { return waitForVisible(HomePageUI.SEARCH_INPUT).getAttribute("value"); }
    public boolean isThemeToggleDisplayed() { return isElementDisplayed(HomePageUI.THEME_TOGGLE_BTN); }
    public int getProductCardCount() { return getElements(HomePageUI.PRODUCT_CARDS).size(); }
    public HomePageObject clickFirstAddToCart() { clickToElement(HomePageUI.ADD_TO_CART_BTNS); return this; }
    public HomePageObject openCart() { clickToElement(HomePageUI.CART_BTN); return this; }
    public HomePageObject openAccountMenu() { clickToElement(HomePageUI.ACCOUNT_TOGGLE_BTN); return this; }
    public HomePageObject openLoginFromMenu() { openAccountMenu(); clickToElement(HomePageUI.DROPDOWN_LOGIN_BTN); return this; }
    public boolean isLogoutDisplayed() { return isElementDisplayed(HomePageUI.DROPDOWN_LOGOUT_BTN); }
    public boolean isLoginEntryDisplayed() { return isElementDisplayed(HomePageUI.DROPDOWN_LOGIN_BTN); }
    public void logout() { openAccountMenu(); clickToElement(HomePageUI.DROPDOWN_LOGOUT_BTN); }
    public HomePageObject clickFirstCategoryPill() { clickToElement(HomePageUI.CATEGORY_PILLS); return this; }
    public HomePageObject clickCategoryAll() { clickToElement(HomePageUI.CATEGORY_PILL_ALL); return this; }
    public boolean hasNoResultMessage() {
        try { return wait.until(org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated(
                org.openqa.selenium.By.tagName("body"), "Không tìm thấy sản phẩm nào phù hợp với tìm kiếm.")); }
        catch (Exception e) { return false; }
    }
}
