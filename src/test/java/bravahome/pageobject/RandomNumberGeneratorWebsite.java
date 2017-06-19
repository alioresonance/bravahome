/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.pageobject;

import org.testng.log4testng.Logger;

import bravahome.datamodel.Range;
import bravahome.framework.Log;
import bravahome.common.util.Utils;
import bravahome.framework.PageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.anyOf;

/**
 * Returns PageObject implementation representing the Random Number Generator Website.
 */
public class RandomNumberGeneratorWebsite extends PageObject {

    public static Logger DEV_NULL = Log.getLogger(RandomNumberGeneratorWebsite.class);

    public RandomNumberGeneratorWebsite() {
        super.init();
    }

    /* -------------------------------------------------------------------------------------------- */
    /* locator                                                                                      */
    /* -------------------------------------------------------------------------------------------- */
    public By byRandomNumberLabelText = By.cssSelector("body > div > div > h3");
    public By byRandomNumberText      = By.cssSelector("#random_number");
    public By byRefreshButton         = By.xpath("//button[text()='Refresh!']");

    /* -------------------------------------------------------------------------------------------- */
    /* element                                                                                      */
    /* -------------------------------------------------------------------------------------------- */
    public WebElement textRandomNumberLabel() {
        return findElement(byRandomNumberLabelText);
    }

    public WebElement textRandomNumber() {
        return findElement(byRandomNumberText);
    }

    public WebElement buttonRefresh() {
        return findElement(byRefreshButton);
    }

    /* -------------------------------------------------------------------------------------------- */
    /* keyword                                                                                      */
    /* -------------------------------------------------------------------------------------------- */
    public String getRandomNumberLabel() {
        Log.info("Getting random number label...");
        return textRandomNumberLabel().getText().split(":")[0];
    }

    public int getRandomNumber() {
        int randomDisplayed = Integer.valueOf(textRandomNumber().getText());
        Log.info("Random number displayed: " + randomDisplayed);
        return randomDisplayed;
    }

    public Range getRangeFromPage() {
        Log.info("Getting the range of acceptable numbers...");
        String label = getRandomNumberLabel();
        Matcher matcher = Pattern.compile("-?\\d+").matcher(label);
        return new Range(matcher);
    }

    public RandomNumberGeneratorWebsite clickRefresh() {
        Log.info("Clicking Refresh button...");
        buttonRefresh().click();
        return this;
    }

    /* -------------------------------------------------------------------------------------------- */
    /* super keyword                                                                                */
    /* -------------------------------------------------------------------------------------------- */
    public RandomNumberGeneratorWebsite openBrowser() {
        Log.info(">>>>>>>>>>>>>>>>>>>");
        Log.info(">>>> Opening Random Number Generator Website...");
        //Log.info(">>>> Opening " + getSubclassName());
        if (driver == null) {
            super.init();
        }

        final String HOME = getUrl();
        Log.info("Going to " + HOME + "...");
        driver.get(HOME);

        // check home page loaded
        verifyOnHomePage();
        return this;
    }

    public RandomNumberGeneratorWebsite closeBrowser() {
        Log.info(">>>> Closing Random Number Generator Website...");
        Log.info(">>>>>>>>>>>>>>>>>>>");
        driver.close();
        driver.quit();
        driver = null;
        return this;
    }

    /* -------------------------------------------------------------------------------------------- */
    /* verify                                                                                       */
    /* -------------------------------------------------------------------------------------------- */
    public RandomNumberGeneratorWebsite verifyOnHomePage() {
        // verify browser opened to home page -- check random number label
        new WebDriverWait(driver, 10).
                until( ExpectedConditions.textToBePresentInElement(textRandomNumberLabel(), "Latest random number between [0, 10) is: ") );

        Log.info("Verified on home page.");
        return this;
    }

    public RandomNumberGeneratorWebsite verifyRandomNumberIsWithinRange() {
        int randomNumberDisplayed = getRandomNumber();
        assertThat("Found random number not within range: " + randomNumberDisplayed,
                getRangeFromPage().isWithinRange(randomNumberDisplayed), is(true));
        Log.info("Verified random number is within range.");
        return this;
    }
    /* -------------------------------------------------------------------------------------------- */
    /* constant                                                                                     */
    /* -------------------------------------------------------------------------------------------- */
    public static class ExpectedMessage {
        public static class Success { }
        public static class Info    { }
        public static class Error   { }
    }



    public static void main(String[] args)
    {
        new RandomNumberGeneratorWebsite() {{
            this.
                    openBrowser().
                    clickRefresh();
            Utils.waitForApp(100);
            closeBrowser();
        }};
    }

}