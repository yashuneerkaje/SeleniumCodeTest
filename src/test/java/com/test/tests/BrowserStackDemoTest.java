package com.test.tests;

import com.test.framework.base.BaseTest;
import com.test.framework.config.ConfigManager;
import com.test.framework.reporting.ExtentReportManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Sample test for BrowserStack demo site
 * This test demonstrates BrowserStack integration
 */
public class BrowserStackDemoTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BrowserStackDemoTest.class);
    private static final String DEMO_URL = "https://bstackdemo.com/";
    private boolean isRemoteExecution = true;
    
    @BeforeMethod
    public void checkBrowserStackCredentials() {
        String username = ConfigManager.getBrowserStackProperty("username");
        String accessKey = ConfigManager.getBrowserStackProperty("access_key");
        
        // Set a flag to determine if we're running in local or remote mode
        String runLocalStr = ConfigManager.getProperty("execution.local");
        boolean runLocal = runLocalStr == null || Boolean.parseBoolean(runLocalStr);
        isRemoteExecution = !runLocal;
        
        // Skip test if running in BrowserStack mode without credentials
        if (isRemoteExecution && (username == null || username.isEmpty() || accessKey == null || accessKey.isEmpty())) {
            logger.warn("Skipping BrowserStack test because credentials are not provided");
            throw new SkipException("BrowserStack credentials not provided. Please set browserstack.username and browserstack.accessKey in browserstack.properties");
        }
    }
    
    /**
     * Test to verify product search on BrowserStack demo site
     */
    @Test
    public void testProductSearch() {
        logger.info("Starting BrowserStack demo test");
        ExtentReportManager.logInfo("Starting BrowserStack demo test");
        
        try {
            // Navigate to BrowserStack demo site
            driver.get(DEMO_URL);
            ExtentReportManager.logInfo("Navigated to BrowserStack demo site");
            
            // Wait for products to load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".shelf-item")));
            
            // Get initial product count
            int initialCount = driver.findElements(By.cssSelector(".shelf-item")).size();
            ExtentReportManager.logInfo("Initial product count: " + initialCount);
            
            // Filter by vendor (Samsung)
            WebElement vendorFilter = driver.findElement(By.xpath("//span[text()='Samsung']"));
            vendorFilter.click();
            ExtentReportManager.logInfo("Filtered by Samsung");
            
            // Wait for filter to apply
            Thread.sleep(1000);
            
            // Get filtered count
            int filteredCount = driver.findElements(By.cssSelector(".shelf-item")).size();
            ExtentReportManager.logInfo("Filtered product count: " + filteredCount);
            
            // Verify filtered count is less than initial
            Assert.assertTrue(filteredCount < initialCount, "Filtered count should be less than initial count");
            
            // Mark test as passed in BrowserStack
            if (isRemoteExecution) {
                ((JavascriptExecutor) driver).executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Product filter working!\"}}");
            }
            
            ExtentReportManager.logPass("Product filtering test passed");
            logger.info("BrowserStack demo test completed successfully");
            
        } catch (Exception e) {
            logger.error("Error in BrowserStack demo test", e);
            ExtentReportManager.logFail("Test failed: " + e.getMessage());
            
            // Mark test as failed in BrowserStack
            if (isRemoteExecution) {
                ((JavascriptExecutor) driver).executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"" + e.getMessage() + "\"}}");
            }
            
            Assert.fail("BrowserStack demo test failed: " + e.getMessage());
        }
    }
}
