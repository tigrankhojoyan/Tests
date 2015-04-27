/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testng.tests;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer  { 
private int count = 0; 
private int maxCount = 3; // set your count to re-run test
protected Logger log;
private static Logger testbaseLog;

static {
    PropertyConfigurator.configure("test-config/log4j.properties");
    testbaseLog = Logger.getLogger("TestclassName");
}

public RetryAnalyzer()
{
    testbaseLog.trace( " ModeledRetryAnalyzer constructor " + this.getClass().getName() );
    log = Logger.getLogger("TestclassName");
}

@Override 
public boolean retry(ITestResult result) { 
    testbaseLog.trace("running retry logic for  '" 
            + result.getName() 
            + "' on class " + this.getClass().getName() );
        if(count < maxCount) {                     
                count++;                                    
                return true; 
        } 
        return false; 
}
}


