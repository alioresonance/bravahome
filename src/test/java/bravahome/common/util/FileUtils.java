/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.util;

import bravahome.framework.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by gzoldi on 02/09/2017 08:43 AM
 */
public class FileUtils {

    public void write(String filepath, String content) {
        File file = new File( filepath );

        // do not create if file already exists
        if (file.exists()) {
            String critical = "[FileAlreadyExistsException] %s\nDid you mean to overwrite instead?  @see %s";
            critical = "".format(critical, filepath, new FileUtils().getClass().getCanonicalName());
            throw new RuntimeException(critical);
        }
        else {
            try {
                FileWriter writer = new FileWriter( filepath, false );
                writer.write( content );
                writer.close();
                Log.info( "Created datafile: " + filepath );
            }
            catch (IOException e) {
                // oops! lets share
                throw new RuntimeException(e);
            };
        }
    }

    public void overwrite(String filepath, String content) {
        AGAIN: do {
            try {
                FileWriter writer = new FileWriter( filepath, false );
                writer.write( content );
                writer.close();
                Log.info( "Updated datafile: " + filepath );
            }
            catch (FileNotFoundException fnfe) {
                // maybe directory missing
                File f = new File( filepath );
                new File( f.getParent() ).mkdir();
                continue AGAIN;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            break AGAIN;
        }
        while(true);
    }

    public void append(String filepath, String content) {
        try {
            FileWriter writer = new FileWriter( filepath, true );
            writer.write( content );
            writer.close();
            Log.info( "Appended datafile: " + filepath );
        }
        catch (IOException e) {
            // oops! lets share yippee ki-yay
            throw new RuntimeException(e);
        }
    }

    public void delete(String filepath) {
        try {
            Files.deleteIfExists( Paths.get(filepath) );
            Log.info( "Deleted datafile: " + filepath );
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public String load(String filepath) {
        String content = "";
        try {
            List<String> contentAsList = Files.readAllLines( Paths.get(filepath) );
            for (String line : contentAsList)
                content += line;
            Log.info( "Loaded datafile: " + filepath );
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return content.replaceAll(" ", "");
    }



    /*
     * playground
     */
    public static void main(String[] args) {
        String pathfile = "blah.json";
        String content = "blahblahblah\n";
        String read;

        if (new File(pathfile).exists())
            new FileUtils().delete(pathfile);

        new FileUtils().write(pathfile, content);
        new FileUtils().append(pathfile, content);

        read = new FileUtils().load(pathfile);
        System.out.println("read => " + read);

        new FileUtils().overwrite(pathfile, content);

        read = new FileUtils().load(pathfile);
        System.out.println("read => " + read);
    }

}