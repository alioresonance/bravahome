/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.framework;

import bravahome.common.util.Utils;
import bravahome.framework.demo.DemoDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.*;
import org.testng.log4testng.Logger;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

/*
 * Created by gzoldi on 02/09/2017 08:43 AM
 */
public class WebDriverFactory {

    public static Logger DEV_NULL = Log.getLogger(WebDriverFactory.class);

    protected static final class SauceLabs {
        public static final String INGREDIENT    = "fxqaauto";
        public static final String SPECIAL_SAUCE = "h1+LiVTEZ4u4V3VIvv7sx7HKh/QC/9CquOu8yZBCh2aJIZchn5mq3A==";
        public static final String OVEN          = "ondemand.saucelabs.com:80";
    }

    public static WebDriver createDriver() {
        WebDriver driver = null;
        final boolean SELENIUMGRID = Boolean.valueOf(Config.getProperty("runtest.on.seleniumgrid"));

        if (SELENIUMGRID) {
            driver = useRemoteDriver();
            setWindowPosition(driver);
        }
        else {
            driver = useLocalDriver();
        }

        // set additional common driver parameters
        setImplicitWait(driver);
        setWindowSize(driver);
        return driver;
    }

    private static WebDriver useLocalDriver() {
        // TODO: refactor to support any browser types: ie, chrome, firefox, safari
        // TODO: inject driver type via bravahome.properties seleniumgrid mode
        System.setProperty("webdriver.chrome.driver", Config.getProperty("webdriver.chrome.driver.mac"));

        // demo driver to slow down test execution to show during presentations so folks can see what is happening,
        // otherwise, it tends to run too quickly to see what's happening
        final boolean IF_DEMO_MODE = Boolean.valueOf(Config.getProperty("demo.mode"));
        WebDriver driver = null;
        try {
            driver = (IF_DEMO_MODE) ? new DemoDriver() : new ChromeDriver();
        }
        catch(IllegalStateException ise) {
            String hint = "";
            hint += "\n1. Are you running this on Jenkins SeleniumGrid?  If so, check your settings in bravahome.properties ";
            hint += "and set 'runtest.on.seleniumgrid=true' then try again.\n";
            hint += "-OR-\n";
            hint += "2. Check the local webdriver path is correct or you may have to download a newer driver: ";
            hint += "https://sites.google.com/a/chromium.org/chromedriver/downloads\n\n";
            throw new RuntimeException(hint + ise);
        }
        return driver;
    }

    private static WebDriver useRemoteDriver() {
        //final Encoder encoder = new Encoder(Artifact.getCertificateKey());
        //final SauceOnDemandAuthentication CHEF = new SauceOnDemandAuthentication(
        //        INGREDIENT, encoder.signCertificate(SPECIAL_SAUCE)
        //);

        //final SauceREST SAUCY = new SauceREST( CHEF.getUsername(), CHEF.getAccessKey() );

        try {
            return new RemoteWebDriver(
                        new URL( Config.getProperty("seleniumgrid.url") ),
                        getCapability()
            );
        }
        catch (MalformedURLException mue) {
            throw new RuntimeException(mue);
        }
        catch (UnreachableBrowserException ube) {
            throw new RuntimeException("Is the SeleniumGrid running (hub & nodes up)? " + ube);
        }
    }

