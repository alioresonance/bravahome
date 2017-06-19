/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework.property;

public enum TestType
{
    UNIT            ("unit"),              // Unit Testing
    INTEGRATION     ("integration"),       // Integration Testing
    API             ("api"),               // API Testing
    FUNCTIONAL      ("functional"),        // Functional Testing
    SYSTEM          ("system"),            // System Testing
    END_TO_END      ("end-to-end"),        // End-to-End Testing

    SANITY          ("sanity"),            // Sanity Testing
    REGRESSION      ("regression"),        // Regression Testing
    ACCEPTANCE      ("acceptance"),        // Acceptance Testing
    AUTOMATION      ("automation"),        // Automation Testing

    BUILD           ("build"),             // Build Verification Testing
    SMOKE           ("smoke"),             // Smoke Testing
    SERVICE         ("service"),           // Service Layer Testing (J2EE, JMS, Spring, .NET Framework)
    WEB_SERVICES    ("ws"),                // Web Services Testing (SOAP, RESTFUL API)

    WORKFLOW        ("workflow"),          // Workflow Testing
    USE_CASE        ("use-case"),          // Use Case Verification

    LOAD            ("load"),              // Load Testing
    STRESS          ("stress"),            // Stress Testing
    PERFORMANCE     ("performance"),       // Performance Testing

    USABILITY       ("usability"),         // Usability Testing
    INSTALL         ("install"),           // Install/Uninstall Testing
    RECOVERY        ("recovery"),          // Recovery Testing
    SECURITY        ("security"),          // Security Testing
    COMPATIBILITY   ("compatibility"),     // Compatibility Testing
    DATABASE        ("database"),          // Database Testing
    BIG_DATA        ("big-data"),          // Hadoop NoSQL Testing

    POC             ("prototype"),         // Protyping (Proof Of Concept)
    ALPHA           ("alpha"),             // Alpha Testing
    BETA            ("beta"),              // Beta Testing
    RC1             ("rc1"),               // Release Candidate 1 Verification
    RC2             ("rc2"),               // Release Candidate 2 Verification
    RC3             ("rc3"),               // Release Candidate 3 Verification
    RTM             ("rtm"),               // Release To Manufacturing Verification

    BLACKBOX        ("blackbox"),          // Blackbox Testing
    WHITEBOX        ("whitebox");          // Whitebox Testing

    private final String category;

    TestType(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

}