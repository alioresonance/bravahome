/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.util;

import bravahome.framework.Config;
import org.testng.SkipException;

/**
 * Created by gzoldi on 02/09/2017 08:43 AM
 */
public class TestUtils {

    /**
     * Skip test w/ jira url for the reason
     * @param issueId   jira ticket number, e.g., FX-2851
     */
    public void bug(String issueId) {
        final String JIRA_URL = Config.getProperty("jira.url");
        skip(JIRA_URL + "/browse/" + issueId);
    }

    /**
     * Marks the test as skipped
     * @param reason    description of the reason
     */
    public void skip(String reason) {
        throw new SkipException(reason);
    }

}