package com.test.framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager for Extent Reports
 */
public class ExtentReportManager {
    private static final Logger logger = LoggerFactory.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static final Map<Long, ExtentTest> testMap = new HashMap<>();
    private static final String REPORT_PATH = "test-output/reports/";
    
    /**
     * Initializes the Extent Reports instance
     */
    public static synchronized void initReports() {
        if (extentReports == null) {
            // Create report directory if it doesn't exist
            File directory = new File(REPORT_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportName = REPORT_PATH + "TestReport_" + timestamp + ".html";
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportName);
            sparkReporter.config().setDocumentTitle("Test Automation Report");
            sparkReporter.config().setReportName("Selenium BrowserStack Test Report");
            sparkReporter.config().setTheme(Theme.STANDARD);
            
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("User", System.getProperty("user.name"));
            
            logger.info("Extent Reports initialized at: {}", reportName);
        }
    }
    
    /**
     * Starts a new test in the report
     * 
     * @param testName name of the test
     * @param testDescription description of the test
     */
    public static synchronized void startTest(String testName, String testDescription) {
        if (extentReports == null) {
            initReports();
        }
        
        ExtentTest test = extentReports.createTest(testName, testDescription);
        testMap.put(Thread.currentThread().getId(), test);
        logger.info("Started test: {}", testName);
    }
    
    /**
     * Gets the current test instance
     * 
     * @return the ExtentTest instance for the current thread
     */
    private static synchronized ExtentTest getTest() {
        return testMap.get(Thread.currentThread().getId());
    }
    
    /**
     * Logs a pass step in the report
     * 
     * @param message the message to log
     */
    public static void logPass(String message) {
        getTest().pass(message);
        logger.info("PASS: {}", message);
    }
    
    /**
     * Logs a fail step in the report
     * 
     * @param message the message to log
     */
    public static void logFail(String message) {
        getTest().fail(message);
        logger.error("FAIL: {}", message);
    }
    
    /**
     * Logs a fail step in the report with a screenshot
     * 
     * @param message the message to log
     * @param base64Screenshot the screenshot as a base64 string
     */
    public static void logFail(String message, String base64Screenshot) {
        getTest().fail(message, 
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        logger.error("FAIL: {} (screenshot captured)", message);
    }
    
    /**
     * Logs an info step in the report
     * 
     * @param message the message to log
     */
    public static void logInfo(String message) {
        getTest().info(message);
        logger.info("INFO: {}", message);
    }
    
    /**
     * Logs a skip step in the report
     * 
     * @param message the message to log
     */
    public static void logSkip(String message) {
        getTest().skip(message);
        logger.info("SKIP: {}", message);
    }
    
    /**
     * Ends the current test
     */
    public static synchronized void endTest() {
        if (extentReports != null) {
            extentReports.flush();
        }
        testMap.remove(Thread.currentThread().getId());
    }
    
    /**
     * Flushes the reports to disk
     */
    public static synchronized void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
