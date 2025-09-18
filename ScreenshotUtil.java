package flipkart;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    public static String takeScreenshot(WebDriver driver, String stepName) {
        // Create a timestamp to make each screenshot filename unique
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Define the screenshots directory
        File screenshotDir = new File("screenshots");
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }

        // Take screenshot and store it in a temporary file
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Define the destination file path
        File destFile = new File(screenshotDir, stepName + "_" + timestamp + ".png");

        try {
            // Copy the screenshot to the destination
            Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved: " + destFile.getAbsolutePath());
            return destFile.getAbsolutePath(); // Return path for embedding in reports
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
            return null;
        }
    }
}
