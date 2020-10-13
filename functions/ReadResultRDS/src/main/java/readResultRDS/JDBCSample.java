package ReadResultRDS.src.main.java.readResultRDS;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class JDBCSample {
	

    public Map<String,String> getAllMessage(Context context) {
        LambdaLogger logger = context.getLogger();
        String url = "jdbc:mysql://db-rdstest.cveiysvnchw0.ap-southeast-1.rds.amazonaws.com:3306";
        String username = "admin";
        String password = "test1234";
        Connection conn = null;
        Statement stmt = null;
        // Get time from DB server
        String keyMsgId = "message_id";
        String keyMsg = "message";
        String msgId = "default";
        String msg = "default";
        
        try {
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Workflow.message_table order by id desc limit 1;");
            
            while(rs.next()){
                //Retrieve by column name
            	 msgId = rs.getString(keyMsgId);
            	 msg = rs.getString(keyMsg);
            	 System.out.println(msgId);
            	 System.out.println(msg);
            }
             rs.close();

        } catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
         }finally{
            //finally block used to close resources
            try{
               if(stmt!=null)
                  conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
               if(conn!=null)
                  conn.close();
            }catch(SQLException se){
               se.printStackTrace();
            }//end finally try
         }//end try
        Map<String,String> result = new HashMap<>();
        result.put(keyMsgId, msgId);
        result.put(keyMsg, msg);
        return result;
    }
}