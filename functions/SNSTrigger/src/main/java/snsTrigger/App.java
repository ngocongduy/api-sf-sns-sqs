package SNSTrigger.src.main.java.snsTrigger;

//package snsTrigger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import SNSTrigger.src.main.java.snsTrigger.model.AmazonSNSSQS;

import javax.swing.text.AbstractDocument;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.BufferedWriter;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.lang.IllegalStateException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Map<String, String>, Map<String, String>> {
	@Override
	public Map<String, String> handleRequest(final Map<String, String> input, final Context context) {

//        HttpResponse<String> response = null;
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//        headers.put("X-Custom-Header", "application/json");
//        APIGatewayProxyResponseEvent res = new APIGatewayProxyResponseEvent()
//                .withHeaders(headers);
		System.out.println("Input - object to string:...");
		System.out.println(input);
		LambdaLogger logger = context.getLogger();

		try {
			logger.log(input.toString());
			String subject = "TestSubject";
			String content = input.getOrDefault("message", "get message failed");
			Date now = new Date();
			String message = String.format("Message: %s , Time: %s ", content, now.toString());
			logger.log(message);
				
			// The state send with request in success field to tell the machine where it should be success/failure/error
			String stateRequest = input.getOrDefault("success", "ERROR");
			String isSuccess = "default";
			if (stateRequest.equals("SUCCESS")) {
				AmazonSNSSQS.publishMessageNoSubcribe(subject, message);
				isSuccess = "SUCCESS";
			} else if (stateRequest.equals("FAILURE")) {
				isSuccess = "FAILURE";
			} else {
				isSuccess = "ERROR";
			}
			Map<String, String> output = new HashMap<>();
			output.put("success", isSuccess);
			return output;
//        	Map<String,String> bodyContent  = new HashMap<>();
//        	bodyContent.put("success", isSuccess);
//            //String bodyContent = String.format("{\"success\": %s}", isSuccess);
//            return res
//                    .withStatusCode(200)
//                    .withBody(bodyContent.toString());

		} catch (Exception e) {
			logger.log(e.getMessage());
		}
//        return res
//                .withBody("{}")
//                .withStatusCode(500);
		System.out.println("Lambda called but failed!...");
		System.out.println(input);
		return null;

	}

}
