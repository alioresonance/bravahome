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

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Created by gzoldi on 02/25/2017 06:57 AM
 */
public class JsonTest extends RestClient {

    public static Logger DEV_NULL = Log.getLogger(JsonTest.class);

    public JsonTest() {
        Log.info("Running " + this.getClass().getName() + "...");
    }

    /**
     * Call JSON Test service to return your ip address
     * @return String ip_address
     */
    public String getIPAddress() {
        RestAssured.baseURI  = getURI(Server.IP);
        return given().get().jsonPath().get("ip");
    }

    private String getURI(Server server) {
        return "".format(
                        "http://%s:%s",
                        server.value() + "." + Config.getProperty("json-test.host"),
                        Config.getProperty("json-test.port")
        );
    }

    private enum Server {
        IP       ("ip"),
        HEADERS  ("headers"),
        DATE     ("date"),
        TIME     ("time"),
        ECHO     ("echo"),
        VALIDATE ("validate");

        private final String value;

        Server(String value) {
            this.value = value;
        }

        private String value() {
            return value;
        }

        public static void main(String[] args) {
            System.out.println("==> " + Server.IP.value());
        }
    }


    /*
     * sandbox
     */
    public static void main(String[] args)
    {
        // invoke test json service endpoint to get your ip address
        String actualIP = RestClient.JsonTest().getIPAddress();

        // verify ipaddress
        assertThat( "should PASS", actualIP, is(not(equalTo("0.0.0.0"))) );
        assertThat( "should FAIL", actualIP, is(equalTo("0.0.0.0")) );
    }

}