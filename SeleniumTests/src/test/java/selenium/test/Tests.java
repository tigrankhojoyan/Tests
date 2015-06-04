/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selenium.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import static org.testng.Assert.*;
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
    WebDriver driver;
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @BeforeMethod
    public void setUpMethod() throws Exception {
        driver = new FirefoxDriver();
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        driver.quit();
    }

    @Test
    public void testOne() throws InterruptedException {
        driver.get("https://maps.google.com/");
        Thread.sleep(5000);
    }

    @Test
    public void testTwo() throws InterruptedException {
        driver.get("https://www.yahoo.com/");
        Thread.sleep(5000);
    }

    @Test
    public void testThree() throws InterruptedException {
        driver.get("http://www.eurosport.ru/");
        Thread.sleep(5000);
    }
}
