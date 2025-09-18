package flipkart;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions; //implicit wait 
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File; 
import java.io.IOException; 
import java.time.Duration;  //wokring with time intervals 
import java.util.List;
import java.util.Scanner;

public class FlipkartAutomation {
	
	WebDriver driver; 
	ExcelSet excelSet;
    
    @BeforeClass     //invoked first before all the class gets invoked 
    public void setup() {
    
        excelSet = new ExcelSet();
        driver = new ChromeDriver();

        
    }

 @Test    
 public void testFlipkart() throws InterruptedException {
	 
  }  
    @AfterClass  //Executes after all test methods in the current class.
    public void tearDown() {
        driver.quit();
        excelSet.saveFile("report.xlsx"); 
    }
    
    static class ScreenshotUtil {  
        public static void takeScreenshot(WebDriver driver, String fileName) {
            try {
                TakesScreenshot ts = (TakesScreenshot) driver; //invoke ss 
                File srcFile = ts.getScreenshotAs(OutputType.FILE); //temprory file and setting the type
                File destDir = new File("./screenshots"); //save in scrennshot folder 
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                File destFile = new File(destDir, fileName + ".png");
                FileHandler.copy(srcFile, destFile); //copying temparory file in destination file 
                System.out.println("Screenshot saved to: " + destFile.getAbsolutePath());  
            } catch (IOException | WebDriverException e) {
                System.err.println("Error taking screenshot: " + e.getMessage());
            } 
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the Browser (chrome/edge): ");  
        String browser = sc.nextLine().trim();
        sc.close();

        WebDriver driver = null;
        ExcelSet excelSet = new ExcelSet();

        try {
            driver = DriverSetup.getDriver(browser);
            if (driver == null) {
                System.err.println("Driver initialization failed for " + browser);
                excelSet.writeData("Driver Setup", "Fail", "Driver initialization failed for " + browser);
                return;
            }
            
            System.out.println("Launching " + browser + " browser.");
            driver.manage().window().maximize();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3)); 

            driver.get("https://www.flipkart.com");
            System.out.println("Navigated to Flipkart homepage.");
            excelSet.writeData("Homepage Navigation", "Pass", "Navigated to Flipkart homepage.");
//popup occur 
            try {
                WebElement closePopup = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button._2KpZ6l._2doB4z")));
                closePopup.click();
                System.out.println("Login popup was closed successfully.");
                excelSet.writeData("Login Popup", "Pass", "Login popup was closed successfully.");
            } catch (Exception e) {
                System.out.println("No login popup appeared.");
                excelSet.writeData("Login Popup", "Pass", "No login popup appeared.");
            }
//search bar 
            WebElement search = driver.findElement(By.className("Pke_EE")); //search bar 
            search.sendKeys("mobiles");
            System.out.println("Entered 'mobiles' in the search bar.");
            excelSet.writeData("Search Input", "Pass", "Entered 'mobiles' in the search bar.");

            search.sendKeys(Keys.ENTER);
            System.out.println("Search executed successfully.");
            excelSet.writeData("Search Execution", "Pass", "Search executed successfully.");

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/div/div[3]/div/div[1]/div/div[1]/div/section[2]/div[2]/div/div[2]")));
   //filtering the amount 
            driver.findElement(By.className("tKgS7w")).click(); //price filter 
            Thread.sleep(3000);   
            
            System.out.println("Clicked on a filter element (class: vVygGr, tKgS7w)."); //price dragger,filter 
            excelSet.writeData("Filter Click", "Pass", "Clicked on a filter element.");
//minimum amount selection 
            try {
                driver.findElement(By.xpath("//select")).click();
                driver.findElement(By.xpath("//*[@id=\"container\"]/div/div[3]/div/div[1]/div/div[1]/div/section[2]/div[4]/div[1]/select")).click();
                System.out.println("Selected minimum amount.");
                excelSet.writeData("Price Filter Min", "Pass", "Selected minimum amount.");
//price filter 
                driver.findElement(By.xpath("//*[@id=\"container\"]/div/div[3]/div/div[1]/div/div/div/section[2]/div[4]/div[3]/select/option[1]")).click();
                System.out.println("Applied price filter.");
                excelSet.writeData("Price Filter Max", "Pass", "Applied price filter.");
            } catch (Exception e) {
                System.err.println("Price filter not applied. Error: " + e.getMessage());
                ScreenshotUtil.takeScreenshot(driver, "price_filter_error");
                excelSet.writeData("Price Filter", "Fail", "Price filter not applied. Error: " + e.getMessage());
            }
//newest first 
            try {
                WebElement newestFirst = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Newest First']")));
                newestFirst.click();
                System.out.println("Sorted by 'Newest First'.");
                Thread.sleep(5000); 
                excelSet.writeData("Sort By", "Pass", "Sorted by 'Newest First'.");
            } catch (Exception e) {
                System.err.println("Unable to sort by 'Newest First'. Error: " + e.getMessage());
                ScreenshotUtil.takeScreenshot(driver, "sort_error");
                excelSet.writeData("Sort By", "Fail", "Unable to sort by 'Newest First'. Error: " + e.getMessage());
             }

            List<WebElement> mobileNames = driver.findElements(By.className("KzDlHZ"));//mobile names 
            List<WebElement> mobilePrices = driver.findElements(By.className("hl05eU"));// mobile price 

            System.out.println("\nTop 5 Mobiles:");
            for (int i = 0; i < Math.min(5, mobileNames.size()); i++) {
                String name = mobileNames.get(i).getText();
                String price = (i < mobilePrices.size()) ? mobilePrices.get(i).getText() : "Price not found";  
                String[] lines = price.split("\n");
                String actualPrice = lines[0];
                System.out.println((i + 1) + ". " + name + " - " + actualPrice);
            }

            if (!mobilePrices.isEmpty()) {
                String firstPriceText = mobilePrices.get(0).getText();
                String[] lines = firstPriceText.split("\n");
                String actualPriceLine = lines[0];
                String cleanedPrice = actualPriceLine.replaceAll("[^\\d]", ""); 

                try {
                    int priceValue = Integer.parseInt(cleanedPrice);
                    if (priceValue < 10000) {
                        System.out.println("First mobile price is less than ₹10,000 (" + priceValue + ")");
                        excelSet.writeData("Price Validation", "Pass", "First mobile price is ₹" + priceValue);
                    } else {
                        System.err.println("First mobile price is ₹" + priceValue + ", which is not less than ₹10,000");
                        ScreenshotUtil.takeScreenshot(driver, "price_validation_fail");
                        excelSet.writeData("Price Validation", "Fail", "First mobile price is ₹" + priceValue);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing price: " + actualPriceLine);
                    ScreenshotUtil.takeScreenshot(driver, "price_parse_error");
                    excelSet.writeData("Price Validation", "Fail", "Error parsing price: " + actualPriceLine);
                }
            } 
            
            
            else {
                System.out.println("No mobile prices found to validate.");
                excelSet.writeData("Price Validation", "Fail", "No mobile prices found to validate.");
            }

        } catch (Exception e) {
            System.err.println("An unexpected error occurred during automation: " + e.getMessage());
            ScreenshotUtil.takeScreenshot(driver, "unexpected_error");
            excelSet.writeData("Overall Test Execution", "Fail", "Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("Browser closed.");
            }

            excelSet.saveFile("FlipkartTestReport.xlsx");
        }
    }
}
