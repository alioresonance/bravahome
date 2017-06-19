/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.client.restapi.impl;

import com.jayway.restassured.RestAssured;
import bravahome.framework.Config;
import bravahome.framework.Log;
import bravahome.common.client.restapi.RestClient;
import org.testng.log4testng.Logger;

/**
 * Created by gzoldi on 02/25/2017 06:57 AM
 */
public class CrontrollerTx extends RestClient {

    public static Logger DEV_NULL = Log.getLogger(CrontrollerTx.class);

    private final String URI = "".format(
            "http://%s:%s",
            Config.getProperty("crontroller-tx.host"),
            Config.getProperty("crontroller-tx.port")
    );

    public CrontrollerTx() {
        Log.info("Running " + this.getClass().getName() + "...");
        RestAssured.baseURI = URI;
    }

    /*
     * sandbox
     */
    public static void main(String[] args)
    {
        /**
         * @see TxTaxCollector
         */
        RestClient rest = RestClient.CrontrollerTx();
    }

}