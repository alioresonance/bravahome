/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.util;

import bravahome.framework.Log;
import org.testng.SkipException;
import org.testng.log4testng.Logger;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by gzoldi on 3/11/17.
 */
public class Utils {

    public static Logger DEV_NULL = Log.getLogger(Utils.class);

    /*
     * shared utilities - nice to expose
     */
    public static DateUtils Date() {
        return new DateUtils();
    }

    public static FileUtils File() {
        return new FileUtils();
    }

    public static PasswordUtils Password() {
        return new PasswordUtils();
    }

    public static TestUtils Test() {
        return new TestUtils();
    }


    public static void snooze(long millis) {
        waitingOnCallback(millis, TimeUnit.MILLISECONDS);
    }

    public static String uniqueByCurrentTime() {
        // use current unix epoch time
        return String.valueOf(System.currentTimeMillis());
    }

    public static String uniqueByUUID() {
        // filter out the hyphens xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void randomWait() {
        // select range between 1 to 30 seconds
        final int MIN = 1;
        final int MAX = 30;
        int randomSecs = new Random().nextInt(MAX - MIN) + MIN;
        waitForApp(randomSecs * 1000);
    }

    public static void waitForApp(long timeout) {
        waitingOnCallback(timeout, TimeUnit.MILLISECONDS);
    }

    public static void skipTest(String reason) {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
        String message = "".format(
                "%s.%s():%s => SKIPPING TEST: %s",
                stack.getClassName(),
                stack.getMethodName(),
                stack.getLineNumber(),
                reason
        );
        Log.info(message);
        throw new SkipException(message);
    }

    /**
     * Waiting for app to complete.
     * @param timeout   how to long wait before giving up
     * @param unit      unit of time
     */
    private static void waitingOnCallback(long timeout, TimeUnit unit) {
        // TODO: add verbose logging for debug mode only
        //Log.info( "".format("¯\_(ツ)_/¯ ... %s %s", timeout, unit.toString().toLowerCase()) );

        long howLongToWait = 0;
        switch (unit) {
            case HOURS:
                howLongToWait = timeout * 60 * 60 * 1000;
                break;
            case MINUTES:
                howLongToWait = timeout * 60 * 1000;
                break;
            case SECONDS:
                howLongToWait = timeout * 1000;
                break;
            case MILLISECONDS:
                howLongToWait = timeout;
                break;
            default:
                throw new RuntimeException( "".format("TimeUnit '%s' is not implemented.", unit) );
        }
        asyncCallback(howLongToWait);
    }


    private static void asyncCallback(long timeout) {
        // temporary workaround with an added side effect of tests running super long
        try {
            // its hacky nappy hammer time
            Thread.sleep(timeout);
        }
        catch (InterruptedException ie) {
            throw new RuntimeException( Log.amuseStamp("[Sleep Interrupt] Ah man I was having such a good nap.") );
        }
    }

}