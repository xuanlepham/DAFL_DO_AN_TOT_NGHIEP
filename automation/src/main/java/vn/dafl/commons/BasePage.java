package vn.dafl.commons;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

/** BasePage theo mau WebRA: cac thao tac WebDriver dung chung, chi dung explicit wait - khong Thread.sleep. */
public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalConstants.LONG_TIMEOUT));
    }
    public static By testId(String id) { return By.cssSelector("[data-testid='" + id + "']"); }
    public static By testIdPrefix(String prefix) { return By.cssSelector("[data-testid^='" + prefix + "']"); }

    public void openUrl(String url) { driver.get(url); }
    public WebElement waitForVisible(By by) { return wait.until(ExpectedConditions.visibilityOfElementLocated(by)); }
    public WebElement waitForClickable(By by) { return wait.until(ExpectedConditions.elementToBeClickable(by)); }
    public void clickToElement(By by) { waitForClickable(by).click(); }
    public void clickByJs(By by) { WebElement e = waitForVisible(by); ((JavascriptExecutor) driver).executeScript("arguments[0].click();", e); }
    public void sendKeysToElement(By by, String value) { WebElement e = waitForVisible(by); e.clear(); e.sendKeys(value); }
    public String getElementText(By by) { return waitForVisible(by).getText(); }
    public boolean isElementDisplayed(By by) {
        try { return waitForVisible(by).isDisplayed(); } catch (TimeoutException | NoSuchElementException e) { return false; }
    }
    public boolean isElementUndisplayed(By by) {
        try { return wait.until(ExpectedConditions.invisibilityOfElementLocated(by)); }
        catch (TimeoutException e) { return driver.findElements(by).isEmpty(); }
    }
    public List<WebElement> getElements(By by) { wait.until(ExpectedConditions.presenceOfElementLocated(by)); return driver.findElements(by); }
    public int getElementCount(By by) { return driver.findElements(by).size(); }
}
