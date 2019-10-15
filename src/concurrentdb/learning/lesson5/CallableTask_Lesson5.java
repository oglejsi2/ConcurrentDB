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
public class CallableTask_Lesson5 implements Callable {
    Credentials lCredentials;
            
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
    
    class Credentials  {               
        Connection conn;

        public Connection getConn() {
            return conn;
        }

        public void setConn(Connection conn) {
            this.conn = conn;
        }
           
    }
    
    // Array with server connect info
    
    

        
    public String  executeCommand () {               
        String locSql="{call :result:=CONCURENTTRANSACTIONS.mydata_ins(:value)}";
        CallableStatement preparedCall;
        try {
            this.gcredentialsClass.getConn().setAutoCommit(false);
            preparedCall = this.gcredentialsClass.getConn().prepareCall(locSql);
            preparedCall.registerOutParameter(1, java.sql.Types.VARCHAR);            
            preparedCall.setString(2, "myText");
            preparedCall.execute();
            lresult = preparedCall.getString(1);                        
            preparedCall.close();  
            System.out.println("lresult "+lresult);
        } catch (SQLException ex) {
            Logger.getLogger(CallableTask_Lesson5.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lresult;
    }
    
    public List<CallableTask_Lesson5.Credentials>   getCredentials (Connection parConn) {
            List<CallableTask_Lesson5.Credentials> locCredentialsList = new ArrayList<CallableTask_Lesson5.Credentials>();
            CallableTask_Lesson5.Credentials locCredentials = new CallableTask_Lesson5.Credentials();            
            try {                          
                parConn.setAutoCommit(false);
                String locServerListSql="select sl_url, sl_username, sl_password from server_list";
                Statement connUrlStat = parConn.createStatement();
                ResultSet connRS = connUrlStat.executeQuery(locServerListSql);            
                
                while (connRS.next()) {
                    locCredentials = new CallableTask_Lesson5.Credentials();
                    locCredentials.conn=DriverManager.getConnection(connRS.getString("sl_url"), connRS.getString("sl_username"), connRS.getString("sl_password"));
                    locCredentialsList.add(locCredentials);
                }
                
                
                connUrlStat.close();
                connRS.close();                
            } catch (SQLException ex) {
                Logger.getLogger(CallableTask_Lesson4.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return locCredentialsList;
            
    }    
    
    
    public Credentials gcredentialsClass;

    public Credentials getGcredentialsClass() {
        return gcredentialsClass;
    }

    public void setGcredentialsClass(Credentials gcredentialsClass) {
        this.gcredentialsClass = gcredentialsClass;
    }
    
 
}
