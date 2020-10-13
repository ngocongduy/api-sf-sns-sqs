package RDSDatabase.src.main.java.rdsDatabase;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCSample {

	public String insertMessage(SQSMessage message, Context context) {
		LambdaLogger logger = context.getLogger();

		String url = "jdbc:mysql://db-rdstest.cveiysvnchw0.ap-southeast-1.rds.amazonaws.com:3306";
		String username = "admin";
		String password = "test1234";

		Connection conn = null;
		Statement stmt = null;
		String insertStmt = "";
		// Get time from DB server
		try {
			conn = DriverManager.getConnection(url, username, password);
			stmt = conn.createStatement();
			String msgId = "DefaultMsgId";
			String msgBody = "Default body";
			if (message != null) {
				msgId = message.getMessageId();
				msgBody = message.getBody();
			}

			insertStmt = "INSERT INTO Workflow.message_table (message_id, message) VALUES ";
			insertStmt += "('" + msgId + "','" + msgBody + "');";
			System.out.println(insertStmt);
			stmt.executeUpdate(insertStmt);

			logger.log("Successfully executed query.  Result: " + insertStmt);

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
//			try {
//				if (stmt != null)
//					conn.close();
//			} catch (SQLException se) {
//			} // do nothing
//			try {
//				if (conn != null)
//					conn.close();
//			} catch (SQLException se) {
//				se.printStackTrace();
//			} // end finally try
		} // end try
		return insertStmt;
	}

}