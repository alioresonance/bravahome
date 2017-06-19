/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * Created by gzoldi on 05/16/2017 07:14 PM
 */
public class RoundRobin {

    private static List<Integer> offsets = Arrays.asList(320, 0, 80, 160, 240);

    public static int getOffset(int index) {
        return offsets.get(index);
    }

    public static int nextOffset() {
        Collections.rotate(offsets, -1);
        return getOffset(0);
    }

}
