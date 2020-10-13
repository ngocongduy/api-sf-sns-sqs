package ReadResultRDS.src.main.java.readResultRDS;
import ReadResultRDS.src.main.java.readResultRDS.JDBCSample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

//public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
public class App implements RequestHandler<Object, Map<String,String>> {
	
	@Override
    public Map<String,String> handleRequest(final Object input, final Context context) {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//        headers.put("X-Custom-Header", "application/json");
//
//        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
//                .withHeaders(headers);
		System.out.println("Input from ReadResultFunction as an Object to string!...");
    	System.out.println(input);
    	Map<String,String> output = new HashMap<>();
        try {
            JDBCSample jdbcSample = new JDBCSample();
            output = jdbcSample.getAllMessage(context);
//            return response
//                    .withStatusCode(200)
//                    .withBody(output);
        } catch (Exception e) {
            e.printStackTrace();
//            return response
//                    .withBody("{}")
//                    .withStatusCode(500);
        }
        return output;
    }

}
