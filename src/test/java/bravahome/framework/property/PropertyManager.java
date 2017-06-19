/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework.property;

import bravahome.framework.Log;
import bravahome.framework.exception.PropertyFileKeyException;
import bravahome.framework.exception.PropertyException;
import bravahome.framework.exception.PropertyManagerRuntimeException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Stores all configuration files.
 */
public class PropertyManager {

    private static Properties properties = null;
    private static List<PropertyFile> propertyFileList = null;

    public final static String GRAVITY_PROP   = "bravahome.properties";
    public final static String CONFIG_PROP    = "config.properties";
    public final static String FRAMEWORK_PROP = "framework.properties";
    public final static String CAMERA_PROP    = "camera.properties";

    /**
     * initializes the internal property file list selected property files
     */
    private static void initialize() {
        propertyFileList = new ArrayList<PropertyFile>();

        // TODO - Make this a dynamically loading mechanism
        PropertyFile frameworkPropertyFile = new PropertyFile( FRAMEWORK_PROP, 1 );
        propertyFileList.add(frameworkPropertyFile);

        PropertyFile cameraPropertyFile = new PropertyFile( CAMERA_PROP, 2 );
        propertyFileList.add(cameraPropertyFile);
    }

    /**
     * default constructor
     * @throws PropertyException, Exception
     */
    private PropertyManager() throws PropertyException, Exception {
        this(FRAMEWORK_PROP);
    }

    /**
     * constructor to instantiate Properties Object
     * @param filename - of Property including the path to the file
     */
    private PropertyManager(String filename) throws PropertyException {
        try {
            properties = new Properties();
            FileInputStream propStream = new FileInputStream( filename );
            properties.load( propStream );
        }
        catch (FileNotFoundException e) {
            throw new PropertyException( "Could not find " + filename );
        }
        catch (IOException e) {
            throw new PropertyException( "Could not load " + filename );
        }
        catch (Exception e) {
            throw new PropertyException( "Error occurred: " + e.getMessage() );
        }
    }

    /***
     * Gets a property value from the PropertyManager. Checks various property files in a specified order.
     * @param   key - property to look for
     * @return  value of the property looked for
     */
    public static String get(String key) {
        String value = null;
        if ( propertyFileList == null )
            initialize();
        // Sort to check in priority order
        Collections.sort( propertyFileList, new PropertyFileComparator() );

        for (PropertyFile propFile : propertyFileList) {
            try {
                value = propFile.get(key);
                Log.debug("Found key [" + key + "] in [" + propFile.getName() + "] with value of [" + value + "].\r\n");
                break;
            }
            catch (PropertyFileKeyException pfke) {
                Log.warn(pfke.getMessage());
            }
        }

        if (value == null) {
            throw new PropertyManagerRuntimeException("[" + key + "] not found in any property file.\r\n");
        }
        return value;
    }

    /***
     * sets the property value to the PropertyManager
     * @param   key - property to look for
     * @param   value - value to set
     * @param   comment - comment for the property
     */
    public static void set(String key, String value, String comment) {
        if ( propertyFileList == null ) {
            initialize();
        }
        // sort to check in priority order, then add to first file
        Collections.sort(propertyFileList, new PropertyFileComparator());
        propertyFileList.get(0).set(key, value);
    }

}