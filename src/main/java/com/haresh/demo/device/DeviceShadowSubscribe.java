/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.haresh.demo.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.iot.client.AWSIotConnectionStatus;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.haresh.demo.sampleUtil.InitIoTClient;
import com.haresh.demo.websocket.WebSocketIntegration;

@Component
public class DeviceShadowSubscribe {

	@Autowired
	private InitIoTClient initIoTClient;
	
	@Autowired
	private WebSocketIntegration integration; 
	
	@Value("${aws.iot.thingName}")
	private String thingName;
	
    private AWSIotMqttClient awsIotClient;

    public void setClient(AWSIotMqttClient client) {
        awsIotClient = client;
    }

    public void getDesiredStateOfDevice() throws InterruptedException, AWSIotException {
        
    	awsIotClient = initIoTClient.initClient();
        
        awsIotClient.setWillMessage(new AWSIotMessage("client/disconnect", AWSIotQos.QOS0, awsIotClient.getClientId()));

        JavaBulbDevice bulbDevice = new JavaBulbDevice(thingName,integration);
        
        awsIotClient.attach(bulbDevice);
        try {
			awsIotClient.connect(10000);
		} catch (AWSIotTimeoutException e1) {
			e1.printStackTrace();
		}
        
        // Delete existing document if any
        ///bulbDevice.delete();
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				AWSIotConnectionStatus status = AWSIotConnectionStatus.DISCONNECTED;
		        while (true) {
		            AWSIotConnectionStatus newStatus = awsIotClient.getConnectionStatus();
		            if (!status.equals(newStatus)) {
		                System.out.println(System.currentTimeMillis() + " Connection status changed to " + newStatus);
		                status = newStatus;
		            }
		            try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		        }
				
			}
		}).start();
        
    }
}
