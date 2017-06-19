/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.test.interview.randomnumbergenerator;

import bravahome.framework.Log;
import bravahome.pageobject.RandomNumberGeneratorWebsite;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

/*
 * Created by gzoldi on 06/18/2017 05:17 PM
 */
public class UnbiasedTest {

    public static Logger DEV_NULL = Log.getLogger(UnbiasedTest.class);

    RandomNumberGeneratorWebsite site = new RandomNumberGeneratorWebsite();

    @DataProvider(name = "refresh")
    public static Object[][] attempts() {
        return new Object[][] {
                { 100 },
                { 200 },
                { 300 },
                { 400 },
                { 500 }
        };
    }

    @BeforeTest
    public void setUp() {
        site.
                openBrowser().
                verifyOnHomePage();
    }

    @AfterTest
    public void tearDown() {
        site.
                closeBrowser();
    }

    @Test(dataProvider = "refresh")
    public void testUnbiased(int attempt) {
        Log.info("".format("Trying %s attempt(s).....................", attempt));
        for (int i = 0; i < attempt; i++) {
            site.
                    verifyRandomNumberIsWithinRange().
                    clickRefresh();
        }
        // TODO: parallelize test (seleniumgrid), if one test fails it will not run the others
    }

}