package com.test.framework.driver;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe manager for WebDriver instances
 */
public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    
    // ThreadLocal to handle multiple threads in parallel execution
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    /**
     * Gets the current thread's WebDriver instance, or creates a new one if it doesn't exist
     * 
     * @param browser browser to use
     * @param useBrowserStack whether to use BrowserStack
     * @return the WebDriver instance
     */
    public static WebDriver getDriver(String browser, boolean useBrowserStack) {
        if (driverThreadLocal.get() == null) {
            logger.info("Creating new driver instance for thread: {}", Thread.currentThread().getId());
            WebDriver driver = DriverFactory.createDriver(browser, useBrowserStack);
            driverThreadLocal.set(driver);
        }
        return driverThreadLocal.get();
    }
    
    /**
     * Gets the current thread's WebDriver instance
     * 
     * @return the WebDriver instance or null if not initialized
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    /**
     * Quits the WebDriver and removes it from the ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            logger.info("Quitting driver instance for thread: {}", Thread.currentThread().getId());
            try {
                driver.quit();
            } catch (Exception e) {
                logger.error("Error quitting driver", e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
