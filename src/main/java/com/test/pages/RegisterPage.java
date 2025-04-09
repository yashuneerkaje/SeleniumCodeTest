package com.test.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // Constructor
    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Locators
    public By closeDialogPopup = By.cssSelector("[aria-label=\"Close dialog\"]");
    public By footerDialogPopupClose = By.cssSelector("[class*=\"dialog__close\"]");
    public By registrationBtn = By.cssSelector("#registerButton");
    public By statusMessage = By.cssSelector("[class=\"regist-pending-validate\"] > h1");
    public By firstNameField = By.cssSelector("[name=\"AddressForm_FirstName\"]");
    public By lastNameField = By.cssSelector("[name=\"AddressForm_LastName\"]");
    public By addressLine1 = By.cssSelector("[name=\"AddressForm_Address1\"]");
    public By addressLine2 = By.cssSelector("[name=\"AddressForm_Address2\"]");
    public By city = By.cssSelector("[name=\"AddressForm_City\"]");
    public By stateDropdown = By.cssSelector("[name=\"AddressForm_State\"]");
    public By zip = By.cssSelector("[name=\"AddressForm_PostalCode\"]");
    public By primaryPhoneNumber = By.cssSelector("[name=\"AddressForm_PhoneHome\"]");
    public By mobileNumber = By.cssSelector("[name=\"AddressForm_Mobile\"]");
    public By emailId = By.cssSelector("[name=\"RegisterUserFullEmail_Login\"]");
    public By password = By.cssSelector("[name=\"RegisterUserFullEmail_Password\"]");
    public By confirmPassword = By.cssSelector("[name=\"RegisterUserFullEmail_PasswordConfirmation\"]");
    public By emailCheckbox = By.cssSelector("[for=\"RegisterUserFullEmail_EmailCheckbox\"]");
    public By contactByRepCheckbox = By.cssSelector("[for=\"RegisterUserFullEmail_YesCheckbox\"]");
    public By companyName = By.cssSelector("[name=\"CompanyInformationForm_CompanyName\"]");
    public By companyAccNumber = By.id("CompanyInformationForm_AccountNumber");
    public By sameAsRegisteredCheckbox = By.cssSelector("[for=\"RegisterUserFullEmail_CopyCheckbox\"]");
    public By comments = By.id("CompanyInformationForm_Comments");
    public By agreeCheckbox = By.id("CompanyInformationForm_AgreeCheckbox");
    public By createAccountBtn = By.cssSelector("[name=\"CreateAccount\"]");

    private WebElement waitUntilVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitUntilClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }


    // Actions
// Method to click "Register" button after filling form
    public void clickRegisterButton() {
        waitUntilClickable(registrationBtn).click();
    }

    public void closeDialogPopup() {
        try {
            WebElement popupCloseButton = waitUntilVisible(closeDialogPopup); // Wait max 5 seconds
            if (popupCloseButton.isDisplayed()) {
                popupCloseButton.click();
            }
        } catch (TimeoutException e) {
            // Popup not found, nothing to close
        }
        try {
            WebElement popupCloseButton = waitUntilVisible(footerDialogPopupClose); // Wait max 5 seconds
            if (popupCloseButton.isDisplayed()) {
                popupCloseButton.click();
            }
        } catch (TimeoutException e) {
            // Popup not found, nothing to close
        }
    }

    // Enter First Name
    public void enterFirstName(String firstName) {
        waitUntilVisible(firstNameField).sendKeys(firstName);
    }

    // Enter Last Name
    public void enterLastName(String lastName) {
        waitUntilVisible(lastNameField).sendKeys(lastName);
    }

    // Enter Address Line 1
    public void enterAddressLine1(String address1) {
        waitUntilVisible(addressLine1).sendKeys(address1);
    }

    // Enter Address Line 2
    public void enterAddressLine2(String address2) {
        waitUntilVisible(addressLine2).sendKeys(address2);
    }

    // Enter City
    public void enterCity(String cityName) {
        waitUntilVisible(city).sendKeys(cityName);
    }

    // Select State from dropdown
    public void selectState(String stateName) {
        Select select = new Select(waitUntilVisible(stateDropdown));
        select.selectByVisibleText(stateName);
    }

    // Enter ZIP Code
    public void enterZip(String zipCode) {
        waitUntilVisible(zip).sendKeys(zipCode);
    }

    // Enter Primary Phone Number
    public void enterPrimaryPhoneNumber(String phoneNumber) {
        waitUntilVisible(primaryPhoneNumber).sendKeys(phoneNumber);
    }

    // Enter Mobile Number
    public void enterMobileNumber(String mobileNum) {
        waitUntilVisible(mobileNumber).sendKeys(mobileNum);
    }

    // Enter Email ID
    public void enterEmailId(String email) {
        waitUntilVisible(emailId).sendKeys(email);
    }

    // Enter Password
    public void enterPassword(String pwd) {
        waitUntilVisible(password).sendKeys(pwd);
    }

    // Enter Confirm Password
    public void enterConfirmPassword(String confirmPwd) {
        waitUntilVisible(confirmPassword).sendKeys(confirmPwd);
    }

    // Click on Email Checkbox
    public void clickEmailCheckbox() {
        waitUntilClickable(emailCheckbox).click();
    }

    // Click on Contact By Representative Checkbox
    public void clickContactByRepCheckbox() {
        waitUntilClickable(contactByRepCheckbox).click();
    }

    // Enter Company Name
    public void enterCompanyName(String company) {
        WebElement element = driver.findElement(companyName);
        scrollIntoView(element);
        waitUntilVisible(companyName).sendKeys(company);
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    // Enter Company Account Number
    public void enterCompanyAccountNumber(String accountNumber) {
        waitUntilVisible(companyAccNumber).sendKeys(accountNumber);
    }

    // Click Same As Registered Checkbox
    public void clickSameAsRegisteredCheckbox() {
        waitUntilClickable(sameAsRegisteredCheckbox).click();
    }

    // Enter Comments
    public void enterComments(String comment) {
        waitUntilVisible(comments).sendKeys(comment);
    }

    // Click Agree Checkbox
    public void clickAgreeCheckbox() {
        waitUntilClickable(agreeCheckbox).click();
    }
    public void clickCreateAccountBtn() {
        waitUntilClickable(createAccountBtn).click();
    }

    public boolean isPendingValidationMessageDisplayed() {
        WebElement pendingMessage = waitUntilVisible(statusMessage);
        return pendingMessage.isDisplayed() && pendingMessage.getText().equalsIgnoreCase("Your Account is Pending Validation");
    }


    // Scroll down by pixels
    public void scrollDown(int pixels) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + pixels + ")");
    }

    // Scroll up by pixels
    public void scrollUp(int pixels) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-" + pixels + ")");
    }

    // Scroll to bottom of page
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    // Scroll to top of page
    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
    }
}
