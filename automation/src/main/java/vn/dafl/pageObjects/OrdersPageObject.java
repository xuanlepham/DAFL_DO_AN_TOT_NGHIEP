package vn.dafl.pageObjects;

import vn.dafl.commons.BasePage;
import vn.dafl.pageUIs.OrdersPageUI;

/** Page Object cho trang lich su don hang /orders. */
public class OrdersPageObject extends BasePage {

    public boolean isOrdersPageDisplayed() {
        return getCurrentPageUrl().contains("/orders");
    }

    /** Don hang duoc coi la xuat hien khi ma don (order code) co trong trang. */
    public boolean isOrderDisplayed(String orderCode) {
        return isElementDisplayedWithWait(OrdersPageUI.DYNAMIC_ORDER_CODE_TEXT.replace("%s", orderCode), 10);
    }
}
