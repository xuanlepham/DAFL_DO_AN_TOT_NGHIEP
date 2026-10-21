package vn.dafl.reportConfig;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import vn.dafl.driver.DriverManager;

import java.io.ByteArrayInputStream;

/**
 * Listener cua TestNG: khi test FAIL, chup man hinh trinh duyet va dinh kem
 * vao bao cao Allure de phuc vu viec xem lai loi trong pipeline.
 */
public class AllureTestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = DriverManager.getDriver();
        if (driver instanceof TakesScreenshot) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot - " + result.getMethod().getMethodName(),
                    "image/png", new ByteArrayInputStream(screenshot), ".png");
        }
        Throwable error = result.getThrowable();
        if (error != null) {
            Allure.addAttachment("Chi tiet loi", "text/plain",
                    new ByteArrayInputStream(String.valueOf(error).getBytes()), ".txt");
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }

    @Override
    public void onTestSkipped(ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onStart(org.testng.ITestContext context) {
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {
    }
}
