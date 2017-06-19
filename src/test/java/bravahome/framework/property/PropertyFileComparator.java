/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework.property;

import java.util.Comparator;

public class PropertyFileComparator implements Comparator<PropertyFile> {

    public int compare(PropertyFile propFile1, PropertyFile propFile2) {
        return propFile1.getPriority() - propFile2.getPriority();
    }

}