/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.datamodel;

import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;

/**
 * Created by gzoldi on 6/18/17.
 */
public class Range {

    private int lowerBound;
    private int upperBound;

    public Range(Matcher matcher) {
        matcher.find();
        this.lowerBound = Integer.valueOf(matcher.group());
        matcher.find();
        this.upperBound = Integer.valueOf(matcher.group());
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int idx = new Random().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[idx];
    }

    public int getLowerBound() {
        return lowerBound;
    }
    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }
    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public boolean isWithinRange(int number) {
        if (number >= lowerBound && number <= upperBound)
            return true;
        else
            return false;
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
