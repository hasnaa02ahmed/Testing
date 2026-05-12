package listeners;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import base.BaseTest;

import java.io.ByteArrayInputStream;

/**
 * TestNG listener that integrates with Allure Reporting.
 *
 * Responsibilities:
 *  - Captures a screenshot on every test FAILURE and attaches it to the Allure report.
 *  - Logs the failure reason as a text attachment.
 *  - Logs test start / pass / skip events into the Allure timeline.
 *
 * Registration: declared in testng.xml via <listeners> tag —
 * no @Listeners annotation needed on individual test classes.
 */
public class AllureListener implements ITestListener {

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Retrieves the WebDriver from the test instance.
     * Works because all tests extend BaseTest which exposes the driver field.
     */
    private WebDriver getDriver(ITestResult result) {
        Object instance = result.getInstance();
        if (instance instanceof BaseTest) {
            return ((BaseTest) instance).driver;
        }
        return null;
    }

    /**
     * Takes a full-page screenshot and attaches it to the current Allure test.
     * Silently skips if the driver is null or the screenshot cannot be taken.
     */
    private void attachScreenshot(ITestResult result) {
        try {
            WebDriver driver = getDriver(result);
            if (driver == null) return;

            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(
                    "Screenshot — " + result.getName(),
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    "png"
            );
        } catch (Exception e) {
            Allure.addAttachment(
                    "Screenshot capture failed",
                    "text/plain",
                    new ByteArrayInputStream(
                            ("Could not capture screenshot: " + e.getMessage()).getBytes()),
                    "txt"
            );
        }
    }

    /**
     * Attaches the exception stack trace as a text file to the Allure report.
     */
    private void attachFailureReason(ITestResult result) {
        Throwable cause = result.getThrowable();
        if (cause == null) return;

        // Build a readable stack trace string
        StringBuilder sb = new StringBuilder();
        sb.append(cause).append("\n\n");
        for (StackTraceElement el : cause.getStackTrace()) {
            sb.append("\tat ").append(el).append("\n");
        }

        Allure.addAttachment(
                "Failure reason",
                "text/plain",
                new ByteArrayInputStream(sb.toString().getBytes()),
                "txt"
        );
    }

    // ── ITestListener callbacks ───────────────────────────────────────────────

    @Override
    public void onTestStart(ITestResult result) {
        Allure.step("▶ Test started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.step("✔ Test PASSED: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        attachFailureReason(result);
        attachScreenshot(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Allure.step("⏭ Test SKIPPED: " + result.getName());
    }

    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) { }
    @Override public void onStart(ITestContext context) { }
    @Override public void onFinish(ITestContext context) { }
}