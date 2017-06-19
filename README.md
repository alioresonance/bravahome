# BravaHome
![brava logo](https://xxx/koalat/blob/master/automation/shared/img/koalat.jpg)

## Interview Test

Test Automation ![build status](http://xxx/buildStatus/icon?job=smoke-tests)
==========

BravaHome Interview Test for Gabe Zoldi

QuickStart Guide
-----------------

Note: The quickstart guide has only been tested for Mac OS X.  For Windows, the setup is almost similar.

**Get [Xcode](https://itunes.apple.com/au/app/xcode/id497799835?mt=12)**

**Get Command Line Tools**

    xcode-select --install

**Install HomeBrew**

    ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
    
    brew doctor         # to verify install
    brew search java    # to verify connect to remote Cellar
    brew update         # to get updates, run as needed
    
    # run if you run into file permission issues
    sudo chown -R $(whoami):admin /usr/local

**Install Java**

    brew install Caskroom/cask/java
    
    # test framework built usingJava 8
    java -version

**Install Maven**
    
    brew install maven
    
    # test framework built using Maven 3.3.9
    mvn -version 
    
**Git Clone Project**

    mkdir ~/github
    cd ~/github
    git clone https://github.com/alioresonance/bravahome.git
    cd bravahome
    git checkout master
    git pull
    
    # to start playing with code, please branch from master; the master branch MUST be kept pristine and ALWAYS in working condition
    git checkout -b <branch_name>        # e.g., git checkout -b gabe-playtime
    
**Get [IntelliJ](https://www.jetbrains.com/idea/download/) or [Eclipse](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/marsr)**

**Open BravaHome Project**

    Launch IntelliJ...
    - Select to Import Project
    - Select to Open ~/github/bravahome/pom.xml
    - Select option Import Maven projects automatically and click Next
    - Skip profiles and click Next
    - Verify artifact selected com.bravahome.interview:test:1.0.0-SNAPSHOT and click Next
    - Verify using JDK 1.8 and click Next ...(if this first time setup you may have to click + to add or ... to point to your local JDK home path)
    - Verify project name interviewtest, path and click Finish
    
    Click Ignore if Unregistered VCS root detected notification pops up.
    
    [WARNING] This may take a few minutes as maven is downloading lib dependencies from the internet
    
    When complete try compiling the project.
    - Select from IntelliJ menu Build > Rebuild Project
    
For Contributors
----------------
Clone the github repository:

    git clone https://github.com/alioresonance/bravahome.git
    cd bravahome

By default, the tests expect the selenium server to be running at `http://0.0.0.0:4723/wd/hub`.

Squish's test suite runs against the included test application. Start that up with installing the test framework

    mvn -DskipTests clean install

Then run tests with

    mvn clean test

### [running tests]

    mvn clean test                                  # run all tests in suite
    mvn clean test -Dtest=SmokeTest                 # run all test methods in test class
    mvn clean test -Dtest=SmokeTest#sampleTest1     # run one test method in test class
    mvn clean javadoc:test-javadoc                  # generate test api docs

# Web Controls
--------------

     abbrev     fullname    control type
     ------     --------    ------------
     btn        button      Button
     txt        text        Text
     sec        section     Section
     lnk        link        Hyperlink
     inp        input       Inputbox
     tre        tree        Tree
     lst        list        List
     img        image       Image
     frm        form        Form
     chk        checkbox    Checkbox
     rdo        radio       Radio Button
     lbl        label       Label
     mnu        menu        Menu

# Test Framework Enhancements
-----------------------------

     [should-have] implement custom step comment annotator to work with auto.highlight feature
     [must-have]   deploy seleniumgrid to be able to do cross-browser functionality tests
     [must-have]   deploy tests to run continuously in jenkins - send email with failures to look at
     [must-have]   generate testng reports to show failures
     [should-have] add logger capability to log steps for retracement if needed
     [could-have]  have a way to file a defect in jira of test failures, to demonstrate automation is catching regression issues
 
# Prioritization Definition
---------------------------
    
    Must have   --> Requirements labeled as MUST are critical to the current delivery timebox in order for it to be a success. If even one MUST requirement is not included, the project delivery should be considered a failure (note: requirements can be downgraded from MUST, by agreement with all relvant stakeholders; for example, when new requirements are deemed more important). MUST can also be considered a backronym for the Minimum Usable Subset.

    Should have --> Requirements labeled as SHOULD are important but not necessary for delivery in the current delivery timebox. While SHOULD requirements can be as important as MUST, they are often not as time-critical or there may be another way to satisfy the requirement, so that it can be held back until a future delivery timebox.

    Could have  --> Requirements labeled as COULD are desirable but not necessary, and could improve user experience or customer satisfaction for little development cost. These will typically be included if time and resources permit.

    Won't have  --> Requirements labeled as WON'T have been agreed by stakeholders as the least-critical, lowest-payback items, or not appropriate at that time. As a result, WON'T requirements are not planned into the schedule for the delivery timebox. WON'T requirements are either dropped or reconsidered for inclusion in later timeboxes. (Note: occasionally the term Would like is substituted, to give a clearer understanding of this choice).
