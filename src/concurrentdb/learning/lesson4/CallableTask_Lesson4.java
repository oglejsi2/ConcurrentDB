/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrentdb.learning.lesson4;

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
public class CallableTask_Lesson4 implements Callable {

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
        String database;
        String userName;
        String Password;
        

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }        

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }
           
    }
    
    // Array with server connect info
    
    
    public List<Credentials>   getCredentials (String pDatabase, String pUserName, String pPassword) {
            List<Credentials> locCredentialsList = new ArrayList<Credentials>();
            Credentials locCredentials = new Credentials();
            Connection conn;
            try {           
                conn = DriverManager.getConnection(pDatabase, pUserName, pPassword);        
                conn.setAutoCommit(false);
                String locServerListSql="select sl_url, sl_username, sl_password from server_list";
                Statement connUrlStat = conn.createStatement();
                ResultSet connRS = connUrlStat.executeQuery(locServerListSql);            
                
                while (connRS.next()) {
                    locCredentials = new Credentials();
                    locCredentials.setDatabase(connRS.getString("SL_URL"));
                    locCredentials.setUserName(connRS.getString("SL_USERNAME"));
                    locCredentials.setPassword(connRS.getString("SL_PASSWORD"));
                    locCredentialsList.add(locCredentials);
                }
                
                
                connUrlStat.close();
                connRS.close();
                conn.close();
            } catch (SQLException ex) {
                conn=null;
                Logger.getLogger(CallableTask_Lesson4.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return locCredentialsList;
            
    }
        
    public String  executeCommand () {        
        Connection locConn;
        String locSql="{call :result:=CONCURENTTRANSACTIONS.mydata_ins(:value)}";
        CallableStatement preparedCall;        
        try {
            locConn=DriverManager.getConnection(gcredentialsClass.getDatabase(), gcredentialsClass.getUserName(), gcredentialsClass.getPassword());            
            locConn.setAutoCommit(false);
            preparedCall = locConn.prepareCall(locSql);
            preparedCall.registerOutParameter(1, java.sql.Types.VARCHAR);            
            preparedCall.setString(2, "myText");
            preparedCall.execute();
            lresult = preparedCall.getString(1);                        
            preparedCall.close();            
            locConn.commit();
        } catch (SQLException ex) {
            locConn=null;
            Logger.getLogger(CallableTask_Lesson4.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lresult;
    }
    
    
    public Credentials gcredentialsClass;

    public Credentials getGcredentialsClass() {
        return gcredentialsClass;
    }

    public void setGcredentialsClass(Credentials gcredentialsClass) {
        this.gcredentialsClass = gcredentialsClass;
    }
    
    
    public static void main (String[] args) {
        CallableTask_Lesson4 locServerList = new CallableTask_Lesson4();                                
        
//  Get list of servers        
        List<CallableTask_Lesson4.Credentials> credentialsList = locServerList.getCredentials("jdbc:oracle:thin:@10.10.11.134:1521:advrcp", "robert", "robert");    
                       
//  Loop through list of servers and executeCommand        
        for (CallableTask_Lesson4.Credentials lCredentials : credentialsList) {
                locServerList.setGcredentialsClass(lCredentials);
                locServerList.executeCommand();
        }
        
    }
}
