/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.pageobject.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Confirm
{
    YES    ("Yes"),
    NO     ("No"),
    CANCEL ("Cancel"),
    CLOSE   ("Close");

    private final String type;

    Confirm(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    private static final List<Confirm> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Confirm random()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}