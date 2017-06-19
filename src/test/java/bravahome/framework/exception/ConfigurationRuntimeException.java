/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework.exception;

public class ConfigurationRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor to print the configuration runtime exception
     * @param message of exception
     */
    public ConfigurationRuntimeException(String message) {
        super(message);
    }

}