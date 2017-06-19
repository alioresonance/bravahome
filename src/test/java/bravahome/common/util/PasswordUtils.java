/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.util;

/**
 * Created by gzoldi on 3/11/17.
 */
public class PasswordUtils {

    public String secureText(String text) {
        return text.replaceAll(".", "•");
    }

}