/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrentdb.learning.lesson2;

import concurrentdb.learning.lesson1.*;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert
 */
public class CallableTask_Lesson2 implements Callable {
    
    /**
     *  Procedure call() is the procedure called by Executor. Executor initializes class with callableTask
     * 
     * @params 
     */            
    
    public CallableTask_Lesson2(String pisOK) {
        this.setIsOK(pisOK);
    }
    
    String isOK;


    public String getIsOK() {
        return isOK;
    }

    public void setIsOK(String isOK) {
        this.isOK = isOK;
    }
    

    @Override
    public Object call() throws Exception {
        Date locDateStart = new Date();
        Random rand = new Random();
        int nextInt = rand.nextInt(5000);        
        try {
            Thread.sleep(nextInt);
        } catch (InterruptedException ex) {
            Logger.getLogger(CallableTask_Lesson2.class.getName()).log(Level.SEVERE, null, ex);
        }
        Date locDateStop = new Date();
        this.setIsOK(this.getIsOK() +" : "+ nextInt +" : " +  locDateStart + " : " + locDateStop);
        return isOK;
    }


    @Override
    public String toString() {                
        return this.isOK;
    }

    
}



