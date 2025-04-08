package com.test.framework.utils;

import com.test.framework.config.ConfigManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for BrowserStack integration
 */
public class BrowserStackHelper {
    private static final Logger logger = LoggerFactory.getLogger(BrowserStackHelper.class);
    
    /**
     * Creates a WebDriver instance configured to run on BrowserStack
     * 
     * @param browser the browser to run
     * @return the WebDriver instance
     */
    public static WebDriver createDriver(String browser) {
        MutableCapabilities capabilities = new MutableCapabilities();
        
        // Set browser capabilities
        Map<String, Object> browserstackOptions = new HashMap<>();
        
        // Set credentials
        browserstackOptions.put("userName", getBrowserStackUsername());
        browserstackOptions.put("accessKey", getBrowserStackAccessKey());
        
        // Set project and build details
        String projectName = ConfigManager.getBrowserStackProperty("project");
        String buildName = ConfigManager.getBrowserStackProperty("build");
        
        if (projectName != null && !projectName.isEmpty()) {
            browserstackOptions.put("projectName", projectName);
        } else {
            browserstackOptions.put("projectName", "Selenium BrowserStack Project");
        }
        
        if (buildName != null && !buildName.isEmpty()) {
            browserstackOptions.put("buildName", buildName);
        } else {
            browserstackOptions.put("buildName", "Build " + System.currentTimeMillis());
        }
        
        // Set session name based on browser
        browserstackOptions.put("sessionName", "Test on " + browser);
        
        // Set operating system and browser details
        // This could be expanded with more options based on the browserstack.properties
        browserstackOptions.put("os", ConfigManager.getBrowserStackProperty("os") != null ? 
                ConfigManager.getBrowserStackProperty("os") : "Windows");
        browserstackOptions.put("osVersion", ConfigManager.getBrowserStackProperty("os_version") != null ? 
                ConfigManager.getBrowserStackProperty("os_version") : "10");
        
        // Set browser and version
        browserstackOptions.put("browserName", browser);
        browserstackOptions.put("browserVersion", ConfigManager.getBrowserStackProperty("browser_version") != null ? 
                ConfigManager.getBrowserStackProperty("browser_version") : "latest");
        
        // Debug options
        browserstackOptions.put("debug", true);
        browserstackOptions.put("networkLogs", true);
        browserstackOptions.put("consoleLogs", "info");
        
        capabilities.setCapability("bstack:options", browserstackOptions);
        
        // Create the remote WebDriver
        try {
            logger.info("Creating BrowserStack WebDriver with capabilities: {}", capabilities);
            return new RemoteWebDriver(new URL(getBrowserStackURL()), capabilities);
        } catch (MalformedURLException e) {
            logger.error("Error creating BrowserStack WebDriver", e);
            throw new RuntimeException("Could not create BrowserStack driver", e);
        }
    }
    
    /**
     * Updates the status of a test in BrowserStack
     * 
     * @param status the test status ("passed" or "failed")
     * @param reason the reason for the status
     * @param driver the WebDriver instance
     */
    public static void markTestStatus(String status, String reason, WebDriver driver) {
        if (driver != null) {
            try {
                JavascriptExecutor jse = (JavascriptExecutor) driver;
                jse.executeScript(
                    "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"" 
                        + status + "\", \"reason\": \"" + reason + "\"}}");
                logger.info("Marked BrowserStack test as {}: {}", status, reason);
            } catch (Exception e) {
                logger.error("Error marking BrowserStack test status", e);
            }
        }
    }
    
    /**
     * Gets the BrowserStack username from configuration
     * 
     * @return the BrowserStack username
     */
    private static String getBrowserStackUsername() {
        String username = ConfigManager.getBrowserStackProperty("username");
        if (username == null || username.isEmpty()) {
            logger.warn("BrowserStack username not found in configuration. Using environment variable BROWSERSTACK_USERNAME");
            username = System.getenv("BROWSERSTACK_USERNAME");
        }
        return username;
    }
    
    /**
     * Gets the BrowserStack access key from configuration
     * 
     * @return the BrowserStack access key
     */
    private static String getBrowserStackAccessKey() {
        String accessKey = ConfigManager.getBrowserStackProperty("access_key");
        if (accessKey == null || accessKey.isEmpty()) {
            logger.warn("BrowserStack access key not found in configuration. Using environment variable BROWSERSTACK_ACCESS_KEY");
            accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        }
        return accessKey;
    }
    
    /**
     * Gets the BrowserStack hub URL
     * 
     * @return the BrowserStack hub URL
     */
    private static String getBrowserStackURL() {
        return "https://hub-cloud.browserstack.com/wd/hub";
    }
}
