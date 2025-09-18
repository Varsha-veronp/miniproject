package flipkart;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.*;
 
public class ExtentReportListener implements ITestListener {
 
    private ExtentReports extent;
    private ExtentTest test;
 
    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Project", "Flipkart");
        extent.setSystemInfo("Tester", "Varsha P");
    }
 
    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
    }
 
    @Override 
    public void onTestSuccess(ITestResult result) {
        test.pass("Test Passed");
    }
 
    @Override
    public void onTestFailure(ITestResult result) {
        test.fail("Test Failed: " + result.getThrowable());
    }
 
    @Override
    public void onTestSkipped(ITestResult result) {
        test.skip("Test Skipped");
    }
 
    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}