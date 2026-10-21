package vn.dafl.commons;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import vn.dafl.driver.DriverManager;

import java.time.Duration;
import java.util.List;

/**
 * Lop co so cho moi Page Object: thao tac phan tu, cho doi (explicit wait),
 * phan tich locator dang "css=..." / "xpath=..." / "id=..." / "name=..." / "class=...".
 * Locator dong dung String.format voi tham so %s.
 */
public class BasePage {

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    protected WebDriverWait getWait(long timeoutInSeconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }

    public void openPageUrl(String url) {
        getDriver().get(url);
    }

    public String getCurrentPageUrl() {
        return getDriver().getCurrentUrl();
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }

    public By getByLocator(String locator) {
        if (locator.startsWith("css=")) {
            return By.cssSelector(locator.substring(4));
        } else if (locator.startsWith("xpath=")) {
            return By.xpath(locator.substring(6));
        } else if (locator.startsWith("id=")) {
            return By.id(locator.substring(3));
        } else if (locator.startsWith("name=")) {
            return By.name(locator.substring(5));
        } else if (locator.startsWith("class=")) {
            return By.className(locator.substring(6));
        } else if (locator.startsWith("tag=")) {
            return By.tagName(locator.substring(4));
        }
        throw new IllegalArgumentException("Locator chua dinh nghia tien to (css=/xpath=/id=/name=/class=): " + locator);
    }

    public String getDynamicLocator(String locator, String... values) {
        return String.format(locator, (Object[]) values);
    }

    public WebElement getWebElement(String locator) {
        return getDriver().findElement(getByLocator(locator));
    }

    public WebElement getWebElement(String locator, String... values) {
        return getDriver().findElement(getByLocator(getDynamicLocator(locator, values)));
    }

    public List<WebElement> getListWebElements(String locator) {
        return getDriver().findElements(getByLocator(locator));
    }

    public List<WebElement> getListWebElements(String locator, String... values) {
        return getDriver().findElements(getByLocator(getDynamicLocator(locator, values)));
    }

    public void clickToElement(String locator) {
        waitForElementClickable(locator);
        getWebElement(locator).click();
    }

    public void clickToElement(String locator, String... values) {
        String dynamic = getDynamicLocator(locator, values);
        waitForElementClickable(dynamic);
        getWebElement(dynamic).click();
    }

    public void clickToElementByJS(String locator) {
        WebElement element = getWebElement(locator);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
    }

    public void sendKeyToElement(String locator, String textValue) {
        waitForElementVisible(locator);
        WebElement element = getWebElement(locator);
        element.clear();
        element.sendKeys(textValue);
    }

    public void sendKeyToElement(String locator, String textValue, String... values) {
        String dynamic = getDynamicLocator(locator, values);
        waitForElementVisible(dynamic);
        WebElement element = getWebElement(dynamic);
        element.clear();
        element.sendKeys(textValue);
    }

    public String getElementText(String locator) {
        waitForElementVisible(locator);
        return getWebElement(locator).getText().trim();
    }

    public String getElementText(String locator, String... values) {
        String dynamic = getDynamicLocator(locator, values);
        waitForElementVisible(dynamic);
        return getWebElement(dynamic).getText().trim();
    }

    public String getElementAttribute(String locator, String attributeName) {
        waitForElementVisible(locator);
        return getWebElement(locator).getAttribute(attributeName);
    }

    public boolean isElementDisplayed(String locator) {
        try {
            return getWebElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementDisplayed(String locator, String... values) {
        try {
            return getWebElement(getDynamicLocator(locator, values)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementDisplayedWithWait(String locator, long timeoutInSeconds) {
        try {
            getWait(timeoutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForElementVisible(String locator) {
        getWait(GlobalConstants.LONG_TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
    }

    public void waitForElementVisible(String locator, String... values) {
        waitForElementVisible(getDynamicLocator(locator, values));
    }

    public void waitForElementClickable(String locator) {
        getWait(GlobalConstants.LONG_TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(getByLocator(locator)));
    }

    public void waitForElementClickable(String locator, String... values) {
        waitForElementClickable(getDynamicLocator(locator, values));
    }

    public void waitForElementInvisible(String locator) {
        getWait(GlobalConstants.LONG_TIMEOUT)
                .until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locator)));
    }

    public void scrollToElement(String locator) {
        WebElement element = getWebElement(locator);
        ((JavascriptExecutor) getDriver()).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
    }

    public void sleepInSecond(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
