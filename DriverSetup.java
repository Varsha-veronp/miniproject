package flipkart;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverSetup {

    public static WebDriver getDriver(String browserName) {
        WebDriver driver = null;  

        switch (browserName.toLowerCase()) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "C:\\Users\\2425304\\Downloads\\chromedriver\\chromedriver-win64\\chromedriver.exe");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");  
                driver = new ChromeDriver(chromeOptions);
                break;

            case "edge":
                System.setProperty("webdriver.edge.driver", "C:\\Users\\2425304\\Downloads\\edge driver v86\\msedgedriver.exe");
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-notifications");
                driver = new EdgeDriver(edgeOptions);
                break;
                
            default:
                System.out.println("Unsupported browser: " + browserName);
                break;
        }

        return driver;
    }
}
