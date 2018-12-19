/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrentdb.learning.lesson5;

import concurrentdb.learning.lesson4.CallableTask_Lesson4;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert
 * We will implement commit after all tasks finished sucessfully.
 */
public class Lesson_5 {
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
        4       We will commit after all tasks finished sucessfully. To do that we have to pass connection to CallableTask as parameter ->
                credentialsList = locServerList.getCredentials(locConn); creates list of connections to databases where code hast to be executed
        */
        
        Connection locConn = null;
        try {
            locConn = DriverManager.getConnection("jdbc:oracle:thin:@10.10.11.166:1521:advrcp", "robert", "robert");
        } catch (SQLException ex) {
            Logger.getLogger(Lesson_5.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<Connection> locConnList = new ArrayList<Connection>();

        
        Lesson_5 locConcurrentDB = new Lesson_5(); 
        ExecutorService locexecutor = locConcurrentDB.initializeExecutor(10);
        CallableTask_Lesson5 locServerList = new CallableTask_Lesson5();
        
//  Get list of servers                
        List<CallableTask_Lesson5.Credentials> credentialsList;         
        List<CallableTask_Lesson5> locCallableTask1List = new ArrayList<CallableTask_Lesson5>();
                       
//  Loop through list of servers and executeCommand        
        credentialsList = locServerList.getCredentials(locConn);    // list of connections to databases where code hast to be executed

        for (CallableTask_Lesson5.Credentials lCredentials : credentialsList) {
                CallableTask_Lesson5 locCallableTask1 = new CallableTask_Lesson5();         

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
                Logger.getLogger(Lesson_5.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
//  Loop through list of servers and report results + check if every procedure finished successfully
      String isOK="OK";     
      for (CallableTask_Lesson5 locCallableTask : locCallableTask1List) {
          if (!(locCallableTask.getLresult().equalsIgnoreCase("1"))) {
              isOK="ERR";
              System.out.println("result of procedure is: "+locCallableTask.getLresult());                  
          }                        
      }
      
      if (isOK.equalsIgnoreCase("OK")) {
          System.out.println("isOK "+isOK);
          for (CallableTask_Lesson5.Credentials lCredentials : credentialsList) {
              try {
                  lCredentials.conn.commit();
              } catch (SQLException ex) {
                  Logger.getLogger(Lesson_5.class.getName()).log(Level.SEVERE, null, ex);
              }
          }
      }
      

      
    }    
    
}
