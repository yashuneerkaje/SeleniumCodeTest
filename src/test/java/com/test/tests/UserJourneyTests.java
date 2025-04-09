package com.test.tests;

import com.test.framework.base.BaseTest;
import com.test.framework.config.ConfigManager;
import com.test.framework.reporting.ExtentReportManager;
import com.test.pages.HomePage;
import com.test.pages.RegisterPage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserJourneyTests extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(UserJourneyTests.class);
    private static final String DEMO_URL = "https://www.shophighlinewarren.com/login";

    private RegisterPage registerPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupTest() {
        String username = ConfigManager.getBrowserStackProperty("username");
        String accessKey = ConfigManager.getBrowserStackProperty("access_key");

        registerPage = new RegisterPage(driver);
        homePage = new HomePage(driver);

        // Set a flag to determine if we're running in local or remote mode
        String runLocalStr = ConfigManager.getProperty("execution.local"); // Only one parameter
        boolean runLocal = runLocalStr == null || Boolean.parseBoolean(runLocalStr);
        isRemoteExecution = !runLocal;

        if (isRemoteExecution && (isEmpty(username) || isEmpty(accessKey))) {
            logger.warn("Skipping test: BrowserStack credentials not provided");
            throw new SkipException("BrowserStack credentials missing. Set browserstack.username and browserstack.accessKey in browserstack.properties");
        }

        driver.get(DEMO_URL);
        // Navigate
        ExtentReportManager.logInfo("Navigated to site");
    }


    @Test
    public void validateHomePageElements() {
        logger.info("Starting Home Page Elements Validation Test");
        ExtentReportManager.logInfo("Starting Home Page Elements Validation Test");

        try {
            // Step 1: Validate Logo is displayed
            Assert.assertTrue(homePage.isLogoDisplayed(), "Home page logo is not displayed.");

            // Step 2: Validate Search Box is displayed
            Assert.assertTrue(homePage.isSearchBoxDisplayed(), "Search box is not displayed on the home page.");

            // Step 3: Validate 'Browse By Category' section is displayed
            Assert.assertTrue(homePage.isBrowseByCategoryDisplayed(), "'Browse By Category' section is not visible on the home page.");

            logger.info("Home Page Elements Validation Test completed successfully.");
            ExtentReportManager.logPass("Home Page Elements Validation Test completed successfully.");

        } catch (Exception e) {
            handleTestFailure(e);
        }
    }


    @Test
    public void validateBrowseByCategoryAndProductFlow() {
        logger.info("Starting 'Browse By Category' and Product Navigation Test");
        ExtentReportManager.logInfo("Starting 'Browse By Category' and Product Navigation Test");

        try {
            // Test Data
            String subCategory = "Bar & Chain";
            String expectedProductName = "Prime Guard Bar & Chain Oil - Gallon";
            String expectedPartialUrl = "oil-bar-chain";

            // Step 1: Close any popup
            homePage.closeDialogPopup();

            // Step 2: Hover on main category
            homePage.hoverOnCategory();
            Assert.assertTrue(homePage.isOilAndLubricantOptionsDisplayed(), "Oil & Lubricants section not displayed after hover.");

            // Step 3: Click on Sub-category
            homePage.clickOnSubCategoryText(subCategory);
            Assert.assertTrue(driver.getCurrentUrl().contains(expectedPartialUrl), "Page navigation to 'Bar & Chain' category failed.");

            // Step 4: Verify Product listing
            Assert.assertTrue(homePage.verifyProductIsDisplayed(expectedProductName), "Expected product is not listed on the page.");

            // Step 5: Navigate to Product Details Page
            homePage.clickOnProduct(expectedProductName);

            // Step 6: Verify Product Details Page Loaded
            Assert.assertTrue(homePage.verifyProductFinalPageIsLoaded(expectedProductName), "Product Details page did not load correctly.");

            logger.info("'Browse By Category' and Product Navigation Test completed successfully.");
            ExtentReportManager.logPass("'Browse By Category' and Product Navigation Test completed successfully.");

        } catch (Exception e) {
            handleTestFailure(e);
        }
    }


    @Test
    public void verifyRegisterFunctionality() {
        logger.info("Starting register functionality test");
        ExtentReportManager.logInfo("Starting register functionality test");

        String emailId = generateUniqueEmail();

        try {
            // Interact with Register Page
            registerPage.closeDialogPopup();
            registerPage.clickRegisterButton();
            registerPage.scrollToBottom();

            // Fill Personal Details
            registerPage.enterFirstName("John");
            registerPage.enterLastName("Doe");
            registerPage.enterAddressLine1("1234 Elm Street");
            registerPage.enterAddressLine2("Suite 567");
            registerPage.enterCity("New York");
            registerPage.selectState("New York");
            registerPage.enterZip("10001");

            // Fill Contact Details
            registerPage.enterPrimaryPhoneNumber("2125551234");
            registerPage.enterMobileNumber("9175556789");
            registerPage.enterEmailId(emailId);

            // Set Credentials
            registerPage.enterPassword("SecurePass@123");
            registerPage.enterConfirmPassword("SecurePass@123");

            // Preferences
            registerPage.clickEmailCheckbox();
            registerPage.clickContactByRepCheckbox();

            // Company Info
            registerPage.enterCompanyName("Doe Technologies Inc.");
            registerPage.enterCompanyAccountNumber("AC123456789");

            // Additional Info
            registerPage.clickSameAsRegisteredCheckbox();
            registerPage.scrollToBottom();
            registerPage.enterComments("Looking forward to work with your team!");

            // Agree Terms and Submit
            registerPage.clickAgreeCheckbox();
            registerPage.clickCreateAccountBtn();

            // Assertion
            Assert.assertTrue(registerPage.isPendingValidationMessageDisplayed(), "Pending validation message not displayed!");

        } catch (Exception e) {
            handleTestFailure(e);
        }
    }

    // ---------- Private Helper Methods ----------

    private String generateUniqueEmail() {
        return "user" + System.currentTimeMillis() + "@example.com";
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }


}
