/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Robert
 * Created: 14-Dec-2018
 */
/**
*   This procedure is all about database. We will show how we can execute commit if all tasks executed successfully
*   
*   Lets create some tables and packages.
*
*   :hostname       -'jdbc:oracle:thin:@ip:1521:orcl'
*   :username       
*   :password       
*/

-- Connect to databases and execute the scripts


-- Table with credentials to databases.
  CREATE TABLE "SERVER_LIST" 
   (	"SL_URL" VARCHAR2(500 BYTE), 
	"SL_USERNAME" VARCHAR2(500 BYTE), 
	"SL_PASSWORD" VARCHAR2(500 BYTE)
   );


-- Don't forget to change orcl and ip in hostname. You have to enter every database you will be using in test
    insert into server_list(sl_url,	sl_username,	sl_password)
    values (:hostname,:username,:password)
    ;

    insert into server_list(sl_url,	sl_username,	sl_password)
    values (:hostname,:username,:password)
    ;



    commit;


-- Table with data we will insert with executor
   create table mydata
    (   text varchar2(500), 
        ins_date date
    );


-- This is declaration of packege with the procedure we will call in executor
create or replace PACKAGE CONCURENTTRANSACTIONS AS 
	function mydata_ins(p_text in varchar2) return number ;
END CONCURENTTRANSACTIONS;
/

-- This is packege with the procedure we will call in executor
create or replace PACKAGE BODY CONCURENTTRANSACTIONS AS
	function mydata_ins(p_text in varchar2) return number as
	/*
		return -1 - error
		return 1 - ok
	*/
	locReturn number;	
	begin
		locReturn:=-1;
		insert into mydata(text, ins_date)
		values (p_text, sysdate);
		locReturn:=1;
		return locReturn;		
	end;

END CONCURENTTRANSACTIONS;
/



-- Test the function. This will enter a row in table
DECLARE
  P_TEXT VARCHAR2(200);
  v_Return NUMBER;
BEGIN
  P_TEXT := 'a';

  v_Return := CONCURENTTRANSACTIONS.MYDATA_INS(
    P_TEXT => P_TEXT
  );
DBMS_OUTPUT.PUT_LINE('v_Return = ' || v_Return);
END;
/


-- Check if row is in the table
select *
from mydata;