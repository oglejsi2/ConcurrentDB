package concurrentdb.push.advansys;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 *
 * @author Robert
 */
public class NotificationTask implements Callable {
    
    /**
     *  Procedure call() is the procedure called by Executor. Executor initializes class with callableTask
     * 
     * @params 
     */            

    Date locDate;
    
    
    
    @Override
    public Object call() throws Exception {       
        Random rand = new Random();   
        int sleepTime=rand.nextInt(5000);       
        Thread.sleep(sleepTime);    
        Date privlocDate=new Date();                      
        locDate=new Date();  
        System.out.println(sleepTime + " " + privlocDate );                                  
        return "Test";
    }
    
    public String getLresult() {
        return "Test " + locDate + " : " + new Date();
    }
}



