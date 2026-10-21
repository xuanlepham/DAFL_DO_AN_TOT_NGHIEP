package vn.dafl.pageUIs;

/** Locator cua gio hang (drawer o trang chu va cung giao dien tai /cart).
 *  Moi dong san pham co data-testid dong theo id san pham (trich tu CartItemRow.tsx):
 *  cart-item-{id}, cart-item-name-{id}, cart-item-price-{id},
 *  cart-item-minus-{id}, cart-item-qty-{id}, cart-item-plus-{id}. */
public class CartDrawerPageUI {

    public static final String CART_DRAWER = "css=[data-testid='cart-drawer']";
    public static final String CART_CLOSE_BTN = "css=[data-testid='cart-close-btn']";
    public static final String CART_ITEMS_LIST = "css=[data-testid='cart-items-list']";
    public static final String CART_EMPTY_STATE = "css=[data-testid='cart-empty-state']";
    public static final String CONTINUE_SHOPPING_BTN = "css=[data-testid='continue-shopping-btn']";

    public static final String DYNAMIC_CART_ITEM = "css=[data-testid='cart-item-%s']";
    public static final String DYNAMIC_CART_ITEM_NAME = "css=[data-testid='cart-item-name-%s']";
    public static final String DYNAMIC_CART_ITEM_PRICE = "css=[data-testid='cart-item-price-%s']";
    public static final String DYNAMIC_CART_ITEM_MINUS_BTN = "css=[data-testid='cart-item-minus-%s']";
    public static final String DYNAMIC_CART_ITEM_QTY = "css=[data-testid='cart-item-qty-%s']";
    public static final String DYNAMIC_CART_ITEM_PLUS_BTN = "css=[data-testid='cart-item-plus-%s']";

    // Bang gia + voucher nam trong khu vuc gio hang
    public static final String CART_PRICING_SUMMARY = "css=[data-testid='cart-pricing-summary']";
    public static final String CART_RAW_SUBTOTAL = "css=[data-testid='cart-raw-subtotal']";
    public static final String CART_VOUCHER_DISCOUNT = "css=[data-testid='cart-voucher-discount']";
    public static final String CART_FINAL_TOTAL = "css=[data-testid='cart-final-total']";
    public static final String VOUCHER_SECTION = "css=[data-testid='voucher-section']";
    public static final String VOUCHER_CODE_INPUT = "css=[data-testid='voucher-code-input']";
    public static final String VOUCHER_APPLY_BTN = "css=[data-testid='voucher-apply-btn']";
    public static final String VOUCHER_ERROR_MSG = "css=[data-testid='voucher-error-msg']";
    public static final String APPLIED_VOUCHER_BANNER = "css=[data-testid='applied-voucher-banner']";
    public static final String REMOVE_VOUCHER_BTN = "css=[data-testid='remove-voucher-btn']";
    public static final String DYNAMIC_VOUCHER_CHIP = "css=[data-testid='voucher-chip-%s']";
}
