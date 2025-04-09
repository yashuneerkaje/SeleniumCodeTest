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
 * Factory class to create WebDriver instances based on browser type.
 */
public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);

    /**
     * Creates a new WebDriver instance.
     *
     * @param browser          the browser to create
     * @param useBrowserStack  whether to use BrowserStack
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
     * Creates a local WebDriver instance.
     *
     * @param browser the browser to create
     * @return the WebDriver instance
     */
    private static WebDriver createLocalDriver(String browser) {
        WebDriver driver;
        boolean isHeadless = Boolean.parseBoolean(ConfigManager.getProperty("headless"));

        switch (browser.toLowerCase()) {
            case "chrome":
                logger.info("Initializing ChromeDriver");
                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions = new ChromeOptions();
                if (isHeadless) {
                    logger.info("Configuring Chrome for headless execution");
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                logger.info("Initializing FirefoxDriver");
                WebDriverManager.firefoxdriver().setup();

                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (isHeadless) {
                    logger.info("Configuring Firefox for headless execution");
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                logger.info("Initializing EdgeDriver");
                WebDriverManager.edgedriver().setup();

                EdgeOptions edgeOptions = new EdgeOptions();
                if (isHeadless) {
                    logger.info("Configuring Edge for headless execution");
                    edgeOptions.addArguments("--headless");
                    edgeOptions.addArguments("--window-size=1920,1080");
                }
                driver = new EdgeDriver(edgeOptions);
                break;

            case "safari":
                logger.info("Initializing SafariDriver");
                // Note: Safari does not support headless mode
                driver = new SafariDriver(new SafariOptions());
                break;

            default:
                logger.warn("Browser [{}] not recognized. Defaulting to ChromeDriver.", browser);
                WebDriverManager.chromedriver().setup();

                ChromeOptions defaultOptions = new ChromeOptions();
                if (isHeadless) {
                    logger.info("Configuring default Chrome for headless execution");
                    defaultOptions.addArguments("--headless=new");
                    defaultOptions.addArguments("--no-sandbox");
                    defaultOptions.addArguments("--disable-dev-shm-usage");
                    defaultOptions.addArguments("--window-size=1920,1080");
                }
                driver = new ChromeDriver(defaultOptions);
        }

        return driver;
    }

    /**
     * Creates a BrowserStack WebDriver instance.
     *
     * @param browser the browser to create
     * @return the BrowserStack WebDriver instance
     */
    private static WebDriver createBrowserStackDriver(String browser) {
        logger.info("Creating BrowserStack WebDriver instance for browser: {}", browser);
        return BrowserStackHelper.createDriver(browser);
    }
}
