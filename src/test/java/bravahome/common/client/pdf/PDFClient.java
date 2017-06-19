/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.client.pdf;

import bravahome.framework.Config;
import bravahome.framework.Log;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.testng.log4testng.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by gzoldi on 02/25/2017 06:57 AM
 */
public class PDFClient {

    public static Logger DEV_NULL = Log.getLogger(PDFClient.class);

    public static List<String> getDocumentAsList(String file) {
        // call to parse pdf to text then convert it to string list
        return new ArrayList<String> (
                Arrays.asList( parseDocument(file).split("\n") )
        );
    }

    public static List<String> getDocumentAsList(String file, String lookFor, int totalLines) {
        // check args
        if (totalLines < 0)
            throw new RuntimeException("Argument totalLines must be > 0.");

        // call to parse pdf to text then convert it to string list
        List<String> lines = getDocumentAsList(file);

        // find line containing lookFor and make it start index
        int start = -1;
        for (String line : lines) {
            if (line.contains(lookFor)) {
                start = lines.indexOf(line);
                continue;
            }
        }

        // determine where to stop pruning, end index
        int end = start + totalLines;

        // prune total lines requested starting from lookFor
        List<String> pruned = new ArrayList<String>();
        try {
            pruned = lines.subList(start, end);
        }
        catch (IndexOutOfBoundsException e) {
            // just do nothing to return empty list
            // callers assertion should find nothing was returned - no match found
        }

        return pruned;
    }

    private static String parseDocument(String path) {
        try {
            PDFParser parser = new PDFParser(
                    new FileInputStream( new File(path) )
            );
            parser.parse();

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(1);
            // TODO: is this even needed to set the end page?
            stripper.setEndPage(10);

            return stripper.getText( new PDDocument(parser.getDocument()) );
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /*
     * playground
     */
    public static void main(String args[])
    {
        final String samplePDF = Config.getProperty("samples.invoice");

        sampleTest1(samplePDF);
        sampleTest2(samplePDF);
    }

    private static void sampleTest1(String file) {
        // convert pdf file to string list
        List<String> actualPDFList = getDocumentAsList(file, "SMS Messages", 3);

        // verify pdf contains single usage line item
        String expectedUsageGroup    = "SMS Messages";
        String expectedUsageBI       = "UNITED STATES Inbound Shortcode SMS - Verizon 1,000 $5.00";
        String expectedUsageSubTotal = " Sub Total $5.00";

        assertThat("Invalid Usage Line Item",
                actualPDFList,
                is(Matchers.containsInAnyOrder(expectedUsageGroup, expectedUsageBI, expectedUsageSubTotal))
        );
    }

    private static void sampleTest2(String file) {
        // convert pdf file to string list
        List<String> actualPDFList = getDocumentAsList(file);

        // verify pdf contains single usage line item
        String expectedUsageGroup    = "SMS Messages";
        String expectedUsageBI       = "UNITED STATES Inbound Shortcode SMS - Verizon 1,000 $5.00";
        String expectedUsageSubTotal = " Sub Total $5.00";

        assertThat("Invalid Usage Line Item",
                actualPDFList, hasItems(expectedUsageGroup, expectedUsageBI, expectedUsageSubTotal)
        );
    }

}