package vn.dafl.pageUIs;

/** Locator cua trang lich su don hang /orders.
 *  Trang nay khong co data-testid rieng cho danh sach, nen xac thuc don hang
 *  bang ma don (order code) xuat hien trong noi dung trang. */
public class OrdersPageUI {

    public static final String DYNAMIC_ORDER_CODE_TEXT = "xpath=//*[contains(text(),'%s')]";
    public static final String ORDERS_HEADING = "xpath=//*[self::h1 or self::h2][contains(text(),'Đơn hàng')]";
}
