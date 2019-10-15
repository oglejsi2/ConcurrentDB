/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqn.si.test;

/**
 *
 * @author Robert
 */
public class Test {
    public static void main(String args[]) {
        String loc="Register the following query: select i from advdba.sendtest\n" +
"ADVDBA.SENDTEST is part of the registration.\n" +
"QCNDemoListener: got an event (cqn.si.test.QCNRegister$QCNDemoListener1@666c5482 running on thread Thread[Thread-2,5,main])\n" +
"Connection information  : local=DESKTOP-0CB53RE.advansys.si/10.10.11.131:47632, remote=nexiomobile.advansys.si/10.10.11.105:59631\n" +
"Exception in thread \"Thread-2\" java.lang.StringIndexOutOfBoundsException: String index out of range: -425\n" +
"Registration ID         : 1874\n" +
"Notification version    : 1\n" +
"Event type              : OBJCHANGE\n" +
"Database name           : matador\n" +
"Table Change Description (length=1)\n" +
"    operation=[INSERT], tableName=ADVDBA.SENDTEST, objectNumber=985539\n" +
"    Row Change Description (length=1):\n" +
"      ROW:  operation=INSERT, ROWID=AADwnDAAHAAFMkNAIQ";
        
        String sprRowid;
        sprRowid=loc.substring(loc.indexOf("ROWID=")+6);
        System.out.println(sprRowid);
    }
}
