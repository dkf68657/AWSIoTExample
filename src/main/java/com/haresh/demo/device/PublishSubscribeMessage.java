package com.haresh.demo.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.haresh.demo.sampleUtil.InitIoTClient;

@Component
public class PublishSubscribeMessage {

	//private static final AWSIotQos TestTopicQos = AWSIotQos.QOS0;
	@Autowired
	private InitIoTClient initIoTClient;
	
	@Value("${aws.iot.thingName}")
	private String thingName;
	
	private static AWSIotMqttClient awsIotClient;

	public static void setClient(AWSIotMqttClient client) {
		awsIotClient = client;
	}

	public void sendMessage(String payload) throws InterruptedException, AWSIotException, AWSIotTimeoutException {
		
		awsIotClient = initIoTClient.initClient();
		
		awsIotClient.connect();

		BlockingPublisher blockingPublisher = new BlockingPublisher(awsIotClient, payload);
		blockingPublisher.publishMessage();
		awsIotClient.disconnect();
	}

}
