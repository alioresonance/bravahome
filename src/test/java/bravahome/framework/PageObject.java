/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework;

/*
 * Created by gzoldi on 02/09/2017 08:43 AM
 */
import bravahome.common.util.Utils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import java.util.List;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class PageObject {

    protected WebDriver driver = null;
    protected Wait<WebDriver> wait = null;

    protected void init() {
        driver = WebDriverFactory.createDriver();
        wait = new FluentWait<WebDriver>( driver ).
                    withTimeout(5, MINUTES ).
                    pollingEvery(5, SECONDS ).
                    ignoring( NoSuchElementException.class );
    }


    /* -------------------------------------------------------------------------------------------- */
    /* shared keyword                                                                               */
    /* -------------------------------------------------------------------------------------------- */
    protected String getUrl() {
        // replace caller clazzName as key to lookup subcontext property key
        final String TEST_ENVIRONMENT = Config.getProperty("test.environment");
        final String URI_PATTERN = Config.getProperty( getSubclassName().toLowerCase() + ".url.pattern" );

        // which environment to run our tests?
        final String USE_THIS_ENV =
                // if unset, by default, use PROD; if PROD do not substitute for env; if DEV, QA STAGE add a dot notation
                ( TEST_ENVIRONMENT.equals("") || TEST_ENVIRONMENT.equals("prod")) ?
                        TEST_ENVIRONMENT :
                        TEST_ENVIRONMENT + ".";

        return "".format(URI_PATTERN, USE_THIS_ENV);
    }

    protected void scrollUp() {
        scrollBy(0,-500);
    }
    protected void scrollDown() {
        scrollBy(0,500);
    }
    protected void scrollRight() {
        scrollBy(700,0);
    }
    protected void scrollLeft() {
        scrollBy(-700,0);
    }

    /**
     * example: 250 pixels = 3.33 inches
     *      window.scrollBy(0, -250);   scroll UP by 250px
     *      window.scrollBy(0, 250);    scroll DOWN by 250px
     *      window.scrollBy(250, 0);    scroll to LEFT by 250px
     *      window.scrollBy(-250, 0);   scroll to RIGHT by 250px
     * @param x  in pixels
     * @param y  in pixels
     */
    protected void scrollBy(int x, int y) {
        String scroll = "".format("window.scrollBy(%s, %s)", x, y);
        ((JavascriptExecutor)driver).executeScript(scroll, "");
    }

    /* -------------------------------------------------------------------------------------------- */
    /* helper methods                                                                               */
    /* -------------------------------------------------------------------------------------------- */
    protected void waitForApp(final long millis) {
        Utils.waitForApp(millis);
    }

    protected void waitUntil(final ExpectedCondition<Boolean> expected, String forThisToHappen) {
        Log.info("Waiting until " + forThisToHappen + "...");
        wait.until(expected);
    }

    protected WebElement findElement(final By element) {
        return findElement(element, driver);
    }

    protected WebElement findElement(final By element, final WebDriver driver) {
        try {
            Utils.waitForApp(200);
            return autoHighlight(driver.findElement(element));
        }
        catch (NullPointerException npe) {
            throw new RuntimeException("WebDriver instance is NULL...call openBrowser() to create browser session.");
        }
    }

    protected List<WebElement> findElements(final By element) {
        return findElements(element, driver);
    }

    protected List<WebElement> findElements(final By element, final WebDriver driver) {
        return driver.findElements(element);
    }

    protected String getSubclassName() {
        int stacktraceLevel = 3;
        String caller = Thread.currentThread().getStackTrace()[stacktraceLevel].getClassName();
        String clazzName = caller.substring( caller.lastIndexOf('.') + 1 );
        return clazzName;
    }

    protected WebElement autoHighlight(final WebElement element) {
        final boolean AUTO_HIGHLIGHT_ENABLED = Boolean.valueOf( Config.getProperty("auto.highlight") );
        // flash the element before returning it
        if (AUTO_HIGHLIGHT_ENABLED) {
            // flash this many times
            final int FLASH_COUNT = 3;
            for (int i = 0; i < FLASH_COUNT; i++)
                flash(element);
        }
        // to flash or not to flash just return the element
        return element;
    }

    private void flash(final WebElement element) {
        // original style set for this element
        String origBgColor     = element.getCssValue("backgroundColor");
        String origBorderColor = element.getCssValue("borderColor");
        String origBorderWidth = element.getCssValue("borderWidth");

        // highlight style colors
        final String HI_BG_COLOR = "rgb(255,255,0)";  // yellow
        final String HI_BORDER_COLOR = "rgb(255,0,0)"; // red border
        final String HI_BORDER_WIDTH = "thick";

        // make element appear highlighted
        highlight(element, HI_BG_COLOR, HI_BORDER_COLOR, HI_BORDER_WIDTH);

        // make element appear NOT highlighted
        highlight(element, origBgColor, origBorderColor, origBorderWidth);  // this doesn't work in every case for some reason
        highlight(element,"rgb(255,255,255)","rgb(255,255,255)","initial");
    }

    private void highlight(final WebElement element, final String bgcolor, final String bordercolor, final String borderwidth) {
        ((JavascriptExecutor) driver).
                executeScript(
                        setStyleTo(bgcolor, bordercolor, borderwidth),
                        element
                );

        // let's slow down the flashing a bit
        final int POLL = 100;
        flashSpeed(POLL);
    }

    private String setStyleTo(final String bgcolor, final String bordercolor, final String borderwidth) {
        // template style attributes to change
        String style = "";
        style += "arguments[0].style.backgroundColor = '%s'; ";
        style += "arguments[0].style.borderColor = '%s'; ";
        style += "arguments[0].style.borderWidth = '%s'; ";

        // javascript query to change style
        return String.format(style, bgcolor, bordercolor, borderwidth);
    }

    private void flashSpeed(final long millis) {
        Utils.waitForApp(millis);
    }

}