/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework.property;

public enum SystemType
{
    FE      ("frontend"),
    BE      ("backend"),
    DB      ("database"),
    RESTAPI ("restapi"),
    MOBILE  ("mobile"),
    WEB     ("web");

    private final String type;

    SystemType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}