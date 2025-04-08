package com.test.tests;

import com.test.framework.base.BaseTest;
import com.test.framework.reporting.ExtentReportManager;
import com.test.framework.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Sample test class for Google search functionality
 */
public class GoogleSearchTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(GoogleSearchTest.class);
    
    /**
     * Simple test to search on Google and verify results appear
     */
    @Test
    public void testGoogleSearch() {
        logger.info("Starting Google search test");
        ExtentReportManager.logInfo("Starting Google search test");
        
        try {
            // Open Google
            driver.get(Constants.GOOGLE_URL);
            ExtentReportManager.logInfo("Opened Google homepage");
            
            // Accept cookies if the dialog appears (this varies by region)
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(., 'Accept all') or contains(., 'I agree') or contains(., 'Accept')]")));
                acceptButton.click();
                ExtentReportManager.logInfo("Accepted cookies");
            } catch (Exception e) {
                // Cookie banner might not appear, so we can ignore this exception
                logger.info("Cookie consent banner did not appear or could not be handled: {}", e.getMessage());
            }
            
            // Find the search box
            WebElement searchBox = driver.findElement(By.name("q"));
            ExtentReportManager.logInfo("Found search box");
            
            // Enter search query and submit
            String searchQuery = "Selenium BrowserStack automation";
            searchBox.sendKeys(searchQuery);
            ExtentReportManager.logInfo("Entered search query: " + searchQuery);
            searchBox.sendKeys(Keys.RETURN);
            ExtentReportManager.logInfo("Submitted search query");
            
            // Wait for page title to change, which indicates search results loaded
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_TIMEOUT));
            wait.until(ExpectedConditions.titleContains("Selenium"));
            
            ExtentReportManager.logInfo("Search results loaded");
            
            // Verify search results contain our query by checking page title
            String pageTitle = driver.getTitle().toLowerCase();
            ExtentReportManager.logInfo("Page title: " + pageTitle);
            
            Assert.assertTrue(pageTitle.contains("selenium") || pageTitle.contains("browserstack"), 
                    "Page title should contain search terms");
            
            ExtentReportManager.logPass("Successfully verified Google search results");
            logger.info("Google search test completed successfully");
            
        } catch (Exception e) {
            logger.error("Error in Google search test", e);
            ExtentReportManager.logFail("Test failed: " + e.getMessage());
            Assert.fail("Google search test failed: " + e.getMessage());
        }
    }
}
