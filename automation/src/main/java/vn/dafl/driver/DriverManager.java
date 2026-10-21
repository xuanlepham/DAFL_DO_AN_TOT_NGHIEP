package vn.dafl.driver;

import org.openqa.selenium.WebDriver;

/** Quan ly WebDriver theo tung thread (an toan khi chay song song). */
public class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {
                // Bo qua loi khi dong trinh duyet
            } finally {
                DRIVER.remove();
            }
        }
    }
}
