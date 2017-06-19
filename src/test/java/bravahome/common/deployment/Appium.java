/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.deployment;

/**
 * Created by gzoldi on 02/09/2017 08:43 AM
 */
import bravahome.common.util.aux.Shell;
import bravahome.framework.Config;
import bravahome.framework.Log;
import org.testng.log4testng.Logger;

public class Appium {

    public static Logger DEV_NULL = Log.getLogger(Appium.class);

    private static final String USER     = Config.getProperty("appium.user");
    private static final String HOSTNAME = Config.getProperty("appium.host");

    private static final String RUN_AS = "".format("%s@%s", USER, HOSTNAME);
    private static final String HTTP   = "http://%s:%s";

    private static final String PORT     = Config.getProperty("appium.port");
    private static final String CGI_PORT = Config.getProperty("appium.cgiport");

    /*
     * appium urls
     */
    public static final String URL       = createUrl_Appium();
    public static final String LOCAL_URL = createUrl_AppiumLocal();
    public static final String CGI_URL   = createUrl_CGI();


    public static void displayStatus() {
        Log.info("Appium Status...");
        ssh("./appium.sh status");
    }

    public static void restart() {
        Log.info("Restarting Appium Server...");

        // TODO: remove later, invoking with ssh no longer works, disabling StrictHostKeyChecking is not working from jenkins
        //ssh("./appium.sh restart");

        // instead solving this by using http://www.johnloomis.org/python/cgiserver.html
        String restartUrl = CGI_URL + "/appium-restart.sh";
        curl(restartUrl);
        //displayStatus();
    }


    private static String createUrl_AppiumLocal() {
        return "".format(HTTP, "0.0.0.0", PORT) + "/wd/hub";
    }

    private static String createUrl_Appium() {
        return "".format(HTTP, HOSTNAME, PORT) + "/wd/hub";
    }

    private static String createUrl_CGI() {
        return "".format(HTTP, HOSTNAME, CGI_PORT) + "/cgi-bin";
    }

    private static void ssh(String cmd) {
        invoke("ssh " + RUN_AS + " '" + cmd + "'");
    }
    private static void curl(String url) {
        invoke("curl --silent " + url);
    }
    private static void invoke(String command) {
        Log.info("Invoke command: " + command);
        Shell.invoke(command);
    }


    /*
     * play area
     */
    public static void main(String[] args)
    {
        Appium.restart();
    }

}
