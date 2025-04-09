package com.test.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;

public class HomePage extends RegisterPage {
    public Actions actions;

    // Constructor
    public HomePage(WebDriver driver) {
        super(driver);
        actions = new Actions(driver);
    }

    // Locators
    private By logo = By.cssSelector("img[title=\"Home Page\"]");
    private By searchBox = By.cssSelector("input[name=\"SearchTerm\"]");
    private By productNumber = By.cssSelector("[class=\"product-number\"]");
    private By productPriceSection = By.xpath("//label[text()=\"Price:\"]");
    private By browseByCategoryHeader = By.cssSelector("a[title=\"Browse By Category\"]");
    private By oilAndLubricantsSection = By.cssSelector("[data-testing-id=\"oilandlubricants-link\"]");
    private By getSubCategoryLink(String linkText) {
        return By.xpath("//*[@class='category-level1 dropdown-menu']//a[text()='" + linkText + "']");
    }
    private By productTitle(String productName) {
        return By.xpath("//span[@class='product-title' and normalize-space(text())='" + productName + "']");
    }
    private By productName(String productName) {
        return By.xpath("//span[text()='"+productName+"']");
    }


    private WebElement waitUntilVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitUntilClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }


    // Actions

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


    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public boolean isLogoDisplayed() {
        return waitUntilVisible(logo).isDisplayed();
    }

    public boolean isSearchBoxDisplayed() {
        return waitUntilVisible(searchBox).isDisplayed();
    }

    public boolean isBrowseByCategoryDisplayed() {
        return waitUntilVisible(browseByCategoryHeader).isDisplayed();
    }

    public void hoverOnCategory() {
        WebElement element = waitUntilVisible(browseByCategoryHeader);
        actions.moveToElement(element);
        element.click();
    }

    public boolean isOilAndLubricantOptionsDisplayed() {
        return waitUntilVisible(oilAndLubricantsSection).isDisplayed();
    }

    public void clickOnSubCategoryText(String subCategory) {
        waitUntilVisible(getSubCategoryLink(subCategory)).click();
    }

    public boolean verifyProductIsDisplayed(String productName){
        return waitUntilVisible(productTitle(productName)).isDisplayed();
    }

    public void clickOnProduct(String productName){
        waitUntilVisible(productTitle(productName)).click();
    }

    public boolean verifyProductFinalPageIsLoaded(String productName){
        return waitUntilVisible(productName(productName)).isDisplayed() && waitUntilVisible(productNumber).isDisplayed() && waitUntilVisible(productPriceSection).isDisplayed();
    }

}
