package RDSDatabase.src.main.java.rdsDatabase;

import RDSDatabase.src.main.java.rdsDatabase.JDBCSample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<SQSEvent, APIGatewayProxyResponseEvent> {
	@Override
	public APIGatewayProxyResponseEvent handleRequest(final SQSEvent event, final Context context) {
		SQSMessage message = null;
		if (event != null) {
			LambdaLogger logger = context.getLogger();
			logger.log("Received event: " + event);
			try {
				message = event.getRecords().get(0);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			if (message != null) {
				String content = message.getBody();
				String msgId =  message.getMessageId();
				logger.log("SQS event triggered and get message: " + content);
				logger.log("SQS event triggered and get message: " + msgId);
				JDBCSample jdbcSample = new JDBCSample();
				String insertStatement = jdbcSample.insertMessage(message, context);
				System.out.println("Lambda called: write some data to database!...");

			}
		}
//    	
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//        headers.put("X-Custom-Header", "application/json");
//
//        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
//                .withHeaders(headers);
//        try {
//            JDBCSample jdbcSample = new JDBCSample();
//            String insertValue = jdbcSample.insertMessage(message, context);
//            
//            String output = String.format("{ \"message\": \"hello world\", \"Inserted message\": \"%s\" }", insertValue);
//
//            return response
//                    .withStatusCode(200)
//                    .withBody(output);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return response
//                    .withBody("{}")
//                    .withStatusCode(500);
//        }

		System.out.println("Lambda called but some thing when wrong!");
		System.out.println(event);
		return null;
	}

}
