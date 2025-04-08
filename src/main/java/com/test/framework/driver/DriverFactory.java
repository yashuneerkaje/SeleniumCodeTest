package com.test.framework.driver;

import com.test.framework.config.ConfigManager;
import com.test.framework.utils.BrowserStackHelper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class to create WebDriver instances based on browser type
 */
public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    
    // Replit paths for Chromium and ChromeDriver
    private static final String CHROMIUM_PATH = "/nix/store/zi4f80l169xlmivz8vja8wlphq74qqk0-chromium-125.0.6422.141/bin/chromium";
    private static final String CHROMEDRIVER_PATH = "/nix/store/3qnxr5x6gw3k9a9i7d0akz0m6bksbwff-chromedriver-125.0.6422.141/bin/chromedriver";
    
    /**
     * Creates a new WebDriver instance
     * 
     * @param browser the browser to create
     * @param useBrowserStack whether to use BrowserStack
     * @return the WebDriver instance
     */
    public static WebDriver createDriver(String browser, boolean useBrowserStack) {
        if (useBrowserStack) {
            return createBrowserStackDriver(browser);
        } else {
            return createLocalDriver(browser);
        }
    }
    
    /**
     * Creates a local WebDriver instance
     * 
     * @param browser the browser to create
     * @return the WebDriver instance
     */
    private static WebDriver createLocalDriver(String browser) {
        WebDriver driver;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                logger.info("Initializing Chrome driver");
                
                // Set Chrome binary location for Replit
                System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_PATH);
                
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setBinary(CHROMIUM_PATH);
                
                // Add Chrome specific options
                if (Boolean.parseBoolean(ConfigManager.getProperty("headless"))) {
                    logger.info("Configuring Chrome for headless execution");
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                logger.info("Initializing Firefox driver");
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                
                // Add Firefox specific options
                if (Boolean.parseBoolean(ConfigManager.getProperty("headless"))) {
                    logger.info("Configuring Firefox for headless execution");
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                }
                
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                logger.info("Initializing Edge driver");
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                
                // Add Edge specific options
                if (Boolean.parseBoolean(ConfigManager.getProperty("headless"))) {
                    logger.info("Configuring Edge for headless execution");
                    edgeOptions.addArguments("--headless");
                }
                
                driver = new EdgeDriver(edgeOptions);
                break;
                
            case "safari":
                logger.info("Initializing Safari driver");
                // Safari does not support headless mode
                driver = new SafariDriver(new SafariOptions());
                break;
                
            default:
                logger.info("Browser not specified or recognized, defaulting to Chrome");
                
                // Set Chrome binary location for Replit
                System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_PATH);
                
                ChromeOptions defaultOptions = new ChromeOptions();
                defaultOptions.setBinary(CHROMIUM_PATH);
                
                if (Boolean.parseBoolean(ConfigManager.getProperty("headless"))) {
                    logger.info("Configuring default Chrome for headless execution");
                    defaultOptions.addArguments("--headless=new");
                    defaultOptions.addArguments("--no-sandbox");
                    defaultOptions.addArguments("--disable-dev-shm-usage");
                }
                
                driver = new ChromeDriver(defaultOptions);
        }
        
        return driver;
    }
    
    /**
     * Creates a BrowserStack WebDriver instance
     * 
     * @param browser the browser to create on BrowserStack
     * @return the WebDriver instance
     */
    private static WebDriver createBrowserStackDriver(String browser) {
        logger.info("Creating BrowserStack WebDriver instance for browser: {}", browser);
        return BrowserStackHelper.createDriver(browser);
    }
}
