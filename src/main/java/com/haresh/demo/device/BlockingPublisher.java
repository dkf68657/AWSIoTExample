package com.haresh.demo.device;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;

public class BlockingPublisher {

	private final AWSIotMqttClient awsIotClient;

	private static final String TestTopic = "common/colour/JavaBulbDevice";

	private String payload;

	public BlockingPublisher(AWSIotMqttClient awsIotClient, String payload) {
		this.awsIotClient = awsIotClient;
		this.payload = payload;
	}

	public void publishMessage() {
		try {
			awsIotClient.publish(TestTopic, payload);
		} catch (AWSIotException e) {
			System.out.println(System.currentTimeMillis() + ": publish failed for " + payload);
		}
		System.out.println(System.currentTimeMillis() + ": >>> " + payload);
	}

}
