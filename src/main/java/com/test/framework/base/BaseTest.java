package com.test.framework.base;

import com.test.framework.config.ConfigManager;
import com.test.framework.driver.DriverManager;
import com.test.framework.reporting.ExtentReportManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;

/**
 * Base test class that all test classes should extend.
 * Handles the WebDriver setup, configuration and cleanup.
 */
public class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;

    @BeforeSuite
    public void beforeSuite() {
        ExtentReportManager.initReports();
    }

    /**
     * Setup method that runs before each test method.
     * Initializes the WebDriver instance based on the browser parameter.
     * 
     * @param browser browser to use for the test
     * @param method test method to execute
     */
    @BeforeMethod
    @Parameters({"browser", "browserStackRun"})
    public void setup(@Optional("chrome") String browser, @Optional("false") String browserStackRun, Method method) {
        logger.info("Setting up WebDriver for test: {}", method.getName());
        boolean useBrowserStack = Boolean.parseBoolean(browserStackRun);
        
        ExtentReportManager.startTest(method.getName(), method.getDeclaringClass().getName());
        
        if (useBrowserStack) {
            logger.info("Using BrowserStack for test execution with browser: {}", browser);
        } else {
            logger.info("Using local WebDriver for test execution with browser: {}", browser);
        }
        
        driver = DriverManager.getDriver(browser, useBrowserStack);
        driver.manage().window().maximize();
    }

    /**
     * Teardown method that runs after each test method.
     * Quits the WebDriver instance and performs cleanup.
     */
    @AfterMethod
    public void tearDown() {
        logger.info("Tearing down WebDriver after test");
        if (driver != null) {
            DriverManager.quitDriver();
        }
        ExtentReportManager.endTest();
    }
}
