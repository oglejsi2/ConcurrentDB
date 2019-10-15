/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrentdb.learning.lesson2;

import concurrentdb.learning.lesson1.*;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert
 */
public class Lesson_2 {

    /**
     * @param args the command line arguments
     * 
     * In this session we add two important features
     * 1.   Parameter in call to the CallableTask_Lesson2 class and call to the CallableTask_Lesson2
     * 2.   Sleep time in the CallableTask_Lesson2.     
     * We need these two parameters to show how tasks are started. Both tasks are started at the same time. One with the larger number after OK or ERR, finishes after the other. 
     * Although we made shutdown of Executer, it waits for both tash to end and then continues.
     */
    
    
    
    public ExecutorService initializeExecutor(int pPoolSize ) {
        ExecutorService executor = Executors.newFixedThreadPool(pPoolSize);
        return executor; 
    }
            
    
    public static void main(String[] args) {
    /*
        Steps:
        0.  Change CallableTask_Lesson2. Call method 
        1.  Initialize executor
        2.  CallableTask_Lesson2.submit -> ExecutorService executes "call" procedure in Callable class        
        3.  Let's make another CallableTask CallableTask_Lesson2 with initial parameter err
        4.  ExecutorService.shutdown(); sends signal to shutdown executor. Executor won't stop. It will continue to execute until last task finishes.
        5.  Thread.sleep(1000);                                                       // If one runs this code from within database, the database will burn the procesor -> let it sleep for a while.
        
        
        ExecutorSrvice runs the call method but we don't see any result. We can get result by implementing toString method in CallableTask_Lesson2.java
    */
        Lesson_2 locConcurrentDB = new Lesson_2();                  
//1     Initialize executor
        ExecutorService locexecutor = locConcurrentDB.initializeExecutor(10);
        CallableTask_Lesson2 locCallableTask = new CallableTask_Lesson2("OK");
        
//2.  CallableTask_Lesson2.submit -> ExecutorService executes "call" procedure in Callable class                                
        Date locDateStart = new Date();
        locexecutor.submit(locCallableTask);
        
//3.  Let's make another CallableTask CallableTask_Lesson2        
        
        CallableTask_Lesson2 locCallableTask1 = new CallableTask_Lesson2("ERR");
        locexecutor.submit(locCallableTask1);
        
//4.  ExecutorService.shutdown(); sends signal to shutdown executor. Executor won't stop. It will continue to execute until last task finishes.        
        locexecutor.shutdown();
        while (!locexecutor.isTerminated()) {      
            try {  
//5.   If one runs this code from within database, the database will burn the procesor -> let it sleep for a while.                
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Lesson_2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
        
        Date locDateStop = new Date();
        
        System.out.println(locDateStart);
        System.out.println(locCallableTask);
        System.out.println(locCallableTask1);
        System.out.println(locDateStop);
    }    
}
