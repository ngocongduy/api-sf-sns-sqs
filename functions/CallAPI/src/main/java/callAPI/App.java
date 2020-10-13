package CallAPI.src.main.java.callAPI;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import CallAPI.src.main.java.callAPI.models.MyJsonObject;

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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<SQSEvent, APIGatewayProxyResponseEvent> {

	@Override
	public APIGatewayProxyResponseEvent handleRequest(SQSEvent event, Context context) {
		
//		context.getLogger().log("Received event: " + event);
//        SQSMessage message = event.getRecords().get(0);
//        context.getLogger().log("SQS event triggered and get message: " + message.getBody().toString());
//		
//        
		String url = "https://apnhany5o7.execute-api.ap-southeast-1.amazonaws.com/dev/101";
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                                .GET()
                                .header("accept", "application/json")
                                .uri(URI.create(url))
                                .build();
        HttpResponse<String> response = null;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        APIGatewayProxyResponseEvent res = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            MyJsonObject json = mapper.readValue(response.body(),MyJsonObject.class);
            System.out.println(json);
            return res
                    .withStatusCode(200)
                    .withBody(json.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        return res
//                .withBody("{}")
//                .withStatusCode(500);
		System.out.println("Lambda called to called with exception!...");
        System.out.println(event);
        return null;
	}

}

