/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework.demo;

import bravahome.common.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by gzoldi on 03/12/2017 03:46 PM
 */
// TODO: for now only chrome is supported for demo mode; refactor to support other browser if needed
public class DemoDriver extends ChromeDriver {

    private final long DELAY = 200;

    public DemoDriver() {
        super();
    }

    /**
     * Overriding ChromeDriver to better control execution speed which is
     * very useful during demos where it's hard to see things running too
     * quickly to see what's happening.  This allows us to slow down execution.
     *
     * @param by type of locator
     * @return selenium webelement if found
     */
    @Override
    public WebElement findElement(By by) {
        Utils.waitForApp(DELAY);
        return by.findElement((SearchContext) this);
    }

}