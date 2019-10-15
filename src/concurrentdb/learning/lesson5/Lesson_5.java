package concurrentdb.learning.lesson5;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;






public class Lesson_5
{
  int locI=0;
  
  public Lesson_5() {}
  
  public ExecutorService initializeExecutor(int pPoolSize)
  {
    ExecutorService executor = Executors.newFixedThreadPool(pPoolSize);
    return executor;
  }

  public static void main(String[] args)
  {
    Connection locConn = null;
    try {
      locConn = DriverManager.getConnection("jdbc:oracle:thin:@10.10.11.186:1521:advrcp", "robert", "robert");
    } catch (SQLException ex) {
      Logger.getLogger(Lesson_5.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    List<Connection> locConnList = new ArrayList();
    

    Lesson_5 locConcurrentDB = new Lesson_5();
    ExecutorService locexecutor = locConcurrentDB.initializeExecutor(10);
    CallableTask_Lesson5 locServerList = new CallableTask_Lesson5(); 

    List<CallableTask_Lesson5> locCallableTask1List = new ArrayList();
    

    List<CallableTask_Lesson5.Credentials> credentialsList = locServerList.getCredentials(locConn);
    
    for (Iterator localIterator = credentialsList.iterator(); localIterator.hasNext();) { 
      
      
      locServerList.lCredentials = (CallableTask_Lesson5.Credentials)localIterator.next();
      CallableTask_Lesson5 locCallableTask1 = new CallableTask_Lesson5();
      
      locCallableTask1List.add(locCallableTask1);
      locCallableTask1.setGcredentialsClass(locServerList.lCredentials);
      
      locConcurrentDB.locI++;
      System.out.println("1 "+locConcurrentDB.locI);
      
      locexecutor.submit(locCallableTask1);
//      System.out.println(locCallableTask1.lCredentials.toString());
    }
//    CallableTask_Lesson5.Credentials lCredentials;
    locexecutor.shutdown();
    while (!locexecutor.isTerminated()) {
      try
      {
        Thread.sleep(1000);
      } catch (InterruptedException ex) {
        Logger.getLogger(Lesson_5.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    

    String isOK = "OK";
    for (CallableTask_Lesson5 locCallableTask : locCallableTask1List) {
      System.out.println("row 82 "+locCallableTask.getLresult());
      if (!locCallableTask.getLresult().equalsIgnoreCase("1")) {
        isOK = "ERR";
        System.out.println("result of procedure is: " + locCallableTask.getLresult());
      }
    }
    
    if (isOK.equalsIgnoreCase("OK")) {      
      for (CallableTask_Lesson5.Credentials lCredentials : credentialsList) {
          System.out.println("isOK " + isOK);
        try {
          locServerList.lCredentials.conn.commit();
        } catch (SQLException ex) {
          Logger.getLogger(Lesson_5.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }
}