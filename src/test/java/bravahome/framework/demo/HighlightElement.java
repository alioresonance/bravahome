/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework.demo;

import org.openqa.selenium.WebElement;

/**
 * Created by gzoldi on 03/12/2017 03:46 PM
 */
public class HighlightElement {

    /**
     * Highlights the webelement while tests executed.
     * Great mode to be in to show what test is doing while clicking around.
     *
     * @param element
     */
    public static void highlightElement(WebElement element) {
        // TODO: find a way to add a tooltip annotator to elements to display step progress during
        // TODO: execution; we should be able to add things like custom comments to each step
        /*
         * synopsis:
         * this nice-to-have functionality might seem pointless for jenkins execution but actually
         * its useful for when tests fail, to be able to re-run failed tests in screen record mode
         * that way we see what exactly automation saw in terms of the failure; also its very useful
         * for manual testers to see or verify or signoff on the automation scripts running locally,
         * that it indeed ran correctly; this tooltip functionality mainly shows whats being executed
         * like hey the webdriver is now 'Clicking this button.' or 'Setting that field
         * with an incorrect password.' or 'Verifying alert error message is...' why?
         */
        for (int i = 0; i <2; i++) {
            //JavascriptExecutor js = (JavascriptExecutor) WebDriverManager.driver;
            //js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: yellow; border: 2px solid yellow;");
            //js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
        }
    }

}