package concurrentdb.learning.lesson5;

import concurrentdb.learning.lesson4.CallableTask_Lesson4;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;






public class Lesson_5V1
{
  int locI=0;
  
  
  
  public ExecutorService initializeExecutor(int pPoolSize)
  {
    ExecutorService executor = Executors.newFixedThreadPool(pPoolSize);
    return executor;
  }

    public List<Connection>   getCredentials (Connection parConn) {
            List<Connection> locCredentialsList = new ArrayList<Connection>();
            Connection locCredentials;
            try {                          
                parConn.setAutoCommit(false);
                String locServerListSql="select sl_url, sl_username, sl_password from server_list";
                Statement connUrlStat = parConn.createStatement();
                ResultSet connRS = connUrlStat.executeQuery(locServerListSql);            
                
                while (connRS.next()) {                    
                    locCredentials=DriverManager.getConnection(connRS.getString("sl_url"), connRS.getString("sl_username"), connRS.getString("sl_password"));
                    locCredentialsList.add(locCredentials);
                }
                
                
                connUrlStat.close();
                connRS.close();                
            } catch (SQLException ex) {
                Logger.getLogger(CallableTask_Lesson4.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return locCredentialsList;
            
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
    

    Lesson_5V1 locConcurrentDB = new Lesson_5V1();
    ExecutorService locexecutor = locConcurrentDB.initializeExecutor(10);
    CallableTask_Lesson5_V1 locServerList = new CallableTask_Lesson5_V1(); 

    List<CallableTask_Lesson5_V1> locCallableTask1List = new ArrayList();
    

    List<Connection> credentialsList =locConcurrentDB.getCredentials(locConn);   
            
    
    
    for (Iterator localIterator = credentialsList.iterator(); localIterator.hasNext();) { 
      
      
      locServerList.lCredentials = (Connection)localIterator.next();
      CallableTask_Lesson5_V1 locCallableTask1 = new CallableTask_Lesson5_V1();
      
      locCallableTask1List.add(locCallableTask1);
      locCallableTask1.setlCredentials(locServerList.lCredentials);
      
      
      locConcurrentDB.locI++;
      
      locexecutor.submit(locCallableTask1);
//      System.out.println(locCallableTask1.lCredentials.toString());
    }
//    CallableTask_Lesson5_V1.Credentials lCredentials;
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
    for (CallableTask_Lesson5_V1 locCallableTask : locCallableTask1List) {
      if (!locCallableTask.getLresult().equalsIgnoreCase("1")) {
        isOK = "ERR";
      }
    }
    
    if (isOK.equalsIgnoreCase("OK")) {      
      for (Connection lCredentials : credentialsList) {
        try {
          lCredentials.commit();
        } catch (SQLException ex) {
          Logger.getLogger(Lesson_5.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }
}