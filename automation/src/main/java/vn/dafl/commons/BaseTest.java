package vn.dafl.commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import vn.dafl.driver.DriverManager;
import vn.dafl.pageObjects.AuthModalPageObject;
import vn.dafl.pageObjects.HomePageObject;

import java.time.Duration;

/**
 * Lop co so cho moi test class: khoi tao/dong trinh duyet quanh tung test method,
 * cung cap cac thao tac dung chung (mo trang chu, dang nhap tai khoan demo...).
 * Che do headless doc tu -Dheadless=true|false (mac dinh false khi chay tay).
 */
public abstract class BaseTest {

    protected String baseUrl;

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    protected boolean isHeadless() {
        String value = System.getProperty("headless", System.getenv("HEADLESS"));
        return value != null && Boolean.parseBoolean(value);
    }

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browserName) {
        baseUrl = GlobalConstants.getAppUrl();
        DriverManager.setDriver(initBrowser(browserName));
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    protected WebDriver initBrowser(String browserName) {
        if ("firefox".equalsIgnoreCase(browserName)) {
            FirefoxOptions options = new FirefoxOptions();
            if (isHeadless()) {
                options.addArguments("-headless");
            }
            return new FirefoxDriver(options);
        }
        ChromeOptions options = new ChromeOptions();
        if (isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(options);
    }

    /** Mo trang chu cua SUT va tra ve HomePageObject. */
    protected HomePageObject openHomePage() {
        HomePageObject homePage = new HomePageObject();
        homePage.openPageUrl(baseUrl);
        homePage.waitForHomePageLoaded();
        return homePage;
    }

    /** Dang nhap bang tai khoan demo (seed san cua SUT) qua Auth Modal. */
    protected HomePageObject loginWithDemoAccount() {
        HomePageObject homePage = openHomePage();
        AuthModalPageObject authModal = homePage.openLoginModal();
        authModal.loginAs(GlobalConstants.DEMO_EMAIL, GlobalConstants.DEMO_PASSWORD);
        homePage.waitForLoggedIn();
        return homePage;
    }

    /** Sinh email ngau nhien khong trung lap cho cac test dang ky. */
    protected String randomEmail() {
        return "test" + System.currentTimeMillis() + "@dafl.vn";
    }

    /** Cat bo moi dinh dang tien te, chi giu chu so (vd "1.250.000d" -> 1250000). */
    protected long parseMoney(String moneyText) {
        String digits = moneyText.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? 0 : Long.parseLong(digits);
    }
}
