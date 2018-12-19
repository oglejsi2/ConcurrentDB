/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrentdb.learning.lesson4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert
 * In this lesson we will modify CallableTask. It will call database procedures defined in lesson3
 * java procedure getCredentials will read the connection strings, usernames, passwords from SERVER_LIST 
 * executeCommand will call CONCURENTTRANSACTIONS.mydata_ins which will insert data into mydata
 * We have not yet achieved our goal which is commit after all tasks finished sucessfully. locConn.commit() is executed within Callable task
 * 
 * 
 */
public class Lesson_4 {
    public ExecutorService initializeExecutor(int pPoolSize ) {
        ExecutorService executor = Executors.newFixedThreadPool(pPoolSize);
        return executor; 
    }

    public static void main (String[] args) {
        /*        
        1       Initialize executor
                ExecutorService locexecutor = locConcurrentDB.initializeExecutor(10);
        2       Connect to database and for each server listed in SERVER_LIST and  execute submit -> CallableTask_Lesson4.submit() : locServerList.submit()
        3       shutdown executor - it won't shutdown, it will finish the jobs and then shutdown
        4       We have not yet achieved our goal which is commit after all tasks finished sucessfully. locConn.commit() is executed within Callable task        
        */
        
        Lesson_4 locConcurrentDB = new Lesson_4(); 
        ExecutorService locexecutor = locConcurrentDB.initializeExecutor(10);
        CallableTask_Lesson4 locServerList = new CallableTask_Lesson4();                                
        
//  Get list of servers        
        List<CallableTask_Lesson4.Credentials> credentialsList = locServerList.getCredentials("jdbc:oracle:thin:@10.10.11.166:1521:advrcp", "robert", "robert");    
        List<CallableTask_Lesson4> locCallableTask1List = new ArrayList<CallableTask_Lesson4>();           
                       
//  Loop through list of servers and executeCommand        
        for (CallableTask_Lesson4.Credentials lCredentials : credentialsList) {                
                CallableTask_Lesson4 locCallableTask1 = new CallableTask_Lesson4();
                
                locCallableTask1List.add(locCallableTask1);
                
                locCallableTask1.setGcredentialsClass(lCredentials);
                locexecutor.submit(locCallableTask1);                                           
        }        

        locexecutor.shutdown();
        while (!locexecutor.isTerminated()) {      
            try {  
//   If one runs this code from within database, the database will burn the procesor -> let it sleep for a while.                
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Lesson_4.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        
//  Loop through list of servers and report results 

        System.out.println("test");
        
      for (CallableTask_Lesson4 locCallableTask : locCallableTask1List) {
          System.out.println("result of procedure is: "+locCallableTask.getLresult());
      }
    }
}
