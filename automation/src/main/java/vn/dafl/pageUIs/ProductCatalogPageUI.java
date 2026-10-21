package vn.dafl.pageUIs;

/** Locator cua luoi san pham o trang chu.
 *  Moi san pham co data-testid dong theo id san pham (trich tu ProductCard.tsx):
 *  product-card-{id}, product-name-{id}, product-price-{id}, add-to-cart-btn-{id}. */
public class ProductCatalogPageUI {

    public static final String ANY_PRODUCT_CARD = "css=[data-testid^='product-card-']";
    public static final String DYNAMIC_PRODUCT_CARD = "css=[data-testid='product-card-%s']";
    public static final String DYNAMIC_PRODUCT_NAME = "css=[data-testid='product-name-%s']";
    public static final String DYNAMIC_PRODUCT_PRICE = "css=[data-testid='product-price-%s']";
    public static final String DYNAMIC_PRODUCT_STOCK = "css=[data-testid='product-stock-%s']";
    public static final String DYNAMIC_ADD_TO_CART_BTN = "css=[data-testid='add-to-cart-btn-%s']";

    public static final String CATEGORY_FILTER_SECTION = "css=[data-testid='category-filter-section']";
    public static final String CATEGORY_SEARCH_INPUT = "css=[data-testid='category-search-input']";
    public static final String CATEGORY_SEARCH_CLEAR_BTN = "css=[data-testid='category-search-clear-btn']";
    public static final String CATEGORY_EMPTY_STATE = "css=[data-testid='category-empty-state']";
    public static final String CLEAR_CATEGORY_BTN = "css=[data-testid='clear-category-btn']";
    public static final String PRODUCT_DETAIL_PAGE = "css=[data-testid='product-detail-page']";
    public static final String PRODUCT_DETAIL_BACK_BTN = "css=[data-testid='product-detail-back-btn']";
}
