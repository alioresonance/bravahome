/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework;

import org.codehaus.jackson.map.ObjectMapper;
import java.io.*;

/**
 * Created by gzoldi on 02/09/2017 08:43 AM
 */
public abstract class DataObject {

    public static class BOTT {
        public static class TestData {
//            public static String getCustomerDataPath(Customer customer) {
//                return Config.getBOTTSPath() + "/" + customer.getAccountType().toString().toLowerCase() + "/Customer" + customer.getAddress().getState() + ".json";
//            }
        }
    }

    public String toString() {
        try {
            return new ObjectMapper().
                    writerWithDefaultPrettyPrinter().
                    writeValueAsString(this);
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

}