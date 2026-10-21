package vn.dafl.commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;

/** BaseTest theo mau WebRA nhung dung Selenium Manager (tu tai ChromeDriver khop Chrome) -
 *  khong hardcode duong dan driver .exe cua mot may cu the nhu ban cu. */
public abstract class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseUrl;
    protected boolean isHeadless;

    @BeforeMethod
    public void setUp() {
        baseUrl = GlobalConstants.getAppUrl();
        String headlessProp = System.getProperty("headless");
        String headlessEnv = System.getenv("HEADLESS");
        isHeadless = headlessProp != null ? Boolean.parseBoolean(headlessProp)
                   : headlessEnv != null ? Boolean.parseBoolean(headlessEnv) : true;
        ChromeOptions options = new ChromeOptions();
        if (isHeadless) options.addArguments("--headless=new");
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu",
                "--window-size=1920,1080", "--remote-allow-origins=*", "--ignore-certificate-errors");
        String chromeBin = System.getenv("CHROME_BIN");
        if (chromeBin != null && !chromeBin.isEmpty()) options.setBinary(chromeBin);
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(GlobalConstants.PAGE_LOAD_TIMEOUT));
        wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalConstants.LONG_TIMEOUT));
    }
    @AfterMethod(alwaysRun = true)
    public void tearDown() { if (driver != null) { try { driver.quit(); } catch (Exception ignored) {} } }
}
