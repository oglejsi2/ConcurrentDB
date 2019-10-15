/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrentdb.push.advansys;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert
 */
public class ProcessNotification {
    public ExecutorService initializeExecutor(int pPoolSize ) {
        ExecutorService executor = Executors.newFixedThreadPool(pPoolSize);
        return executor; 
    }
    
    public static void main (String[] args) {
        try {
            ProcessNotification locProcessNotification = new ProcessNotification();
            ExecutorService locexecutor = locProcessNotification.initializeExecutor(10);
            NotificationTask locNotificationTask = new NotificationTask();
            NotificationTask locNotificationTask1 = new NotificationTask();
            
            
            Future b = locexecutor.submit(locNotificationTask);
            Future c = locexecutor.submit(locNotificationTask1);
            
            locexecutor.shutdown();
            
            while (!locexecutor.isTerminated()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcessNotification.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            System.out.println("Finished");
            System.out.println(locNotificationTask.getLresult());
            System.out.println(locNotificationTask1.getLresult());
            System.out.println("b_get " +b.get());
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessNotification.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(ProcessNotification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
