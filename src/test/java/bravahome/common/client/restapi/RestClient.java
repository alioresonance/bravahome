/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.client.restapi;

import bravahome.common.client.restapi.impl.CrontrollerTx;
import bravahome.common.client.restapi.impl.JsonTest;
import bravahome.common.client.restapi.impl.TxDataApi;
import bravahome.common.client.restapi.impl.TxTaxCollector;
import bravahome.common.deployment.Artifact;
import bravahome.common.deployment.Encoder;
import bravahome.framework.Log;
import org.testng.log4testng.Logger;

/**
 * Created by gzoldi on 02/25/2017 06:57 AM
 */
public abstract class RestClient {

    public static Logger DEV_NULL = Log.getLogger(RestClient.class);

    /*
     * factory method
     */
    public static JsonTest JsonTest() {
        return new JsonTest();
    }

    public static TxTaxCollector TxTaxCollector() {
        return new TxTaxCollector();
    }

    public static TxDataApi TxDataApi() {
        return new TxDataApi();
    }

    public static CrontrollerTx CrontrollerTx() {
        return new CrontrollerTx();
    }

    /*
     * support method
     */
    public static String authy(String type) {
        return new Encoder(Artifact.getCertificateKey()).signCertificate(type);
    }

    public static String deauthy(String type) {
        return new Encoder(Artifact.getCertificateKey()).decryptCertKey(type);
    }

}