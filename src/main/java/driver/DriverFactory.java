package driver;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DriverFactory {

    private static ThreadLocal<WebDriver> webDriver_TL = new ThreadLocal<>();

    public static WebDriver getDriver(){
        if(webDriver_TL.get() == null){
            webDriver_TL.set(createDriver());
        }
        return  webDriver_TL.get();
    }

    private static WebDriver createDriver() {
        WebDriver driver = null;
        String browserType = "chrome";

        switch (getBrowserType()) {
            case "chrome" -> {
                System.setProperty("webdriver.chrome.driver",
                        System.getProperty("user.dir")
                                + "/src/main/java/driver/drivers/chromedriver");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                driver = new ChromeDriver(chromeOptions);
            }
            case "firefox" -> {
                System.setProperty("webdriver.gecko.driver",
                        System.getProperty("user.dir")
                                + "src/main/java/driver/drivers/geckodriver");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                driver = new FirefoxDriver(firefoxOptions);
            }
        }
        //driver.manage().window().maximize();
        return driver;
    }

    private static String getBrowserType() {
        String browserType = null;
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(System.getProperty("user.dir")
                                                    + "/src/main/java/properties/config.properties");
            properties.load(fileInputStream);
            browserType = properties.getProperty("browser");
        } catch (IOException e) {
            System.out.println("Exception occurred while reading the config property file - " +e.getMessage());
        }
        return browserType;
    }

    public static void cleanupDriver(){
        webDriver_TL.get().quit();
        webDriver_TL.remove();
    }
}
