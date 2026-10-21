package vn.dafl.pageObjects;

import org.openqa.selenium.WebElement;
import vn.dafl.commons.BasePage;
import vn.dafl.pageUIs.ProductCatalogPageUI;

import java.util.List;

/** Page Object cho luoi san pham: doc id san pham dong tu data-testid,
 *  them gio, loc danh muc va trang thai rong. */
public class ProductCatalogPageObject extends BasePage {

    public List<WebElement> getProductCards() {
        return getListWebElements(ProductCatalogPageUI.ANY_PRODUCT_CARD);
    }

    public int getProductCount() {
        return getProductCards().size();
    }

    public void waitForProductsLoaded() {
        waitForElementVisible(ProductCatalogPageUI.ANY_PRODUCT_CARD);
    }

    /** Lay id cua san pham o vi tri index (bat dau tu 0) tu data-testid "product-card-{id}". */
    public String getProductIdAt(int index) {
        List<WebElement> cards = getProductCards();
        if (index >= cards.size()) {
            throw new IllegalStateException("Chi co " + cards.size() + " san pham, khong co vi tri " + index);
        }
        String testId = cards.get(index).getAttribute("data-testid");
        return testId.replace("product-card-", "");
    }

    public String getFirstProductId() {
        return getProductIdAt(0);
    }

    public String getProductName(String productId) {
        return getElementText(ProductCatalogPageUI.DYNAMIC_PRODUCT_NAME, productId);
    }

    public String getProductPriceText(String productId) {
        return getElementText(ProductCatalogPageUI.DYNAMIC_PRODUCT_PRICE, productId);
    }

    public void addProductToCart(String productId) {
        scrollToElement(ProductCatalogPageUI.DYNAMIC_PRODUCT_CARD.replace("%s", productId));
        clickToElement(ProductCatalogPageUI.DYNAMIC_ADD_TO_CART_BTN, productId);
    }

    public void addFirstProductToCart() {
        addProductToCart(getFirstProductId());
    }

    public boolean isCategoryEmptyStateDisplayed() {
        return isElementDisplayedWithWait(ProductCatalogPageUI.CATEGORY_EMPTY_STATE, 5);
    }

    public boolean isProductDetailDisplayed() {
        return isElementDisplayedWithWait(ProductCatalogPageUI.PRODUCT_DETAIL_PAGE, 5);
    }

    public void openProductDetail(String productId) {
        clickToElement(ProductCatalogPageUI.DYNAMIC_PRODUCT_CARD, productId);
    }

    public void backToCatalog() {
        clickToElement(ProductCatalogPageUI.PRODUCT_DETAIL_BACK_BTN);
    }
}
