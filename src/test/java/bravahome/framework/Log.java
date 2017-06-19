/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework;

import org.testng.log4testng.Logger;
import org.testng.Reporter;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Debugging utility to make log entries to logfile and the console output.
 */
public class Log {

    public final static String AMUSE = "><)))'> ";

    private static Logger logger = null;
    private static String screenShotDir = "";
    private static String style = "[%s] %s";

    public static Logger getLogger(Class zClass) {
        if (logger == null) {
            initializeLog(zClass);
        }
        return logger;
    }

    private static String datestamp(String methodName, Object message) {
        return datestamp( "".format(style, methodName, message.toString()) );
    }

    private static String datestamp(Object message) {
        String addStyleForTimestamp = "".format(style, "yyyy-MM-dd HH:mm:ss", "");
        return new SimpleDateFormat(addStyleForTimestamp).format(new Date()) + message.toString();
    }

    public static String amuseStamp(String methodName, Object message) {
        return AMUSE + datestamp(methodName, message);
    }

    public static String amuseStamp(Object message) {
        return AMUSE + datestamp(message);
    }

    /**
     * Initializes the logging object based on the class passed in from configuration xml specified.
     * @param zClass	the class in the configuration xml to use
     */
    private static void initializeLog(Class zClass) {
        logger = Logger.getLogger(zClass);
    }

    /**
     * Uses the logger to post a basic information message.
     * @param message the text to post using the log object.
     */
    public static void info(Object message) {
        String[] cause = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
        String classNameTag = "".format("[%s] ", cause[cause.length - 1]);
        logger.info(amuseStamp(classNameTag + message));
    }

    /**
     * Uses the logger to post a warning message.
     * @param message the text to post using the log object.
     */
    public static void warn(Object message) {
        logger.warn(amuseStamp(message));
    }

    /**
     * Uses the logger to post a error message.
     * @param message the text to post using the log object.
     */
    public static void error(Object message) {
        logger.error(amuseStamp(message));
    }

    /**
     * Uses the logger to post a debug message.
     * @param message the text to post using the log object.
     */
    public static void debug(Object message) {
        String methodName = getCallingMethod(3);
        logger.debug(amuseStamp(methodName, message));
    }

    /**
     * Uses the logger to post a fatal message.
     * @param message the text to post using the log object.
     */
    public static void fatal(Object message) {
        String methodName = getCallingMethod(3);
        logger.fatal(amuseStamp(methodName, message));
    }

    /**
     * Uses the logger to post a trace message.
     * @param message the text to post using the log object.
     */
    public static void trace(Object message) {
        String methodName = getCallingMethod(3);
        logger.trace(amuseStamp(methodName, message));
    }

    /**
     * Uses the logger to post a fatal message and take a screenshot if
     * requested.  Automatically appends a date-time stamp, the message
     * level, and the calling method to the message passed in.
     * @param message the text to post using the log object.
     * @param takeScreenshot flag to indicate to take a screenshot or not.
     */
    public static void fatal(Object message, boolean takeScreenshot) {
        String methodName = getCallingMethod(3);
        if (takeScreenshot) {
            takeScreenshot(methodName + "_fatal", true);
        }
        logger.fatal(datestamp(methodName, message));
    }

    /**
     * Uses the logger to post a debug message and take a screenshot if
     * requested.  Automatically appends a date-time stamp, the message
     * level, and the calling method to the message passed in.
     * @param message the text to post using the log object
     * @param takeScreenshot flag to take screenshot during debugging
     *
     */
    public static void debug(Object message, boolean takeScreenshot) {
        String methodName = getCallingMethod(3);
        if ( methodName.contentEquals( getCallingMethod(2) ) ) {
            methodName = getCallingMethod(4);
        }
        if (takeScreenshot) {
            takeScreenshot(methodName + "_debug", true);
        }
        logger.debug(datestamp(methodName, message));
    }

    /**
     * Uses the logger to post a error message and take a screenshot if
     * requested.  Automatically appends a date-time stamp, the message
     * level, and the calling method to the message passed in.
     * @param message the text to post using the log object.
     * @param takeScreenshot flag to indicate to take a screenshot or not.
     */
    public static void error(Object message, boolean takeScreenshot) {
        String methodName = getCallingMethod(3);
        if ( methodName.contentEquals( getCallingMethod(2) ) ) {
            methodName = getCallingMethod(4);
        }
        if (takeScreenshot) {
            takeScreenshot(methodName + "_error", true);
        }
        logger.error(datestamp(methodName, message));
    }

    /**
     * Takes a screenshot of the current desktop and saves it the location
     * and name specified.
     * @param fileName 	path and name of the file to save the screenshot as.
     * @param appendTimeStamp	flag for appending a time and date to the
     * 							file name specified.
     */
    public static void takeScreenshot(String fileName, boolean appendTimeStamp) {
        String imageName = fileName;
        if (appendTimeStamp) {
            imageName = imageName + getDateForFileName();
        }
        if ( !screenShotDir.isEmpty() ) {
            imageName = screenShotDir + "\\" + imageName ;
        }

        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);
            Robot robot = new Robot();
            File file = new File(imageName + ".png");
            BufferedImage image = robot.createScreenCapture(screenRect);

            ImageIO.write(image, "png", file);
            Reporter.log("<a href=\"" + file.getAbsolutePath() + "\">Screenshot (" + fileName + ") </a>");
        }
        catch (AWTException e) {
            logger.error( datestamp("Error taking screenshot - " + e.getMessage()) );
        }
        catch (IOException e) {
            logger.error( datestamp("Error writing screenshot - " + e.getMessage()) );
        }
    }

    /**
     * Creates a string using the current date and time for use in a
     * file name.
     * @return a date-time formatted string for file name usage.
     */
    private static String getDateForFileName() {
        Calendar calendar = Calendar.getInstance();
        String dateString = "";
        dateString =  dateString + "_" + "".format("%tF", calendar);
        dateString = dateString + "_" + "".format("%tT", calendar);
        dateString = dateString.replace(':', '_');
        return dateString;
    }

    /**
     * Gets the specified calling method name at the depth requested
     * by the parameter.
     * @param callStackDepth the depth of method in the stack to retrieve.
     * @return the name of the method at the stack depth specified.
     */
    private static String getCallingMethod(int callStackDepth) {
        String className = Thread.currentThread().getStackTrace()[callStackDepth].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[callStackDepth].getMethodName();
        return className + "." + methodName;
    }

    /**
     *
     * @param outputDir
     */
    public static void setScreenshotDir(String outputDir) {
        screenShotDir  = outputDir;
    }

}