    private static void setImplicitWait(WebDriver driver) {
        /*
           ~~~ global override setting ~~~
           implicit vs. explicit waits
           1. when waiting for findElement(), selenium will wait implicitly for this long before it times out
           2. note that if the page is still loading, the onload event will appear as completed eventhough there maybe
              some page processing still going on in the background
              - e.g., the AngularJS Framework implements a custom event model so even though the browser
                event may say its done loading, in reality there still could be some processing going in the post onload
              - in these cases you should try injecting a javascript callback into angular's ngView controller to send
                back an event call for checking viewContentLoaded
              - otherwise you may be trying to access an element during test execution that will not be visible or
                inaccessible by Selenium which will throw you an error
           3. implicits are for the life of the webdriver instance
           4. existence of element on page is defined by properties for visibility and clickability, there maybe others
           5. try to always wait explicitly WebDriverWait(driver, maxtimeout).until(ExpectedCondition) function instead of relying on the
              global implicit wait to make your tests fast and more deterministic
              - by default, implicit waits will override explicit waits so use smallest wait possible
                and if you need to wait longer, extend the wait time per element lookup basis
         */
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    private static void setWindowPosition(WebDriver driver) {
        // rotating x-coordinate to offset window position so we can see browsers
        // that are stacked on top of each other when were running tests in parallel
        driver.manage().window().setPosition(new Point(RoundRobin.nextOffset(),0));
    }

    private static void setWindowSize(WebDriver driver) {
        final Boolean SCREENSHOT_ON_FAILURE = Boolean.valueOf(Config.getProperty("screenshot.on.failure"));

        if (SCREENSHOT_ON_FAILURE) {
            // make screen as big as possible so screenshot won't miss things or cutoff
            driver.manage().window().maximize();

            // TODO: wire-in capturing screeshots using @Auto-Wired(screenshots=true) at factory level
            /*
            import com.codeborne.selenide.testng.ScreenShooter;
            @Listeners({ ScreenShooter.class})

            // capturing local
            WebDriver driver = new ChromeDriver();
            driver.get("http://www.google.com/");
            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("target/screenshots/screenshot.png"));

            // capturing remote
            WebDriver augmentedDriver = new Augmenter().augment(driver);
            ((TakesScreenshot)augmentedDriver).getScreenshotAs(...);
            */
        }
        else {
            final int WIDTH = 0;
            final int HEIGHT = 1;

            // set window size according to property file setting
            String[] screen = Config.getProperty("screen.setting").split("x");
            int width = Integer.valueOf(screen[WIDTH]);
            int height = Integer.valueOf(screen[HEIGHT]);
            driver.manage().window().setSize(new Dimension(width, height));
        }
    }

    /* -------------------------------------------------------------------------------------------- */
    /* browser support methods                                                                      */
    /* -------------------------------------------------------------------------------------------- */
    private static DesiredCapabilities getCapability() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("dns-prefetch-disable", "start-maximized", "disable-webgl", "blacklist-webgl",
                "blacklist-accelerated-compositing", "disable-accelerated-2d-canvas", "disable-accelerated-compositing",
                "disable-accelerated-layers", "disable-accelerated-plugins", "disable-accelerated-video",
                "disable-accelerated-video-decode", "disable-gpu", "disable-infobars", "test-type");

        DesiredCapabilities caps = DesiredCapabilities.chrome();
        caps.setCapability(ChromeOptions.CAPABILITY, options);
        caps.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
        caps.setCapability(CapabilityType.BROWSER_VERSION, "58");       // <== @see https://saucelabs.com/platforms
        caps.setCapability(CapabilityType.PLATFORM, Platform.MAC);
        caps.setCapability("maxDuration", "10800");
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        //caps.setCapability("name", "billing-acceptance-tests");
        //caps.setCapability("build", "QA Automation - FX Billing Platform")
        caps.setCapability("commandTimeout", 600);
        caps.setCapability("idleTimeout", 999);
        return caps;
    }

    private void captureScreenshot(String obj) throws IOException {
        WebDriver chromeOnly = new ChromeDriver();
        File screenshotFile = ( (TakesScreenshot) chromeOnly ).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(
                screenshotFile,
                new File("target/screenshots/" + obj + "" + getTimeStampValue() + ".png")
        );
    }

    private void takeScreenshotElement(WebElement element) throws IOException {
        WrapsDriver wrapsDriver = (WrapsDriver) element;
        File screenshot = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
        java.awt.Rectangle rectangle = new java.awt.Rectangle(element.getSize().width, element.getSize().height);
        Point location = element.getLocation();
        BufferedImage bufferedImage = ImageIO.read(screenshot);
        BufferedImage destImage = bufferedImage.getSubimage(location.x, location.y, rectangle.width, rectangle.height);
        ImageIO.write(destImage, "png", screenshot);
        File file = new File("target/screenshots");
        FileUtils.copyFile(screenshot, file);
    }

    private void takeScreenshotMethod() throws IOException, AWTException {
        Utils.waitForApp(2000);
        long id = Thread.currentThread().getId();
        BufferedImage image = new Robot().createScreenCapture(
                new java.awt.Rectangle( Toolkit.getDefaultToolkit().getScreenSize() )
        );
        ImageIO.write(
                image,
                "jpg",
                new File("./target/surefire-reports/" + id + "/screenshot.jpg")
        );
    }

    private  String getTimeStampValue() {
        Calendar cal = Calendar.getInstance();
        Date time = cal.getTime();
        String timestamp=time.toString();
        Log.debug(timestamp);
        String systime = timestamp.replace(":", "-");
        Log.debug(systime);
        return systime;
    }

    /*
    public void selenium_after_step(Scenario scenario) throws IOException, JSONException {
        if ( scenario.isFailed() ) {
            scenario.write("Current URL = " + driver.getCurrentUrl() + "\n");

            try {
                driver.manage().window().maximize();  // maximize window to get full screen

                if ( isAlertPresent() ) {
                    Alert alert = getAlertIfPresent();
                    alert.accept();
                }

                byte[] screenshot;

                // Remote Driver flow
                if (false) { // Ggt screen shot from remote driver
                    Augmenter augmenter = new Augmenter();
                    TakesScreenshot ts = (TakesScreenshot) augmenter.augment(driver);
                    screenshot = ts.getScreenshotAs(OutputType.BYTES);
                }
                else { // get screen shot from local driver
                    screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                }

                scenario.embed(screenshot, "image/png");      // Embed image in reports
            }
            catch (WebDriverException we) {
                throw new RuntimeException(we);
            }
            catch (ClassCastException cce) {
                throw new RuntimeException(cce);
            }
        }
        seleniumCleanup();
    }
    */

}