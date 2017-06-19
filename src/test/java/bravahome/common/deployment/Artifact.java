/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.deployment;

import bravahome.framework.Config;
import bravahome.framework.Log;

/*
 * Created by gzoldi on 02/09/2017 08:43 AM
 */
public class Artifact {
    /*
     * artifact builder: IPA (iphone), IPK (android)
     * -- must be generated as ios-dev-build (signed using identity of iOS Developer)
     */
    public static String getLatest() {
        // TODO: try a builder pattern for default artifact injections into framework from hockeyapp deployment lanes
        final String JENKINS_HOST = Config.getProperty("jenkins.host");
        final String JENKINS_URL = "".format("http://%s", JENKINS_HOST) + "/billing";

        // set to get latest ipa from temporary repo on slave jenkins
        String artifactHostedOn = JENKINS_URL + "/" + Config.getProperty("latest.ios.artifact");

        // *** for development & debugging ***
        try {
            String overrideLatestIPA = Config.getProperty("override.latest.ipa");
            // if this flag is set in properties file return the overridden path to ipa
            if (!overrideLatestIPA.isEmpty()) {
                artifactHostedOn = overrideLatestIPA;
            }
        }
        catch (Exception e) {
            // swallow NPE, prolly key is not set in properties file
            Log.info(e.getMessage());
        }
        return artifactHostedOn;
    }

    public static String getCertificateKey() {
        String key = "";
        key += "nIbLZNn8ID/8OpP7";
        key += "AsYB6TmvDBxIuLur";
        key += "jADfiEkbfrlyEcom";
        key += "tN8dOlRMSy3KFKM4";
        key += "coBnusKhQ0f9KwXG";
        key += "6zuzlAVvO79JcJOi";
        key += "0VwDZ8vwGgTspIL5";
        key += "5P1S0/IRXAZlW1Ap";
        key += "ILKVb/l7XvE=";
        return key;
    }

}