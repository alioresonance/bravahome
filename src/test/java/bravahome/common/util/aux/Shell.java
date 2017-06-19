/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.util.aux;

/*
 * Created by gzoldi on 02/09/2017 08:43 AM
 */
import bravahome.framework.Log;
import org.testng.log4testng.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Utility to invoke shell commands.
 */
public class Shell {

    public static Logger DEV_NULL = Log.getLogger(Shell.class);

    /**
     * Returns NULL if it fails for any reason
     * @param cmd - command to invoke, e.g., 'echo testing123'
     * @param directory - directory to run this command from
     * @return results in list form, e.g., 'testing 123'
     */
    public static ArrayList<String> command(final String cmd, final String directory) {
        try {
            Process process =
                    new ProcessBuilder(new String[] {"bash", "-c", cmd})
                            .redirectErrorStream(true)
                            .directory(new File(directory))
                            .start();

            ArrayList<String> output = new ArrayList<String>();
            BufferedReader br = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
            String line = null;
            while ( (line = br.readLine()) != null )
                output.add(line);

            // TODO: there really should be a timeout here using worker threads to handle TimeoutException
            if (process.waitFor() != 0) {
                ArrayList<String> oops = new ArrayList<String>();
                oops.add("[ERROR] Unable to connect to appium server to invoke commands.");
                return oops;
            }
            return output;
        }
        catch (Exception e) {
            throw new RuntimeException("Ouch!...that hurts my feeling." + e.getMessage());
        }
    }

    public static ArrayList<String> invoke(final String cmdline) {
        ArrayList<String> output = command(cmdline, ".");
        for (String line : output) {
            Log.info(line);
        }
        return output;
    }

}