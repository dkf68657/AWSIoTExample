package com.haresh.demo.device;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.haresh.demo.device.ShadowThing.Document;

@Service
public class DeviceService {

	@Autowired
	private PublishSubscribeMessage publishSubscribeMessage;
	
	@Autowired
	private UpdateDeviceShadow updateDeviceShadow;
	
	@Autowired
	private DeviceShadowSubscribe deviceShadowSubscribe;
	
	public void sentTelemetryMessage(BulbType bulbType, boolean status) throws InterruptedException, AWSIotException, AWSIotTimeoutException, IOException {
		
		String payload = "{'"+bulbType+"' : "+getStatus(status)+"}";
		publishSubscribeMessage.sendMessage(payload);
		updateDeviceShadow.updateDeviceReportedState(bulbType, status);
	}
	
	private String getStatus(boolean value) {
		return value ? "on" : "Off";
	}
	
	public void updateDeviceDesiredStatus(BulbType bulbType, boolean status) throws IOException, AWSIotException, AWSIotTimeoutException, InterruptedException {
		updateDeviceShadow.updateDeviceDesiredState(bulbType, status);
	}
	
	/**
	 * This method need to invoke on while Application UP.
	 * There is a another post construct is written for this simulator hence invoking manually
	 * @throws InterruptedException
	 * @throws AWSIotException
	 */
	public void getDeviceStatus() throws InterruptedException, AWSIotException {
		deviceShadowSubscribe.getDesiredStateOfDevice();
	}
	
	
	public Document getDeviceLastStatus() throws InterruptedException, AWSIotException, IOException, AWSIotTimeoutException {
		return updateDeviceShadow.getDeviceLastStatus();
	}
	
	
}
