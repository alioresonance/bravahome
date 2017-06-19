/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework;

/*
 * Created by gzoldi on 02/09/2017 08:43 AM
 */
import bravahome.framework.property.PropertyManager;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Properties config file reader
 */
public class Config {

    // using Apache Commons Configuration for variable interpolation
    private static Configuration prop = null;


    public static String getTestDataPath() {
        return Config.getProperty("testdata.path");
    }

    public static String getSamplePath() {
        return getTestDataPath() + "/samples";
    }

    public static String getBOTTSPath() {
        return getTestDataPath() + "/botts";
    }


    /**
     * Return the key value in properties file.
     * @param   key  key name
     * @return       key value
     */
    public static String getProperty(String key) {
        try {
            prop = new PropertiesConfiguration( PropertyManager.GRAVITY_PROP );
        }
        catch (ConfigurationException e) {
            throw new RuntimeException( "".format( "Error getting value for the key '%s' from the property file %s",
                    key, PropertyManager.GRAVITY_PROP), e );
        }

        // get key value
        String value = prop.getString(key);
        Config.validateProp(key, value);

        // remove any comments inline, for example,   appium.host=0.0.0.0     # for development & debugging
        value = (value.contains("#")) ? value.split("#")[0] : value;
        return value.trim();
    }


    /**
     * Validates key is set and not empty.
     * @param key - name of key
     * @param value - value of key
     */
    public static void validateProp(String key, String value) {
        // check if key undefined
        if ( value == null )
            throw new RuntimeException( "".format("Cannot find '%s' key in %s file.", key, PropertyManager.GRAVITY_PROP) );

        // check if key unset
        if ( value.isEmpty() )
            Log.info( "".format( "The key '%s' unset in %s file.", key, PropertyManager.CONFIG_PROP ) );
    }



    /*
     * playground
     */
    public static void main(String[] args)
    {
        String prop = null;

        prop = Config.getProperty("appium.host");
        System.out.println(prop);

        prop = Config.getProperty("appium.hostXXX");
        System.out.println(prop);
    }

}