package SNSTrigger.src.main.java.snsTrigger.model;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;


public class AmazonSNSSQS {

	private static ProfileCredentialsProvider credentialsProvider; {
		credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. ", e);
        }
	}
	
	public static AmazonSQS createQueue(String queueName) {
		
        AmazonSQS sqsClient = AmazonSQSClientBuilder.standard()
                               //.withCredentials(credentialsProvider)
                               .withRegion(Regions.AP_SOUTHEAST_1)
                               .build();
    	
    	CreateQueueRequest req = new CreateQueueRequest(queueName)
    			.addAttributesEntry("DelaySeconds", "0")
    			.addAttributesEntry("MessageRetentionPeriod", "100");
    	try {
    		sqsClient.createQueue(req);
    	}catch(AmazonSQSException e){
    		if(!e.getErrorCode().equals("QueueAlreadyExists")) {
    			throw e;
    		}
    	}
    	return sqsClient;
    	
    }
	
	public static AmazonSNS createSNS(String topicName) {
		AmazonSNS sns = AmazonSNSClientBuilder.standard()
				//.withCredentials(credentialsProvider)
				.withRegion(Regions.AP_SOUTHEAST_1).build();    	
    	return sns;
	}		
	
    public static String createTopicAndGetSNSTopicArn(AmazonSNS snsClient, String topicName) {
    	CreateTopicRequest tr = new CreateTopicRequest(topicName); 
    	CreateTopicResult ts = snsClient.createTopic(tr);
    	System.out.println(ts);
    	System.out.println("Create topic request " + snsClient.getCachedResponseMetadata(tr));
    	String topicArn = ts.getTopicArn();
    	return topicArn;
    }

    
    public static void subcribeQueueToSNS(AmazonSNS sns, AmazonSQS sqs, String snsTopicArn, String queueName) {

    	String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
    	
    	Topics.subscribeQueue(sns, sqs, snsTopicArn, queueUrl);
    	

    }
    
	public static void publishMessageToQueue(AmazonSQS sqsClient, String queueName, String message) {
		
		GetQueueUrlResult queueURL = sqsClient.getQueueUrl(queueName);
		System.out.println("*********Publishing message to the queue***********");
		sqsClient.sendMessage(queueURL.getQueueUrl(), message);
	}
	
	public static void publishMessageToQueueFromSNS(AmazonSNS snsClient,String snsTopicArn, String subject, String message) {
		try {
			snsClient.publish(new PublishRequest(snsTopicArn, message).withSubject(subject));
			System.out.println("Publish with no exception!");
		}
		catch(Exception e){
			throw e;
		}
	}
	
	public static void consumeMessageFromQueue(AmazonSQS sqsClient, String queueName) {
		GetQueueUrlResult queueURL = sqsClient.getQueueUrl(queueName);
		ReceiveMessageResult messages = sqsClient.receiveMessage(queueURL.getQueueUrl());
		messages.getMessages().forEach(m -> {
			System.out.println("*********Printing the message from queue*************** : "+m.getBody());
			System.out.println("After processing deleting the message from queue");
			sqsClient.deleteMessage(queueURL.getQueueUrl(), m.getReceiptHandle());
		});
		
	}	
	
	public static void publishMessageWithSubcribe(String subject, String message) {
		String queueName1 = "QueueCallAPI";
		String queueName2 = "QueueRDSDatabase";
		String topicName= "SnsTopic";
		
		AmazonSQS sqs1 = createQueue(queueName1);
		AmazonSQS sqs2 = createQueue(queueName2);
		
		AmazonSNS sns = createSNS(topicName);
		
		String topicArn = createTopicAndGetSNSTopicArn(sns, topicName);
		
		subcribeQueueToSNS(sns, sqs1, topicArn, queueName1);
		subcribeQueueToSNS(sns, sqs2, topicArn, queueName2);
		
		publishMessageToQueueFromSNS(sns, topicArn, subject, message);
		
	}
	public static void publishMessageNoSubcribe(String subject, String message) {
		String topicName= "SnsTopic";
		
		AmazonSNS sns = createSNS(topicName);
		
		String topicArn = createTopicAndGetSNSTopicArn(sns, topicName);
		
		publishMessageToQueueFromSNS(sns, topicArn, subject, message);
		
	}
	
	public static void publishMessage(String subject, String message) {
		String topicName= "SnsTopic";
		
		AmazonSNS sns = createSNS(topicName);
		
		String topicArn = createTopicAndGetSNSTopicArn(sns, topicName);
		
		publishMessageToQueueFromSNS(sns, topicArn, subject, message);
		
	}
	
	public static void consumeMessages(String subject, String message) {
		String queueName1 = "QueueCallAPI";
		String queueName2 = "QueueRDSDatabase";

		AmazonSQS sqs1 = createQueue(queueName1);
		AmazonSQS sqs2 = createQueue(queueName2);
		
		consumeMessageFromQueue(sqs1, queueName1);
		consumeMessageFromQueue(sqs2, queueName2);
	}
}
