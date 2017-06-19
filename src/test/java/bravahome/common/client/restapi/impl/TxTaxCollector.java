/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.client.restapi.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import bravahome.common.util.Utils;
import bravahome.framework.Config;
import bravahome.framework.Log;
import bravahome.common.client.restapi.RestClient;
import org.testng.log4testng.Logger;
import java.net.URL;
import java.util.Calendar;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by gzoldi on 02/25/2017 06:57 AM
 */
public class TxTaxCollector extends RestClient {

    public static Logger DEV_NULL = Log.getLogger(TxTaxCollector.class);

    public TxTaxCollector() {
        final String URI = "".format( "http://%s:9992", getFirstActivePublicHostname());
        Log.info("Triggering " + this.getClass().getName() + " via " + URI + "...");
        RestAssured.baseURI = URI;
        RestAssured.requestSpecification = given().contentType(ContentType.JSON);
    }

    public String getSureTaxBasePath() {
        return Config.getProperty("basepath.suretax") + "/%s";
    }

    /**
     * Trigger sales tax now using current month/year
     * @param accountSid    the account id (32 characters and starts with AC), e.g., AC3d8e6847e4d77cd20ebbb3e9d9e00c64
     * @param commit        true => process in real-time, false => testing do not process
     * @return
     */
    public Response triggerSalesTaxNow(String accountSid, boolean commit) {
        String currentMonth = String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1);
        String currentYear = String.format("%04d", Calendar.getInstance().get(Calendar.YEAR));
        return triggerSalesTax(accountSid, currentMonth, currentYear, currentMonth, currentYear, commit);
    }

    /**
     * Trigger sales tax for account using tx-tax-collector service endpoint
     * @param accountSid    the account id (32 characters and starts with AC), e.g., AC3d8e6847e4d77cd20ebbb3e9d9e00c64
     * @param billingMonth  the billing period month
     * @param billingYear   the billing period year
     * @param usageMonth    the month usage occurred
     * @param usageYear     the year usage occurred
     * @param commit        true => process in real-time, false => testing do not process
     * @return Response     RestAssured JSONObject
     *
     * @comment  If called twice in a row with commit it will return {"error": "No taxable usage"}.
     */
    public Response triggerSalesTax(String accountSid, String billingMonth, String billingYear, String usageMonth,
                                    String usageYear, boolean commit) {
        String WITH_PARAMS = "";
        WITH_PARAMS += getSureTaxBasePath();
        WITH_PARAMS += "?BillingMonth=%s&BillingYear=%s";
        WITH_PARAMS += "&UsageMonth=%s&UsageYear=%s";
        WITH_PARAMS += "&Go=%s&Commit=%s";

        /*
         * &Go=true&Commit=true         real-time processing -- will see taxes in PDF invoice
         * &Go=false&Commit=false       testing purpose      -- will NOT see taxes in PDF invoice
         */
        final String BASE_PATH = "".format(WITH_PARAMS, accountSid, billingMonth, billingYear, usageMonth, usageYear, commit, commit);

        // TODO: not sure why we need to post this way instead of using RestAssured withArgs() but that didn't work
        RestAssured.basePath = BASE_PATH;
        Response response = given().post(RestAssured.baseURI + RestAssured.basePath);

        final String NO_USAGE_FOUND = "No taxable usage";

        // check if no usage found - cannot proceed without usage added to account
        if ( response.body().jsonPath().get("").toString().contains(NO_USAGE_FOUND) )
            throw new RuntimeException(NO_USAGE_FOUND + " found");

        // wait for it to process in real-time asynchronously
        // TODO: [HACK] look into better approach deterministic
        Utils.waitForApp(60000);
        return response;
    }


    /**
     * Cancel sales tax for account using tx-tax-collector service endpoint
     * @param accountSid    the account id (32 characters and starts with AC), e.g., AC3d8e6847e4d77cd20ebbb3e9d9e00c64
     * @param cancelMonth   the billing period month to cancel
     * @param cancelYear    the billing period year to cancel
     * @param month         the month
     * @param year          the year
     * @return Response     RestAssured JSONObject
     *
     * @comment  If called twice in a row with commit it will return {..."error1": "No requests to cancel for {AccountSid} in {BillingPeriod}"...}
     */
    public Response cancelSalesTax(String accountSid, int cancelMonth, int cancelYear, int month, int year,
                                   boolean commit) {
        String WITH_PARAMS = "";
        WITH_PARAMS  += getSureTaxBasePath() + "/CancelTax";
        WITH_PARAMS += "?CancelMonth=%s&CancelYear=%s";
        WITH_PARAMS += "&Month=%s&Year=%s";
        WITH_PARAMS += String.format("&Go=%s&Commit=%s", commit, commit);

        RestAssured.basePath = "".format(WITH_PARAMS, accountSid, cancelMonth, cancelYear, month, year);
        return given().post(RestAssured.baseURI + RestAssured.basePath);
    }

    /**
     * Get first active tx-tax-collector boxconfig instance found running on stage
     * @return  ec2 public hostname
     */
    public String getFirstActivePublicHostname() {
        Log.info("Checking TX Cluster Monitor to get current public hostname...");
        final String TX_CLUSTER_MONITOR = Config.getProperty("tx.cluster.monitor");
        String public_hostname = null;

        try {
            // get hosts running as tx-tax-collector role on stage
            JsonNode root = new ObjectMapper().readTree( new URL(TX_CLUSTER_MONITOR) );

            // iterate thru hosts to find the first one that's Active
            for (JsonNode host : root.path("hosts")) {
                String active = host.path("roles").get(0).path("active").asText();
                if (active.equals("1")) {
                    public_hostname = host.path("public_hostname").asText();
                    Log.info("Found EC2 Public Hostname running for tx-tax-collector: " + public_hostname);
                }
            }
        }
        catch (Exception e) {
            final String ERR = "Could find available public hostname from tx-tax-collector monitor endpoint: " +
                    TX_CLUSTER_MONITOR + "\n" + e;
            throw new RuntimeException(ERR);
        }

        // check if no running Active instance
        if (public_hostname == null)
            throw new RuntimeException("Found no tx-tax-collector instance running on stage: " + TX_CLUSTER_MONITOR);
        return public_hostname;
    }



    /*
     * sandbox
     */
    public static void main(String[] args)
    {
        // trigger account sales tax
        Response resp = RestClient.TxTaxCollector().triggerSalesTax(
                "ACaa62175122e7f1db7f7430f905957075",
                "04",
                "2017",
                "04",
                "2017",
                false
        );

        Log.info("SaaS => " + resp.body().jsonPath().get("taxableUsage.SAAS"));
        Log.info("Telco => " + resp.body().jsonPath().get("taxableUsage.TELCO"));


        // verify SAAS
        assertThat( "Incorrect SaaS tax amount",
                resp.body().jsonPath().get("taxableUsage.SAAS").toString(), containsString("amount=8.0")
        );
        assertThat( "Incorrect SaaS bifur",
                resp.body().jsonPath().get("taxableUsage.SAAS").toString(), containsString("bifur=0.8")
        );

        // verify TELCO
        assertThat( "Incorrect Telco tax amount",
                resp.body().jsonPath().get("taxableUsage.TELCO").toString(), containsString("amount=2.0")
        );
        assertThat( "Incorrect Telco bifur",
                resp.body().jsonPath().get("taxableUsage.TELCO").toString(), containsString("bifur=0.2")
        );
    }

}
