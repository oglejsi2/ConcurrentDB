/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrentdb.learning.lesson5;


import concurrentdb.learning.lesson4.CallableTask_Lesson4;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert
 */
public class CallableTask_Lesson5_V1 implements Callable {
    Connection lCredentials;

    public Connection getlCredentials() {
        return lCredentials;
    }

    public void setlCredentials(Connection lCredentials) {
        this.lCredentials = lCredentials;
    }
            
    @Override
    public Object call() throws Exception {
        String lexecuteCommand = executeCommand();
        return lexecuteCommand;
    }
    
    @Override
    public String toString() {
        return this.getLresult();
        //return "return";
    }
    
    
    
    
    String lresult;    

    public String getLresult() {
        return lresult;
    }

    public void setLresult(String lresult) {
        this.lresult = lresult;
    }
    
    // Inner class with credential data
    

    // Array with server connect info
    
    

        
    public String  executeCommand () {               
        String locSql="{call :result:=CONCURENTTRANSACTIONS.mydata_ins(:value)}";
        CallableStatement preparedCall;
        try {
            System.out.println("executeCommand "+lCredentials.getMetaData().getURL().toString());
            lCredentials.setAutoCommit(false);
            preparedCall = lCredentials.prepareCall(locSql);
            preparedCall.registerOutParameter(1, java.sql.Types.VARCHAR);            
            preparedCall.setString(2, "myText");
            preparedCall.execute();
            lresult = preparedCall.getString(1);                        
            preparedCall.close();  
            System.out.println("lresult v1 "+lresult);
        } catch (SQLException ex) {
            Logger.getLogger(CallableTask_Lesson5_V1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lresult;
    }
    
   
    
    
    public Connection gcredentialsClass;



    
 
}
