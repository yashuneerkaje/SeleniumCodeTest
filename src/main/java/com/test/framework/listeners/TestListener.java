package com.test.framework.listeners;

import com.test.framework.driver.DriverManager;
import com.test.framework.reporting.ExtentReportManager;
import com.test.framework.utils.BrowserStackHelper;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for tracking test execution and reporting
 */
public class TestListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting test suite: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finishing test suite: {}", context.getName());
        ExtentReportManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getName());
        ExtentReportManager.logPass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getName());
        
        // Capture screenshot on failure
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            try {
                String screenshotBase64 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
                ExtentReportManager.logFail("Test failed: " + result.getThrowable().getMessage(), screenshotBase64);
                
                // Update BrowserStack status if using BrowserStack
                String browserStackRun = result.getTestContext().getCurrentXmlTest()
                        .getParameter("browserStackRun");
                
                if (browserStackRun != null && Boolean.parseBoolean(browserStackRun)) {
                    BrowserStackHelper.markTestStatus("failed", result.getThrowable().getMessage(), driver);
                }
            } catch (Exception e) {
                logger.error("Error taking screenshot", e);
                ExtentReportManager.logFail("Test failed: " + result.getThrowable().getMessage());
            }
        } else {
            ExtentReportManager.logFail("Test failed: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.info("Test skipped: {}", result.getName());
        ExtentReportManager.logSkip("Test skipped: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.info("Test failed but within success percentage: {}", result.getName());
    }
}
