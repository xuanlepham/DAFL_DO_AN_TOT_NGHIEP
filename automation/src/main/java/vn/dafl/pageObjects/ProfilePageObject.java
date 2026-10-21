package vn.dafl.pageObjects;

import vn.dafl.commons.BasePage;
import vn.dafl.pageUIs.ProfilePageUI;

/** Page Object cho trang ho so /profile. */
public class ProfilePageObject extends BasePage {

    public boolean isProfilePageDisplayed() {
        return getCurrentPageUrl().contains("/profile");
    }

    public boolean isTextDisplayedOnPage(String text) {
        return isElementDisplayedWithWait(ProfilePageUI.DYNAMIC_TEXT_ON_PAGE.replace("%s", text), 10);
    }
}
