/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testng.tests;

import java.io.FileWriter;
import java.io.IOException;
import junit.framework.Assert;
import static org.testng.Assert.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author tigran
 */
public class Tests {
    
    public String fileName = "src/main/resources/testsShortStatus.txt";
    
    /**
     * Writes the given data into the file. If the file does not exist, it will be created.
     * @param testCaseStatus
     * @param testCaseName
     * @param fileName 
     */
  public void writeDataIntoFile(String testCaseStatus, String testCaseName, String fileName) {
    try {
      FileWriter fw = new FileWriter(fileName, true);
      fw.write(testCaseName + " - " + testCaseStatus + "\n");
      fw.close();
    } catch (IOException ioe) {
      System.err.println("IOException: " + ioe.getMessage());
    }
  }

    public Tests() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        String testCaseStatus = "Pass";
        if (result.getStatus() == ITestResult.FAILURE) {
            //your screenshooting code goes here
            testCaseStatus = "Fail";
        }
        writeDataIntoFile(testCaseStatus, result.getMethod().getMethodName(), fileName);
    }

    @Test
    public void test1() {
        Assert.assertTrue(true);
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void test2() {
        Assert.assertTrue(false);
    }
}
