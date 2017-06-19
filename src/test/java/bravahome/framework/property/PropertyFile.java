/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework.property;

import bravahome.framework.Log;
import bravahome.framework.exception.PropertyFileKeyException;
import bravahome.framework.exception.PropertyManagerRuntimeException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyFile {

    private Properties properties = null;
    private String fileName;
    private int position = 0;

    /***
     * Constructor to create a new PropertyFile object.
     * @param propertyFileName - name of the key-value pair text file to use
     * @param priority - in which to search, 1 = first, 2 = second, and so on...
     */
    public PropertyFile(String propertyFileName, int priority) {
        fileName = propertyFileName;
        position = priority;
    }

    /***
     * Initializes the PropertyFile object for use.
     * Attempts to load the requested file into a java Properties object.
     */
    private void initialize() {
        properties = new Properties();
        FileInputStream file;

        try {
            file = new FileInputStream(fileName);
            properties.load(file);
        }
        catch (FileNotFoundException e) {
            Log.error("Cannot find file [" + fileName + "] - " + e.getMessage());
            throw new PropertyManagerRuntimeException(e.getMessage());
        }
        catch (IOException e) {
            Log.error("Cannot read file [" + fileName + "] - " + e.getMessage());
            throw new PropertyManagerRuntimeException(e.getMessage());
        }
    }

    /***
     * Attempt to get the current instance of the local properties object.
     * Creates a new one if it does not exist.
     * @return Returns the local instance of the properties object.
     */
    private Properties getInstance() {
        if (properties == null) {
            initialize();
        }
        return properties;
    }

    /***
     * Gets a property value from the internal properties object.
     * @param   key - key of the value to retrieve
     * @return  String of the value for the specified key
     * @throws PropertyFileKeyException key not found
     */
    public String get(String key) throws PropertyFileKeyException {
        if (!getInstance().containsKey(key))
            throw new PropertyFileKeyException("[" + fileName + "] does not contain key [" + key + "].\r\n");
        return getInstance().getProperty(key);
    }

    /***
     * Sets a property value for the passed in key.
     * @param key - Key to set
     * @param value - Value for the passed in key
     */
    public void set(String key, String value) {
        getInstance().setProperty(key, value);
    }

    /***
     * Gets the name of the PropertyFile object.
     * @return String filename of this PropertyFile object.
     */
    public String getName() {
        return fileName;
    }

    /***
     * Gets the priority of the property file it the list.
     * @return int representing the position of the property file in the list
     */
    public int getPriority() {
        return position;
    }

    @Override
    public String toString() {
        return fileName;
    }

}