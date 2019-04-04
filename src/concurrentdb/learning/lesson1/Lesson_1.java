/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrentdb.learning.lesson1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Robert
 */
public class Lesson_1 {

    /**
     * @param args the command line arguments
     * This lesson shows how executor works
     * 1. We start with initialize executor
     * 2. submit Callable Task tells executor to start the task specified in parameter
     * 3. shutdown executor. If you don't shutdown executor it will wait for new tasks. 
     */
    
    
    
    public ExecutorService initializeExecutor(int pPoolSize ) {
        ExecutorService executor = Executors.newFixedThreadPool(pPoolSize);
        return executor; 
    }
            
    
    public static void main(String[] args) {
    /*
        Steps:
        1.  Initialize executor
        2.  CallableTask_Lesson1 is a class which extends Callable. ExecutorService executes "call" procedure in Callable class
        3.  ExecutorService.shutdown(); sends signal to shutdown executor. Executor won't stop. It will continue to execute until last task finishes.
        4. 
    */
        Lesson_1 locConcurrentDB = new Lesson_1();                  
        ExecutorService locexecutor = locConcurrentDB.initializeExecutor(10);
        CallableTask_Lesson1 locCallableTask = new CallableTask_Lesson1();
                        
        locexecutor.submit(locCallableTask);
        
        locexecutor.shutdown();
        while (!locexecutor.isTerminated()) {      
                System.out.println("test");
        }
    }
}
