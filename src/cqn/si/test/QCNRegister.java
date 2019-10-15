/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqn.si.test;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.dcn.QueryChangeDescription;
import oracle.jdbc.dcn.RowChangeDescription;

/**
 *
 * @author Robert
 */
public class QCNRegister { 

    
    
    /**
     * Create new registration
     * 
     * @param  url  an absolute URL giving the base location of the image
     * @param  name the location of the image, relative to the url argument
     * @return      the image at the specified URL
     * @see         Image
     */    
        
    
  /**
   * Creates a connection the database.
   */
    
  
        
  
    /**
     * Create new connection to database
     * 
     * @param  pUrl     Url for database connection jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=yourhost.yourdomain.com)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=yourservicename))
     * @param  pUser    Username
     * @param  pPasswd  Password
     * @return Database connection
     * @see         Image
     */    
  OracleConnection connect(String pUrl, String pUser, String pPasswd) throws SQLException
  {
    OracleDriver dr = new OracleDriver();
    Properties prop = new Properties();
    prop.setProperty("user", pUser);
    prop.setProperty("password", pPasswd);
    return (OracleConnection)dr.connect(pUrl,prop);    
  }    
    
  
    /**
     * Register Continous Query Notification
     * 
     * @param  pUrl     Url for database connection jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=yourhost.yourdomain.com)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=yourservicename))
     * @param  pUser    Username
     * @param  pPasswd  Password
     * @return Database connection
     * @see         Image
     */    
    public  DatabaseChangeRegistration register(OracleConnection pConn ) throws SQLException {      
            Properties prop = new Properties();
        
            // if connected through the VPN, you need to provide the TCP address of the client.
            // For example:
            // prop.setProperty(OracleConnection.NTF_LOCAL_HOST,"10.10.10.101");                
                
            prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS,"true");

            // Activate "query" change notification as opposed to the "table" change notification:
            prop.setProperty(OracleConnection.DCN_QUERY_CHANGE_NOTIFICATION,"true");            
            
            /*
                The following operation does a roundtrip to the database to create a new
                registration for DCN. It sends the client address (ip address and port) that
                the server will use to connect to the client and send the notification
                when necessary. Note that for now the registration is empty (we haven't registered
                any table). This also opens a new thread in the drivers. This thread will be
                dedicated to DCN (accept connection to the server and dispatch the events to 
                the listeners).
            */
            return pConn.registerDatabaseChangeNotification(prop);                    

    }
    
    public void addListener(OracleConnection pConn, DatabaseChangeRegistration pDcr) throws SQLException {
            
                        
            //pConn.unregisterDatabaseChangeNotification(1883); // če kateri obtiči
            pConn.unregisterDatabaseChangeNotification(943); // če kateri obtiči


            QCNDemoListener1 list;
            list = new QCNDemoListener1(this);
            pDcr.addListener(list);
            Statement stmt = pConn.createStatement();
                 // associate the statement with the registration:
                 //String query = "select i from advdba.sendtest";
                 String query = "select i from sendtest where i=1";
                 System.out.println("Register the following query: "+query);
                 ((OracleStatement)stmt).setDatabaseChangeRegistration(pDcr);
                 ResultSet rs = stmt.executeQuery(query);
                 while (rs.next())
                 {}

                 String[] tableNames = pDcr.getTables();
                 for(int i=0;i<tableNames.length;i++)
                   System.out.println(tableNames[i]+" is part of the registration.");
                 rs.close();
                 stmt.close();            
                

    }
    
    public static void main(String args[])  {
        OracleConnection conn;
        QCNRegister locQCNRegister = new QCNRegister();
        
        try {
            
            /*
            Connect to database
            */
                conn = locQCNRegister.connect(args[0],args[1], args[2]);  
                
            /*
            On database: regiser empty database change registration
            */
                DatabaseChangeRegistration dcr = locQCNRegister.register(conn);
                
            /*
            Open DatabaseChangeListener. It will listen for database changes
             */
            locQCNRegister.addListener(conn,dcr);
                    
            //conn.unregisterDatabaseChangeNotification(dcr);
            
            
            
            /*
                Close database connection
            */
                conn.close();            
        }   catch(ArrayIndexOutOfBoundsException ignored) {
            System.out.println("Enter username and password and url jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=yourhost.yourdomain.com)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=yourservicename)) for database");            
        } catch (SQLException ex) {
          Logger.getLogger(QCNRegister.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    /**
     * Databace change listener: it prints out the event details in stdout.
     */
    class QCNDemoListener1 implements DatabaseChangeListener
    {
      QCNRegister demo;
      QCNDemoListener1(QCNRegister dem)
      {
        demo = dem;
      }

/*
      public void onDatabaseChangeNotification(DatabaseChangeEvent e)
      {
        Thread t = Thread.currentThread();
        System.out.println("QCNDemoListener: got an event ("+this+" running on thread "+t+")");
        System.out.println(e.toString());
        System.out.println("*******************");
        System.out.println("getRegId: "+e.getRegId());
        System.out.println("getTableChangeDescription");                      
        System.out.println(e.toString().indexOf("ROWID="));
        System.out.println(e.toString().substring(e.toString().indexOf("ROWID=")+6));        
        System.out.println("*******************");
        synchronized( demo ){ demo.notify();}
      }
*/
      
      public void onDatabaseChangeNotification(DatabaseChangeEvent e)
      {
        Thread t = Thread.currentThread();    
        System.out.println("rowid "+e.toString().substring(e.toString().indexOf("ROWID=")+6));        
      }
      
    }    

}


